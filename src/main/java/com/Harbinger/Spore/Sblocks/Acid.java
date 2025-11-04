package com.Harbinger.Spore.Sblocks;


import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sparticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class Acid extends Block {
    public Acid() {
        super(Properties.of().noOcclusion().randomTicks());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);
        {
            return box(0, 0, 0, 16, 1, 16).move(offset.x, offset.y, offset.z);
        }
    }
    @Override
    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 1);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        level.removeBlock(pos,false);
        super.randomTick(state, level, pos, randomSource);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity _entity && Utilities.TARGET_SELECTOR.Test(_entity))
            _entity.addEffect(new MobEffectInstance(Seffects.CORROSION, 100, 0));
        super.entityInside(blockState, level, pos, entity);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState blockstate, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(blockstate, world, pos, random);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int l = 0; l < 2; ++l) {
            double x0 = x + random.nextFloat();
            double z0 = z + random.nextFloat();
            double dx = (random.nextFloat() - 0.5D) * 0.5D;
            double dy = (random.nextFloat() - 0.5D) * 0.5D;
            double dz = (random.nextFloat() - 0.5D) * 0.5D;
            world.addParticle(Sparticles.ACID_PARTICLE.get(), x0, y, z0, dx, dy, dz);
        }
    }

}
