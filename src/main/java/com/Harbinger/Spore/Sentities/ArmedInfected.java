package com.Harbinger.Spore.Sentities;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.core.Senchantments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;

public interface ArmedInfected {
    default Holder<Enchantment> meleeEnchants(LivingEntity living){
        List<Holder<Enchantment>> values = new ArrayList<>();
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.SHARPNESS));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.FIRE_ASPECT));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.KNOCKBACK));
        return values.get(living.getRandom().nextInt(values.size()));
    }
    default Holder<Enchantment> bowEnchantments(LivingEntity living){
        List<Holder<Enchantment>> values = new ArrayList<>();
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.FLAME));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.POWER));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.PUNCH));
        return values.get(living.getRandom().nextInt(values.size()));
    }
    default Holder<Enchantment> crossbowEnchantments(LivingEntity living){
        List<Holder<Enchantment>> values = new ArrayList<>();
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.MULTISHOT));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.PIERCING));
        return values.get(living.getRandom().nextInt(values.size()));
    }
    default Holder<Enchantment> armorEnchantments(LivingEntity living){
        List<Holder<Enchantment>> values = new ArrayList<>();
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.PROTECTION));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.THORNS));
        values.add(Senchantments.getEnchantment(living.level(),Enchantments.UNBREAKING));
        return values.get(living.getRandom().nextInt(values.size()));
    }
    default int getRandomLevel(Enchantment enchantment,LivingEntity living){
        if (enchantment.getMaxLevel() == 1){
            return 1;
        }
        return living.getRandom().nextInt(1,enchantment.getMaxLevel());
    }
    default void EnchantBasedOnItem(ItemStack stack,LivingEntity living){
        Item item = stack.getItem();
        Holder<Enchantment> melee = meleeEnchants(living);
        Holder<Enchantment> bow = bowEnchantments(living);
        Holder<Enchantment> crossbow = crossbowEnchantments(living);
        if (item instanceof TieredItem){
            stack.enchant(melee,getRandomLevel(melee.value(),living));
        }
        if (item instanceof BowItem){
            stack.enchant(bow,getRandomLevel(bow.value(),living));
        }
        if (item instanceof CrossbowItem){
            stack.enchant(crossbow,getRandomLevel(crossbow.value(),living));
        }
    }


    default void enchantItems(LivingEntity living){
        Holder<Enchantment> armor = armorEnchantments(living);
        living.getArmorSlots().forEach(item -> item.enchant(armor,getRandomLevel(armor.value(),living)));
        EnchantBasedOnItem(living.getMainHandItem(),living);
        EnchantBasedOnItem(living.getOffhandItem(),living);
    }
}
