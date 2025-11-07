package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Symbiosis extends MobEffect implements SporeEffectsHandler{
    public Symbiosis() {
        super(MobEffectCategory.BENEFICIAL, 8412043);
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
                    player.causeFoodExhaustion(0.1F);
                    return true;
        }
        return false;
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        if (this == Seffects.SYMBIOSIS.value()) {
            int i = 40 >> intensity;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return false;
    }
}
