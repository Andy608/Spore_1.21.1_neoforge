package com.Harbinger.Spore.Sblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class VentPlateBlock extends MultifaceBlock {
    public static final MapCodec<VentPlateBlock> CODEC = simpleCodec(VentPlateBlock::new);
    public VentPlateBlock(Properties properties) {
        super(properties);
    }


    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockState blockState = level.getBlockState(pos.above());
        if (player.getPose()  != Pose.SWIMMING && !player.isCrouching()){
            if (blockState.isSolidRender(level,pos.above())){
                player.setPose(Pose.SWIMMING);
            }
            player.moveTo(pos.getX()+0.5,pos.getY(),pos.getZ() +0.5);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return entity.isCrouching();
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().getItem() == this.asItem();
    }
}
