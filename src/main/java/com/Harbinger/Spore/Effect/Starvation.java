package com.Harbinger.Spore.Effect;


import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Starvation extends MobEffect implements SporeEffectsHandler{
    public Starvation() {
        super(MobEffectCategory.HARMFUL, 34613);
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        int i = 80 >> intensity;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void triggerEffects(LivingEntity entity, int intensity) {
        if (entity instanceof Infected){
            if (this == Seffects.STARVATION.value()) {
                double originalMotionX = entity.getDeltaMovement().x;
                double originalMotionY = entity.getDeltaMovement().y;
                double originalMotionZ = entity.getDeltaMovement().z;
                entity.hurt(entity.damageSources().generic(), 1.0F);
                entity.setDeltaMovement(originalMotionX, originalMotionY, originalMotionZ);
            }
        }
    }

}
