package com.Harbinger.Spore.Sblocks;

import com.Harbinger.Spore.SBlockEntities.ZoaholicBlockEntity;
import com.Harbinger.Spore.core.SblockEntities;
import com.Harbinger.Spore.core.Sitems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZoaholicBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final Properties defaultProperties = Properties.of().sound(SoundType.STONE).strength(6f, 20f);
    public static final MapCodec<ZoaholicBlock> CODEC = simpleCodec(ZoaholicBlock::new);

    // Custom data component keys
    public static final String BIOMASS_KEY = "biomass";
    public static final String HEART_KEY = "heart";
    public static final String BRAIN_KEY = "brain";
    public static final String INNARDS_KEY = "innards";

    public ZoaholicBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ZoaholicBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);
        return box(0.1, 0, 0.1, 15.9, 16, 15.9).move(offset.x, offset.y, offset.z);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof ZoaholicBlockEntity zoaholicBlock) {
            if (!zoaholicBlock.HasBrain() && stack.getItem() == Sitems.CEREBRUM.get()) {
                zoaholicBlock.setBrain(true);
                stack.shrink(1);
            } else if (!zoaholicBlock.HasHeart() && stack.getItem() == Sitems.MUTATED_HEART.get()) {
                zoaholicBlock.setHasHeart(true);
                stack.shrink(1);
            } else if (!zoaholicBlock.hasEnoughInnards() && stack.getItem() == Sitems.INNARDS.get()) {
                zoaholicBlock.setAmountOfInnards(zoaholicBlock.getAmountOfInnards() + 1);
                stack.shrink(1);
            } else if (zoaholicBlock.getBiomass() <= 9000 && stack.getItem() == Sitems.BIOMASS.get()) {
                zoaholicBlock.addBiomass(3000);
                stack.shrink(1);
            } else {
                if (zoaholicBlock.isActive() && zoaholicBlock.getProcessing() <= 0) {
                    zoaholicBlock.setProcessing(200);
                } else if (zoaholicBlock.HasHeart() && zoaholicBlock.hasEnoughInnards() && zoaholicBlock.HasBrain()) {
                    String string = Component.translatable(Sitems.BIOMASS.get().getDescriptionId()).getString();
                    player.displayClientMessage(Component.literal(string + " " + zoaholicBlock.getBiomass() + "/12000"), true);
                } else {
                    player.displayClientMessage(Component.translatable("zoaholic.line_1"), true);
                }
            }
            if (player.isShiftKeyDown() && player instanceof ServerPlayer && !level.isClientSide) {
                player.openMenu(zoaholicBlock);
                NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createCDUTicker(level, type, SblockEntities.ZOAHOLIC.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCDUTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends ZoaholicBlockEntity> blockEntityType) {
        return level.isClientSide ? createTickerHelper(type, blockEntityType, ZoaholicBlockEntity::clientTick) : createTickerHelper(type, blockEntityType, ZoaholicBlockEntity::serverTick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            boolean hasBrain = customData.copyTag().getBoolean(BRAIN_KEY);
            boolean hasHeart = customData.copyTag().getBoolean(HEART_KEY);
            int innards = customData.copyTag().getInt(INNARDS_KEY);
            int biomass = customData.copyTag().getInt(BIOMASS_KEY);

            if (hasBrain && hasHeart && innards >= 2) {
                String string = Component.translatable(Sitems.BIOMASS.get().getDescriptionId()).getString();
                components.add(Component.literal(string + " " + biomass + "/12000"));
            } else {
                components.add(Component.translatable("zoaholic.line_2"));
                if (!hasBrain) {
                    components.add(Sitems.CEREBRUM.get().getDescription());
                }
                if (!hasHeart) {
                    components.add(Sitems.MUTATED_HEART.get().getDescription());
                }
                if (innards < 1) {
                    components.add(Sitems.INNARDS.get().getDescription());
                }
                if (innards < 2) {
                    components.add(Sitems.INNARDS.get().getDescription());
                }
            }
        } else {
            components.add(Component.translatable("zoaholic.line_2"));
            components.add(Sitems.CEREBRUM.get().getDescription());
            components.add(Sitems.MUTATED_HEART.get().getDescription());
            components.add(Sitems.INNARDS.get().getDescription());
            components.add(Sitems.INNARDS.get().getDescription());
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof ZoaholicBlockEntity zoaholicBlockEntity) {
            ItemStack stack = new ItemStack(this);
            var tag = CustomData.EMPTY.update(t -> {
                t.putInt(BIOMASS_KEY, zoaholicBlockEntity.getBiomass());
                t.putBoolean(HEART_KEY, zoaholicBlockEntity.HasHeart());
                t.putBoolean(BRAIN_KEY, zoaholicBlockEntity.HasBrain());
                t.putInt(INNARDS_KEY, zoaholicBlockEntity.getAmountOfInnards());
            });

            stack.set(DataComponents.CUSTOM_DATA, tag);
            ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            level.addFreshEntity(item);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        if (level.getBlockEntity(pos) instanceof ZoaholicBlockEntity zoaholicBlock) {
            // Read data from custom data component
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                var tag = customData.copyTag();
                zoaholicBlock.setBiomass(tag.getInt(BIOMASS_KEY));
                zoaholicBlock.setAmountOfInnards(tag.getInt(INNARDS_KEY));
                zoaholicBlock.setHasHeart(tag.getBoolean(HEART_KEY));
                zoaholicBlock.setBrain(tag.getBoolean(BRAIN_KEY));
            }
        }
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}