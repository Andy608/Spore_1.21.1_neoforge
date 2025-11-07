package com.Harbinger.Spore.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Symbiosis extends MobEffect implements SporeEffectsHandler{
    public Symbiosis() {
        super(MobEffectCategory.BENEFICIAL, 8412043);
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        int i = 40 >> intensity;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void triggerEffects(LivingEntity living, int intensity) {
        if (living instanceof Player player) {
            player.causeFoodExhaustion(0.1F);
        }
    }
}
