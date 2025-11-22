package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.Effect.SporeEffectsHandler;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorData;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsBaseItem;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class LivingTickEvent {
    private static final TagKey<Block> tag = BlockTags.create(ResourceLocation.parse("spore:fungal_blocks"));
    public static void TickEvents(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }
        for (MobEffectInstance instance : living.getActiveEffects()) {
            int amp = instance.getAmplifier();
            MobEffect effect = instance.getEffect().value();
            if (effect instanceof SporeEffectsHandler handler) {
                if (handler.isDurationEffectTick(instance.getDuration(), amp)) {
                    handler.triggerEffects(living, amp);
                }
            }
        }

        if (living instanceof Player player) {
            MobEffectInstance effectInstance = player.getEffect(Seffects.MADNESS);
            if (effectInstance != null && effectInstance.getDuration() == 1) {
                int level = effectInstance.getAmplifier();
                if (level > 0) {
                    effectInstance.update(new MobEffectInstance(Seffects.MADNESS, 12000, level - 1));
                }
            }

            if (player.tickCount % 400 == 0 && player.level().isClientSide) {
                AABB aabb = player.getBoundingBox().inflate(5);

                List<BlockPos> list = new ArrayList<>();

                BlockPos.betweenClosedStream(aabb)
                        .forEach(blockPos -> {
                            BlockPos immutablePos = blockPos.immutable();
                            if (player.level().getBlockState(immutablePos).is(tag)) {
                                list.add(immutablePos);
                            }
                        });

                if (list.size() > 4) {
                    player.playSound(Ssounds.AREA_AMBIENT.value());
                }
            }


        }
    }
    public static void TickEffects(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        // Corrosion - Manual damage application
        if (player.tickCount % 60 == 0) {
            MobEffectInstance corrosion = player.getEffect(Seffects.CORROSION);
            if (corrosion != null) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                        ItemStack stack = player.getItemBySlot(slot);
                        if (!stack.isEmpty() && stack.isDamageableItem()) {
                            if (stack.getItem() instanceof SporeArmorData data && !data.tooHurt(stack)){
                                return;
                            }
                            int newDamage = stack.getDamageValue() + corrosion.getAmplifier() + 1;
                            if (newDamage < stack.getMaxDamage()) {
                                if (newDamage != stack.getDamageValue()) {
                                    stack.setDamageValue(newDamage);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Symbiosis - Manual healing
        if (player.tickCount % 200 == 0) {
            MobEffectInstance symbiosis = player.getEffect(Seffects.SYMBIOSIS);
            if (symbiosis != null) {
                int healAmount = (symbiosis.getAmplifier() + 1)  * 2;

                // Heal inventory
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (shouldHealStack(player, stack)) {
                        if (stack.getItem() instanceof SporeToolsBaseItem base) {
                            base.healTool(stack, healAmount);
                        } else if (stack.getItem() instanceof SporeArmorData base) {
                            base.healTool(stack, healAmount);
                        }
                        int newDamage = Math.max(0, stack.getDamageValue() - healAmount);
                        stack.setDamageValue(newDamage);
                    }
                }
            }
        }
    }

    private static boolean shouldHealStack(Player player, ItemStack stack) {
        return !stack.isEmpty() &&
                stack.isDamaged() &&
                Senchantments.hasEnchant(player.level(), stack, Senchantments.SYMBIOTIC_RECONSTITUTION);
    }

}