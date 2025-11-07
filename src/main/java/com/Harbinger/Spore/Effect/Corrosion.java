package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;

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
        if (SConfig.SERVER.corrosion.get().contains(living.getEncodeId())){
            if (this == Seffects.CORROSION.value()) {
                living.hurt(SdamageTypes.acid(living), 1.0F);
            }
        }
        if (living instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && player.tickCount % 60 == 0){
            for (int i : Inventory.ALL_ARMOR_SLOTS){
                player.getInventory().getArmor(i).hurtAndBreak(intensity,serverLevel,player,(p_348383_) -> {});
            }
        }
    }


}
