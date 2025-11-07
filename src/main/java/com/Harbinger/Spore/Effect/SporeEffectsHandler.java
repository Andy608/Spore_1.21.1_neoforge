package com.Harbinger.Spore.Effect;

import net.minecraft.world.entity.LivingEntity;

public interface SporeEffectsHandler {
    boolean isDurationEffectTick(int duration, int intensity);
    void triggerEffects(LivingEntity living ,int intensity);
}
