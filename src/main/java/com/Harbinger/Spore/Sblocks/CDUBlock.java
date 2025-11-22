package com.Harbinger.Spore.Sblocks;


import com.Harbinger.Spore.SBlockEntities.CDUBlockEntity;
import com.Harbinger.Spore.core.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.Harbinger.Spore.core.SdataComponents.CDU_FUEL;
import static com.Harbinger.Spore.core.SdataComponents.DATA_COMPONENTS;

public class CDUBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final Properties defaultProperties = Properties.of().sound(SoundType.STONE).strength(6f, 20f);
    public static final MapCodec<CDUBlock> CODEC = simpleCodec(CDUBlock::new);
    public CDUBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CDUBlockEntity(pos,state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public static void replaceCDU(BlockPos pos,Level level){
        if (level == null || level.isClientSide){
            return;
        }
        BlockState blockState = level.getBlockState(pos);
        if (blockState.equals(Sblocks.CDU.get().defaultBlockState())){
            level.setBlock(pos,blockState.getBlock().defaultBlockState().setValue(LIT,true),3);
        }
    }
    public static boolean isCDUUsable(BlockPos pos,Level level){
        if (level == null || level.isClientSide){
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if (!state.getBlock().equals(Sblocks.CDU.get())){
            return true;
        }
        return !state.getValue(LIT);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(state, level, blockPos, randomSource);
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (!state.getValue(LIT)){
            if (entity instanceof CDUBlockEntity blockEntity && blockEntity.getFuel() > 0){
                for (int i = 0; i < 360; i++) {
                    if (i % 20 == 0) {
                        double yy = Math.sin(i) * Math.cos(i) * 0.25d;
                        level.addParticle(ParticleTypes.SNOWFLAKE,
                                blockPos.getX()+0.47, blockPos.getY() + 1, blockPos.getZ()+0.47,
                                Math.cos(i) * 0.15d, yy, Math.sin(i) * 0.15d);
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);
        {
            return box(0.1, 0, 0.1, 15.9, 16, 15.9).move(offset.x, offset.y, offset.z);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(LIT);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof CDUBlockEntity blockEntity){
            ItemStack item = player.getItemInHand(hand);
            if (item.getItem() == Sitems.ICE_CANISTER.get()){
                if (blockEntity.getFuel() > 0){
                    player.displayClientMessage(Component.literal("Current fuel " + blockEntity.getFuel() + "/" + blockEntity.maxFuel),true);
                }else{
                    level.playLocalSound(pos, Ssounds.CDU_INSERT.value(), SoundSource.BLOCKS,2f,2f,true);
                    level.playLocalSound(pos, Ssounds.CDU_AMBIENT.value(), SoundSource.BLOCKS,1f,1f,true);
                    blockEntity.setFuel(blockEntity.maxFuel);
                    item.shrink(1);
                }
                return ItemInteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown() && player instanceof ServerPlayer && !level.isClientSide){
                player.openMenu(blockEntity);
                NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
                return ItemInteractionResult.SUCCESS;
            }
            player.displayClientMessage(Component.literal("Current fuel " + blockEntity.getFuel() + "/" + blockEntity.maxFuel),true);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153274_, BlockEntityType<T> type) {
        return createCDUTicker(level, type, SblockEntities.CDU.get());
    }
    public int getEntityFuel(Level level,BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof  CDUBlockEntity blockEntity){
            return blockEntity.getFuel();
        }
        return 0;
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCDUTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends CDUBlockEntity> p_151990_) {
        return level.isClientSide ? null : createTickerHelper(type, p_151990_, CDUBlockEntity::serverTick);
    }

    public void setFuelTag(ItemStack stack,int value){
        stack.set(CDU_FUEL,value);
    }
    public int getFuelTag(ItemStack stack){
        return stack.getOrDefault(CDU_FUEL,0);
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("cdu.line").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Sitems.ICE_CANISTER.get().getDescription());
        tooltipComponents.add(Component.literal(this.getFuelTag(stack) +"/12000").withStyle(ChatFormatting.DARK_BLUE));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        ItemStack stack = new ItemStack(this);
        this.setFuelTag(stack,this.getEntityFuel(level,pos));
        ItemEntity item = new ItemEntity(level, pos.getX() , pos.getY(),pos.getZ(),stack);
        level.addFreshEntity(item);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CDUBlockEntity cduBlockEntity){
            cduBlockEntity.setFuel(stack.getOrDefault(SdataComponents.CDU_FUEL,0));
        }
    }
}
