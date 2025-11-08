package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Corrosion extends MobEffect implements SporeEffectsHandler{
    public Corrosion() {
        super(MobEffectCategory.HARMFUL, -13369549);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int intensity) {
        return duration % 60 == 0;
    }

    @Override
    public void triggerEffects(LivingEntity living, int intensity) {
        if (SConfig.SERVER.corrosion.get().contains(living.getEncodeId())) {
            if (this == Seffects.CORROSION.value()) {
                double originalMotionX = living.getDeltaMovement().x;
                double originalMotionY = living.getDeltaMovement().y;
                double originalMotionZ = living.getDeltaMovement().z;
                living.hurt(SdamageTypes.acid(living), intensity + 1.0F);
                living.setDeltaMovement(originalMotionX, originalMotionY, originalMotionZ);
            }
        }
    }
}
