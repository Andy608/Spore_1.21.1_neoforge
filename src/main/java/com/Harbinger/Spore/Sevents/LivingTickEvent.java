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
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
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
    public static void TickEvents(EntityTickEvent.Pre event){
        if (!(event.getEntity() instanceof LivingEntity living)){
            return;
        }
        for (MobEffectInstance instance : living.getActiveEffects()){
            int amp = instance.getAmplifier();
            MobEffect effect = instance.getEffect().value();
            if (effect instanceof SporeEffectsHandler handler){
                if (handler.isDurationEffectTick(instance.getDuration(),amp)){
                    handler.triggerEffects(living,amp);
                }
            }
        }
        if (living instanceof Player player){
            MobEffectInstance effectInstance = player.getEffect(Seffects.MADNESS);
            if (effectInstance != null && effectInstance.getDuration() == 1){
                int level = effectInstance.getAmplifier();
                if (level > 0){
                    effectInstance.update(new MobEffectInstance(Seffects.MADNESS,12000,level-1));
                }
            }
            if (player.tickCount % 400 == 0 && player.level().isClientSide){
                AABB aabb = player.getBoundingBox().inflate(5);
                List<BlockPos> list = new ArrayList<>();
                for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))){
                    if (player.level().getBlockState(blockPos).is(tag)){
                        list.add(blockPos);
                    }
                }
                if (list.size() > 4){
                    player.playSound(Ssounds.AREA_AMBIENT.get());
                }
            }
        }
    }
    public static void TickEffects(PlayerTickEvent.Post event){
        Player living = event.getEntity();
        MobEffectInstance CorrosionInstance = living.getEffect(Seffects.CORROSION);
        MobEffectInstance SymbiosisInstance = living.getEffect(Seffects.SYMBIOSIS);
        if (CorrosionInstance != null){
            if (living.tickCount % 60 == 0){
                if (living.level() instanceof ServerLevel serverLevel) {
                    for (EquipmentSlot slot : EquipmentSlot.values()){
                        if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR){
                            ItemStack armorStack = living.getItemBySlot(slot);
                            if (!armorStack.isEmpty()) {
                                armorStack.hurtAndBreak(
                                        CorrosionInstance.getAmplifier() + 1
                                        ,serverLevel,living, item ->{living.onEquippedItemBroken(item,slot);}
                                );
                            }
                        }
                    }
                }
            }
        }
        if (SymbiosisInstance != null) {
            if (living.tickCount % 200 == 0) {
                int size = living.getInventory().getContainerSize();
                for (int i = 0; i <= size; i++) {
                    ItemStack itemStack = living.getInventory().getItem(i);
                    if (Senchantments.hasEnchant(living.level(), itemStack, Senchantments.SYMBIOTIC_RECONSTITUTION) && itemStack.isDamaged()) {
                        if (itemStack.getItem() instanceof SporeToolsBaseItem base) {
                            base.healTool(itemStack, SymbiosisInstance.getAmplifier() * 2);
                        } else if (itemStack.getItem() instanceof SporeArmorData base) {
                            base.healTool(itemStack, SymbiosisInstance.getAmplifier() * 2);
                        } else {
                            int l = itemStack.getDamageValue() - SymbiosisInstance.getAmplifier() * 2;
                            itemStack.setDamageValue(l);
                        }
                    }
                }
            }
        }

    }
}