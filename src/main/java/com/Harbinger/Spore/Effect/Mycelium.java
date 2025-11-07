package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Mycelium extends MobEffect implements SporeEffectsHandler{
    public Mycelium() {
        super(MobEffectCategory.HARMFUL, 9643043);
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        int i = 80 >> intensity;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    public void triggerEffects(LivingEntity entity, int intensity) {
        if (!SConfig.SERVER.mycelium.get().contains(entity.getEncodeId())){
            if (this ==  Seffects.MYCELIUM.value()) {
                if (!entity.level().isClientSide && entity instanceof Player player && player.getFoodData().getFoodLevel() > 0 && intensity < 1){
                    player.causeFoodExhaustion(1.0F);
                }else {
                    entity.hurt(SdamageTypes.mycelium_overtake(entity), 1.0F);
                }
            }
        }
    }
}