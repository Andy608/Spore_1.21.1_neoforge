package com.Harbinger.Spore.Sblocks;

import com.Harbinger.Spore.SBlockEntities.ContainerBlockEntity;
import com.Harbinger.Spore.SBlockEntities.SurgeryTableBlockEntity;
import com.Harbinger.Spore.Screens.SurgeryMenu;
import com.Harbinger.Spore.core.SblockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import org.jetbrains.annotations.Nullable;

public class SurgeryTableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape SHAPE = Block.box(0.1, 0, 0.1, 15.9, 16, 15.9);
    public static final MapCodec<SurgeryTableBlock> CODEC = simpleCodec(SurgeryTableBlock::new);
    public static final Properties defaultProperties = Properties.of().sound(SoundType.STONE).strength(6f, 20f);
    public SurgeryTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SurgeryTableBlockEntity(blockPos,blockState);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SurgeryTableBlockEntity) {
                ((SurgeryTableBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, param);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153274_, BlockEntityType<T> type) {
        return createSurgeryTicker(level, type, SblockEntities.SURGERY_TABLE_ENTITY.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createSurgeryTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends SurgeryTableBlockEntity> p_151990_) {
        return level.isClientSide ? null : createTickerHelper(type, p_151990_, SurgeryTableBlockEntity::serverTick);
    }

    public BlockState rotate(BlockState p_54360_, Rotation p_54361_) {
        return p_54360_.setValue(FACING, p_54361_.rotate(p_54360_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_54357_, Mirror p_54358_) {
        return p_54357_.rotate(p_54358_.getRotation(p_54357_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING,WATERLOGGED);
    }
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof SurgeryTableBlockEntity surgeryTableBlock && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.spore.surgery_table");
                }

                @Override
                public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    return new SurgeryMenu(containerId, playerInventory, surgeryTableBlock);
                }

                @Override
                public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
                    MenuProvider.super.writeClientSideData(menu, buffer);
                    buffer.writeBlockPos(pos);
                }
            });
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
  }
}
