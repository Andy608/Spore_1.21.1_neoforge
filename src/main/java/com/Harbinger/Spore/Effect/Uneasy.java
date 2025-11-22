package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Uneasy extends MobEffect implements SporeEffectsHandler{
    public Uneasy() {
        super(MobEffectCategory.NEUTRAL, -13434778);
    }


    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
            player.playSound(Ssounds.HEART_BEAT.value());
            return true;
        }
        return false;
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        return duration % 80 == 0;
    }

    @Override
    public void triggerEffects(LivingEntity entity, int intensity) {
        if (entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
            player.playSound(Ssounds.HEART_BEAT.value());
        }
    }
}
