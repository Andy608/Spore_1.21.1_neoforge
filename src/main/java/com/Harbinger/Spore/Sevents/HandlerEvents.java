package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.ExtremelySusThings.ChunkLoadRequest;
import com.Harbinger.Spore.ExtremelySusThings.ChunkLoaderHelper;
import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Hyper;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.Organoid;
import com.Harbinger.Spore.Sentities.Utility.ScentEntity;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SblockEntities;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = Spore.MODID)
public class HandlerEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        DespawnSystem.tickMobCleaner(event.getServer());
        ChunkLoaderHelper.tick();
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ChunkLoadRequest.loadChunkData(level);
        }
    }
    @SubscribeEvent
    public static void TickEvents(PlayerTickEvent.Post event){
        Player player = event.getEntity();
            Level level = player.level();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    ItemStack armorStack = player.getItemBySlot(slot);
                    if (!armorStack.isEmpty() && armorStack.getItem() instanceof SporeBaseArmor sporeBaseArmor) {
                        if (!level.isClientSide) {
                            sporeBaseArmor.tickArmor(player,level);
                        }
                    }
                }
            }
        Holder<MobEffect> madnessHolder = Seffects.MADNESS;
            MobEffectInstance effectInstance = player.getEffect(madnessHolder);
            if (effectInstance != null && effectInstance.getDuration() == 1) {
                int amplifier = effectInstance.getAmplifier();
                if (amplifier > 0) {
                    player.removeEffect(madnessHolder);
                    player.addEffect(new MobEffectInstance(madnessHolder, 12000, amplifier - 1));
                }
            }

    }
    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                SblockEntities.SURGERY_TABLE_ENTITY.get(),
                (blockEntity, context) -> blockEntity.getItemHandler()
        );
    }
    @SubscribeEvent
    private static void registerAttributes(EntityAttributeCreationEvent event){
        MobAttributes.registerAttributes(event);
    }
}
