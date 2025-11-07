package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
        if (living instanceof Player player && player.level() instanceof ServerLevel serverLevel) {
            for (int slotIndex : Inventory.ALL_ARMOR_SLOTS) {
                ItemStack armorStack = player.getInventory().getArmor(slotIndex);
                if (!armorStack.isEmpty()) {
                    armorStack.hurtAndBreak(
                            intensity + 1,
                            serverLevel,
                            player,
                            item -> {
                                player.onEquippedItemBroken(armorStack.getItem(),EquipmentSlot.values()[slotIndex]);
                            }
                    );
                }
            }
        }
    }
}
