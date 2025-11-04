package com.Harbinger.Spore.Effect;


import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Starvation extends MobEffect {
    public Starvation() {
        super(MobEffectCategory.HARMFUL, 34613);
    }
    //To read infected
    public boolean applyEffectTick(LivingEntity entity, int intense) {
        if (entity instanceof Infected){
            if (this == Seffects.STARVATION.value()) {
                    entity.hurt(entity.damageSources().generic(), 1.0F);
                    return true;
                }
        }
        return false;
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        if (this == Seffects.STARVATION.value()) {
            int i = 80 >> intensity;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return false;
    }

}
