package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sentities.ArmorPersentageBypass;
import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Protector;
import com.Harbinger.Spore.Sentities.EvolvingInfected;
import com.Harbinger.Spore.Sentities.Organoids.Proto;
import com.Harbinger.Spore.Sentities.Utility.Illusion;
import com.Harbinger.Spore.Sentities.Utility.ScentEntity;
import com.Harbinger.Spore.Sitems.BaseWeapons.DamagePiercingModifier;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.Sitems.PCI;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;

import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Sentities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class DamageHandeling {
    private static float clampMin(float current, float min) {
        return Math.max(current, min);
    }
    public static void DefenseBypass(LivingDamageEvent.Pre event) {

        Entity attacker = event.getSource().getEntity();
        LivingEntity target = event.getEntity();

        float dmg = event.getNewDamage();  // always modify THIS value
        float modified = dmg;

        if (attacker instanceof Player player && target.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            ItemStack weapon = player.getMainHandItem();

            if (weapon.getItem() instanceof PCI pci &&
                    pci.getCharge(weapon) > 0 &&
                    !player.getCooldowns().isOnCooldown(pci)) {
                float targetHealth = target.getHealth();
                int charge = pci.getCharge(weapon);
                int dmgMod = SConfig.SERVER.pci_damage_multiplier.get();
                int freezeDamage = charge >= targetHealth ? (int) targetHealth : charge;
                boolean freeze = target.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
                modified = clampMin(modified, (float) (freeze ? freezeDamage * dmgMod : freezeDamage));
                target.setTicksFrozen(600);

                pci.setCharge(weapon, charge - freezeDamage);

                player.getCooldowns().addCooldown(pci, 20);

                pci.playSound(player);
            }
        }

        /* --------------------------------------------------------
         *  PROTECTOR LOGIC
         * -------------------------------------------------------- */
        if (target instanceof Infected victim && !(victim instanceof Protector)) {

            LivingEntity lAttacker = attacker instanceof LivingEntity le ? le : null;

            if (lAttacker != null) {
                List<Protector> protectorList = SporeSavedData.protectorList();
                for (Protector protector : protectorList) {
                    if (protector.isAlive() &&
                            protector.distanceTo(lAttacker) < 64 &&
                            !lAttacker.isSpectator() &&
                            Utilities.TARGET_SELECTOR.Test(lAttacker)) {

                        protector.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0));
                        protector.setTarget(lAttacker);
                    }
                }
            }
        }

        /* --------------------------------------------------------
         *  ARMOR PERCENTAGE BYPASS (NOW A MULTIPLIER)
         * -------------------------------------------------------- */
        if (attacker instanceof ArmorPersentageBypass bypass) {
            float minimum = bypass.amountOfDamage(dmg); // you return a minimum
            modified = clampMin(modified, minimum);
        }

        /* --------------------------------------------------------
         *  DAMAGE PIERCING WEAPONS (MULTIPLIER ONLY)
         * -------------------------------------------------------- */
        if (attacker instanceof LivingEntity living &&
                living.getMainHandItem().getItem() instanceof DamagePiercingModifier pierce) {

            float minimum = pierce.getMinimalDamage(dmg);
            modified = clampMin(modified, minimum);
        }

        /* --------------------------------------------------------
         *  MADNESS EFFECT INCREASE (safe)
         * -------------------------------------------------------- */
        if (attacker instanceof UtilityEntity && !(attacker instanceof Illusion)) {
            MobEffectInstance inst = target.getEffect(Seffects.MADNESS);

            if (inst != null) {
                int level = inst.getAmplifier();
                int dur = inst.getDuration() + 1200;
                boolean jump = dur < 12000;

                target.addEffect(new MobEffectInstance(
                        Seffects.MADNESS,
                        jump ? dur : dur - 12000,
                        jump ? level : level + 1
                ));
            }
        }

        /* --------------------------------------------------------
         *  ARMOR DAMAGE BONUS (ADDITIVE, NOT REPLACEMENT)
         * -------------------------------------------------------- */
        if (target instanceof Player player) {
            for (ItemStack stack : player.getArmorSlots()) {
                if (stack.getItem() instanceof SporeBaseArmor armor) {
                    modified += armor.calculateAdditionalDamage(
                            event.getSource(), stack, dmg
                    );
                }
            }
        }

        if (attacker instanceof ServerPlayer sp) {
            int fire = 0;
            int thornLevel = 0;
            for (ItemStack stack : sp.getInventory().armor) {
                if (stack.getItem() instanceof SporeBaseArmor base &&
                        base.getVariant(stack) == SporeArmorMutations.CHARRED) {

                    fire += 2;
                }
                if (stack.getItem() instanceof ArmorItem) {

                    thornLevel += thornLevel;
                }
            }

            if (fire > 0) {
                target.setRemainingFireTicks(fire * 20);
            }

        }
        if (attacker instanceof LivingEntity living){
            int thornLevel = 0;
            int mutagenic = 0;
            int unwavering = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()){
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
                ItemStack stack = target.getItemBySlot(slot);
                if (!stack.isEmpty()){
                    if (Senchantments.hasEnchant(target.level(),stack,Senchantments.SERRATED_THORNS)){
                        thornLevel++;
                    }
                    if (Senchantments.hasEnchant(target.level(),stack,Senchantments.MUTAGENIC_REACTANT)){
                        mutagenic++;
                    }
                    if (Senchantments.hasEnchant(target.level(),stack,Senchantments.UNWAVERING_NATURE)){
                        unwavering++;
                    }
                }

            }
            if (thornLevel > 0) {
                handleSpikes(thornLevel,living);
            }
            if (mutagenic > 0) {
                mutagenicLevel(mutagenic,target);
            }
            if (unwavering > 0) {
                unWavering(unwavering,target);
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.HEAD),Senchantments.VORACIOUS_MAW)){
                if (Math.random() < 0.1f && living instanceof Player player){
                    player.playNotifySound(SoundEvents.GENERIC_EAT, SoundSource.AMBIENT,1,1);
                    player.getFoodData().eat(6,0);
                }
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.MAINHAND),Senchantments.CORROSIVE_POTENCY)){
                target.addEffect(new MobEffectInstance(Seffects.CORROSION,60,1));
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.MAINHAND),Senchantments.UNWAVERING_NATURE)){
                target.addEffect(new MobEffectInstance(Seffects.MYCELIUM,600,1));
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.MAINHAND),Senchantments.CRYOGENIC_ASPECT)){
                target.setTicksFrozen(target.getTicksFrozen()+300);
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.MAINHAND),Senchantments.GASTRIC_SPEWAGE)){
                for (MobEffectInstance instance : BileLiquid.bileEffects()){
                    target.addEffect(instance);
                }
            }
            if (Senchantments.hasEnchant(living.level(),living.getItemBySlot(EquipmentSlot.MAINHAND),Senchantments.MUTAGENIC_REACTANT)){
                if (Math.random() < 0.1 && target instanceof Infected infected && infected instanceof EvolvingInfected evolvedInfected){
                    if (evolvedInfected instanceof EvolvedInfected evolved){
                        evolved.setEvolution(SConfig.SERVER.evolution_age_human.get());
                        evolved.setEvoPoints(evolved.getEvoPoints()+SConfig.SERVER.min_kills_hyper.get());
                    }else{
                        infected.setEvolution(SConfig.SERVER.evolution_age_human.get());
                        infected.setEvoPoints(infected.getEvoPoints()+SConfig.SERVER.min_kills.get());
                    }
                    if(living instanceof Player player){
                        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel()/2);
                    }
                }
            }


        }
        /* --------------------------------------------------------
         *  HIVEMIND LOGIC
         * -------------------------------------------------------- */
        if (attacker instanceof Mob mob) {
            CompoundTag tag = mob.getPersistentData();

            if (tag.contains("hivemind")) {
                Level lvl = mob.level();
                Entity summoner = lvl.getEntity(tag.getInt("hivemind"));

                if (summoner instanceof Proto proto) {
                    proto.praisedForDecision(tag.getInt("decision"), tag.getInt("member"));
                }
            }
        }

        if (target instanceof Mob mob) {
            CompoundTag tag = mob.getPersistentData();

            if (tag.contains("hivemind")) {
                Level lvl = mob.level();
                Entity summoner = lvl.getEntity(tag.getInt("hivemind"));

                if (summoner instanceof Proto proto) {
                    proto.punishForDecision(tag.getInt("decision"), tag.getInt("member"));
                }
            }
        }

        /* --------------------------------------------------------
         *  FINAL APPLY (SAFE)
         * -------------------------------------------------------- */
        event.setNewDamage(modified);
    }


    public static void handleSpikes(int thornLevel,LivingEntity living){
        int duration = 40 + thornLevel * 40;
        living.hurt(living.damageSources().thorns(living), 3.5f * thornLevel);
        if (Math.random() < 0.5f) {
            living.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    duration,
                    0,
                    false,
                    true
            ));
        } else {
            living.addEffect(new MobEffectInstance(
                    Seffects.CORROSION,
                    duration,
                    0,
                    false,
                    true
            ));
        }
    }
    public static void mutagenicLevel(int mutagenic,LivingEntity living){
        if (Math.random() < 0.2){
            for (int i = 0;i<mutagenic;i++){
                MobEffectInstance effect = badMutations().get(living.getRandom().nextInt(badMutations().size()));
                living.addEffect(effect);
            }
        }
    }
    public static void unWavering(int nature,LivingEntity living){
        for (int i = 0;i<nature;i++){
            if (!living.hasEffect(Seffects.MYCELIUM)){
                living.addEffect(new MobEffectInstance(Seffects.MYCELIUM,600,0));
            }
            if (Math.random() < 0.05){
                ScentEntity scent = new ScentEntity(Sentities.SCENT.get(),living.level());
                scent.moveTo(living.getX(),living.getY(),living.getZ());
                living.level().addFreshEntity(scent);
            }
        }
    }

    public static List<MobEffectInstance> badMutations(){
        List<MobEffectInstance> values = new ArrayList<>();
        values.add(new MobEffectInstance(MobEffects.WEAKNESS,160,0));
        values.add(new MobEffectInstance(MobEffects.POISON,80,0));
        values.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,120,0));
        values.add(new MobEffectInstance(MobEffects.CONFUSION,200,0));
        return values;
    }
}
