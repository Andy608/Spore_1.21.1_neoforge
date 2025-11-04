package com.Harbinger.Spore.Sentities.Projectile;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sentities;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Random;

public class Vomit extends AbstractArrow implements ItemSupplier {
    public Vomit(Level level) {
        super(Sentities.ACID.get(),level);
    }

    public Vomit(EntityType<Vomit> vomitEntityType, Level level) {
        super(vomitEntityType,level);
    }


    public ItemStack getItem() {
        return new ItemStack(Sitems.ACID.get());
    }


    @Override
    public void tick() {
        super.tick();
        if (this.inGround || this.isInFluidType())
            this.discard();
    }

    public static Vomit shoot(Level world, LivingEntity entity, Random random, float power, double damage, int knockback) {
        Vomit entityarrow = new Vomit(Sentities.ACID.get(), world);
        entityarrow.setOwner(entity);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 0.1f, 0);
        entityarrow.setBaseDamage(damage);
        world.addFreshEntity(entityarrow);
        return entityarrow;
    }

    public static Vomit shoot(LivingEntity entity, LivingEntity target,float damage) {
        Vomit entityarrow = new Vomit(Sentities.ACID.get(), entity.level());
        entityarrow.setOwner(entity);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 2;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.1F, dz, 1f * 2, 12.0F);
        entityarrow.setBaseDamage(damage);
        entity.level().addFreshEntity(entityarrow);

        return entityarrow;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity living && Utilities.TARGET_SELECTOR.Test(living)){
            super.onHitEntity(hitResult);
            levels(living);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
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
