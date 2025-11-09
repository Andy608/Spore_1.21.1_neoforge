package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sentities.ArmorPersentageBypass;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Protector;
import com.Harbinger.Spore.Sentities.Organoids.Proto;
import com.Harbinger.Spore.Sentities.Utility.Illusion;
import com.Harbinger.Spore.Sitems.BaseWeapons.DamagePiercingModifier;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.Sitems.PCI;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

public class DamageHandeling {
    public static void DefenseBypass(LivingDamageEvent.Pre event) {
        Entity living = event.getSource().getEntity();
        if (living instanceof Player player && event.getEntity().getItemBySlot(EquipmentSlot.CHEST).equals(ItemStack.EMPTY)){
            ItemStack weapon = player.getMainHandItem();
            if (weapon.getItem() instanceof PCI pci && pci.getCharge(weapon)>0 && !player.getCooldowns().isOnCooldown(pci)){
                int damageMod = SConfig.SERVER.pci_damage_multiplier.get();
                int charge = pci.getCharge(weapon);
                LivingEntity target = event.getEntity();
                boolean freeze = event.getEntity().getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
                float targetHealth = freeze ? target.getHealth()/damageMod : target.getHealth();
                int freezeDamage = charge >= targetHealth ? (int) targetHealth : charge;
                event.setNewDamage(freeze ?freezeDamage * damageMod : freezeDamage);
                pci.setCharge(weapon, charge - freezeDamage);
                target.setTicksFrozen(600);
                player.getCooldowns().addCooldown(pci, (int) Math.ceil(targetHealth / 5f) * 20);
                pci.playSound(player);
            }
        }
        if(event.getEntity() instanceof Infected victim && !(victim instanceof Protector)) {
            LivingEntity attacker = living instanceof LivingEntity e ? e : null;
            List<Protector> protectorList = SporeSavedData.protectorList();
            if (!protectorList.isEmpty() && attacker != null){
                for (Protector protector1 : protectorList){
                    double d0 = protector1.distanceTo(attacker);
                    if (protector1.isAlive() && d0 < 64f && !attacker.isSpectator() && Utilities.TARGET_SELECTOR.Test(attacker)){
                        protector1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,0));
                        protector1.setTarget(attacker);
                    }
                }
            }
        }
        if (living instanceof ArmorPersentageBypass bypass){
            float original_damage = event.getOriginalDamage();
            float recalculatedDamage = bypass.amountOfDamage(original_damage);
            if (recalculatedDamage >= 0 && original_damage < recalculatedDamage){
                event.setNewDamage(recalculatedDamage);
            }
        }
        if (living instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() instanceof DamagePiercingModifier piercingModifier){
            float original_damage = event.getOriginalDamage();
            float recalculatedDamage = piercingModifier.getMinimalDamage(original_damage);
            if (recalculatedDamage >= 0 && original_damage < recalculatedDamage){
                event.setNewDamage(recalculatedDamage);
            }
        }
        if (living instanceof UtilityEntity && !(living instanceof Illusion)){
            LivingEntity livingEntity = event.getEntity();
            MobEffectInstance mobEffectInstance = livingEntity.getEffect(Seffects.MADNESS);
            if (mobEffectInstance != null){
                int level = mobEffectInstance.getAmplifier();
                int duration = mobEffectInstance.getDuration() +1200;
                boolean jumpLevel = duration < 12000;
                livingEntity.addEffect(new MobEffectInstance(Seffects.MADNESS,jumpLevel ? duration: duration-12000,jumpLevel ? level : level+1));
            }
        }
        if (event.getEntity() instanceof Player player) {
            float totalDamageModification = 0.0F;

            for (ItemStack stack : player.getArmorSlots()) {
                if (stack.getItem() instanceof SporeBaseArmor armor) {
                    totalDamageModification += armor.calculateAdditionalDamage(event.getSource(), stack, event.getOriginalDamage());
                }
            }
            event.setNewDamage(event.getOriginalDamage() + totalDamageModification);
        }
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer){
            int i = 0;
            for (ItemStack stack : serverPlayer.getInventory().armor){
                if (stack.getItem() instanceof SporeBaseArmor baseArmor && baseArmor.getVariant(stack) == SporeArmorMutations.CHARRED){
                    i=i+2;
                }
            }
            if (i > 0){
                event.getEntity().setRemainingFireTicks(i * 20);
            }
        }
        if (event.getSource().getEntity() instanceof Mob attacker){
            CompoundTag data = attacker.getPersistentData();
            if (data.contains("hivemind")) {
                int summonerUUID = data.getInt("hivemind");
                Level level = attacker.level();
                Entity summoner = level.getEntity(summonerUUID);

                if (summoner instanceof Proto smartMob) {
                    int decision = data.getInt("decision");
                    int member = data.getInt("member");
                    smartMob.praisedForDecision(decision,member);
                }
            }
        }
        if (event.getEntity() instanceof Mob creature){
            CompoundTag data = creature.getPersistentData();
            if (data.contains("hivemind")) {
                int summonerUUID = data.getInt("hivemind");
                Level level = creature.level();
                Entity summoner = level.getEntity(summonerUUID);
                if (summoner instanceof Proto smartMob) {
                    int decision = data.getInt("decision");
                    int member = data.getInt("member");
                    smartMob.punishForDecision(decision,member);
                }
            }
        }
    }
}
