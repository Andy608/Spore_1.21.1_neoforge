
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InfectedExoskeleton extends SporeBaseArmor implements CustomModelArmorData{
    private static final ResourceLocation location = ResourceLocation.parse("spore:textures/armor/living_armor_set.png");
    private final Holder<MobEffect> effectHolder;
    public InfectedExoskeleton(Type type) {
        super(type, new int[]{
                SConfig.SERVER.boots_durability.get(), SConfig.SERVER.pants_durability.get(), SConfig.SERVER.chestplate_durability.get(), SConfig.SERVER.helmet_durability.get()},new int[]{
                SConfig.SERVER.boots_protection.get(), SConfig.SERVER.pants_protection.get(), SConfig.SERVER.chestplate_protection.get(), SConfig.SERVER.helmet_protection.get()
        }, SConfig.SERVER.armor_toughness.get(),SConfig.SERVER.knockback_resistance.get());
        effectHolder = Seffects.SYMBIOSIS;
    }


    @Override
    public void tickArmor(Player player, Level level) {
        super.tickArmor(player, level);
        this.geteffect(player);
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
    private int getEffectMod(LivingEntity living){
        int i = 0;
        ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = living.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = living.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = living.getItemBySlot(EquipmentSlot.FEET);
        if (helmet.equals(ItemStack.EMPTY) || chest.equals(ItemStack.EMPTY) || legs.equals(ItemStack.EMPTY) || feet.equals(ItemStack.EMPTY)){return -1;}
        if (helmet.getItem().equals(Sitems.INF_UP_HELMET.get())){i=i+2;}
        if (chest.getItem().equals(Sitems.INF_UP_CHESTPLATE.get())){i=i+2;}
        if (legs.getItem().equals(Sitems.INF_UP_PANTS.get())){i=i+2;}
        if (feet.getItem().equals(Sitems.INF_UP_BOOTS.get())){i=i+2;}
        if (helmet.getItem().equals(Sitems.INF_HELMET.get())){i++;}
        if (chest.getItem().equals(Sitems.INF_CHEST.get())){i++;}
        if (legs.getItem().equals(Sitems.INF_PANTS.get())){i++;}
        if (feet.getItem().equals(Sitems.INF_BOOTS.get())){i++;}
        return  i < 4 ? -1 : i > 7 ? 1 : 0;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return location;
    }
}
