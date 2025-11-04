package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Corrosion extends MobEffect {
    public Corrosion() {
        super(MobEffectCategory.HARMFUL, -13369549);
    }
    public boolean applyEffectTick(LivingEntity entity, int p_19468_) {
        if (SConfig.SERVER.corrosion.get().contains(entity.getEncodeId())){
            if (this == Seffects.CORROSION.value()) {
                entity.hurt(SdamageTypes.acid(entity), 1.0F);
                return true;
            }
        }
        return false;
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        if (this == Seffects.CORROSION.value()) {
            int i = 60 >> intensity;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return false;
    }
}
