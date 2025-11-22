package com.Harbinger.Spore.Sentities.Projectile;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.core.Sblocks;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sentities;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Random;

public class AcidBall extends AbstractArrow implements ItemSupplier {
    public AcidBall(Level level) {
        super(Sentities.ACID_BALL.get(),level);
    }

    public AcidBall(EntityType<AcidBall> acidBallEntityType, Level level) {
        super(acidBallEntityType, level);
    }


    public ItemStack getItem() {
        return new ItemStack(Sitems.ACID_BALL.get());
    }


    @Override
    public void tick() {
        super.tick();
        if (this.inGround || this.isInFluidType())
            this.discard();
    }


    public static AcidBall shoot(Level world, LivingEntity entity, Random random, float power, double damage, int knockback) {
        AcidBall entityarrow = new AcidBall(Sentities.ACID_BALL.get(), world);
        entityarrow.setOwner(entity);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setBaseDamage(damage);
        world.addFreshEntity(entityarrow);
        return entityarrow;
    }

    public static AcidBall shoot(LivingEntity entity, LivingEntity target,float damage) {
        AcidBall entityarrow = new AcidBall(Sentities.ACID_BALL.get(),entity.level());
        entityarrow.setOwner(entity);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 2;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setBaseDamage(damage);
        entity.level().addFreshEntity(entityarrow);

        return entityarrow;
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        place_acid(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(),
                blockHitResult.getBlockPos().getZ());

    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Sitems.BILE.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity living && Utilities.TARGET_SELECTOR.Test(living)){
            super.onHitEntity(hitResult);
            levels(living);
        }
    }

    public static void place_acid(LevelAccessor world, double x, double y, double z) {
        if ((world.getBlockState(new BlockPos((int) x, (int) y + 1, (int) z)).isAir())&&
             world.getBlockState(new BlockPos((int) x,(int)  y, (int) z)).canOcclude()) {
            world.setBlock(new BlockPos((int) x, (int) y + 1, (int) z), Sblocks.ACID.get().defaultBlockState(), 3);
        }
    }

    private void levels(LivingEntity living) {
        int level = 0;
        MobEffectInstance instance = living.getEffect(Seffects.CORROSION);
        if (instance != null){
            level = instance.getAmplifier() +1;
        }
        living.addEffect(new MobEffectInstance(Seffects.CORROSION,300,level));
    }
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SLIME_JUMP_SMALL;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

}
