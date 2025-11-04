package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LivingExoskeleton extends SporeBaseArmor {
    protected final ResourceLocation TEXTURE = ResourceLocation.parse("spore:textures/armor/flesh_armor_set.png");
    private final Holder<MobEffect> effectHolder;
    public LivingExoskeleton(Type slot) {
        super(slot, new int[]{
                SConfig.SERVER.boots_durability2.get(), SConfig.SERVER.pants_durability2.get(), SConfig.SERVER.chestplate_durability2.get(), SConfig.SERVER.helmet_durability2.get()},new int[]{
                SConfig.SERVER.boots_protection2.get(), SConfig.SERVER.pants_protection2.get(), SConfig.SERVER.chestplate_protection2.get(), SConfig.SERVER.helmet_protection2.get()
        }, SConfig.SERVER.armor_toughness2.get(),SConfig.SERVER.knockback_resistance2.get());
        effectHolder = Seffects.SYMBIOSIS;
    }

    @Override
    public void tickArmor(Player living, Level level) {
        geteffect(living);
    }
    public void geteffect(LivingEntity entity) {
        MobEffectInstance instance = entity.getEffect(effectHolder);
        if (entity.tickCount % 20 == 0){
            int val = getEffectMod(entity);
            if (instance != null && instance.getDuration() < 60){
                if (val != -1){
                    entity.addEffect(new MobEffectInstance(effectHolder, 200, val, (false), (false)));
                }
            }
            if (instance == null){
                if (val != -1){
                    entity.addEffect(new MobEffectInstance(effectHolder, 200, val, (false), (false)));
                }
            }
        }
    }
    public int  getEffectMod(LivingEntity entity) {
        if ((entity.getItemBySlot(EquipmentSlot.FEET).getItem() == Sitems.LIVING_BOOTS.get())
                && (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == Sitems.LIVING_PANTS.get())
                && (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == Sitems.LIVING_CHEST.get())
                && (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Sitems.LIVING_HELMET.get())) {
            return 0;
        }
        return -1;
    }
}
