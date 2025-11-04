package com.Harbinger.Spore.Sitems.BaseWeapons;

import com.Harbinger.Spore.core.SdataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SporeArmorData {

    default boolean tooHurt(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 10;
    }

    default double calculateTrueDefense(ItemStack stack, double defense) {
        double value = getAdditionalProtection(stack) * 0.01;
        if (value > 0) {
            return defense + (defense * value);
        }
        return defense;
    }

    default void setAdditionalProtection(double value, ItemStack stack) {
        stack.set(SdataComponents.ADDITIONAL_PROTECTION.get(), (float) value);
    }

    default double getAdditionalProtection(ItemStack itemStack) {
        return itemStack.getOrDefault(SdataComponents.ADDITIONAL_PROTECTION.get(), 0.0f);
    }

    default double calculateTrueToughness(ItemStack stack, double defense) {
        double value = getAdditionalToughness(stack) * 0.01;
        if (value > 0) {
            return defense + (defense * value);
        }
        return defense;
    }

    default void setAdditionalToughness(double value, ItemStack stack) {
        stack.set(SdataComponents.ADDITIONAL_TOUGHNESS.get(), (float) value);
    }

    default double getAdditionalToughness(ItemStack itemStack) {
        return itemStack.getOrDefault(SdataComponents.ADDITIONAL_TOUGHNESS.get(), 0.0f);
    }

    default void setLuck(int value, ItemStack stack) {
        stack.set(SdataComponents.ENCHANTING_LUCK.get(), value);
    }

    default int getLuck(ItemStack itemStack) {
        return itemStack.getOrDefault(SdataComponents.ENCHANTING_LUCK.get(), 0);
    }

    default SporeArmorMutations getVariant(ItemStack stack) {
        return SporeArmorMutations.byId(this.getTypeVariant(stack) & 255);
    }

    default int getTypeVariant(ItemStack stack) {
        return stack.getOrDefault(SdataComponents.ARMOR_MUTATION.get(), 0);
    }

    default void setVariant(SporeArmorMutations variant, ItemStack stack) {
        stack.set(SdataComponents.ARMOR_MUTATION.get(), variant.getId() & 255);
    }

    default void healTool(ItemStack stack, int value) {
        if (stack.getDamageValue() < stack.getMaxDamage()) {
            stack.setDamageValue(stack.getDamageValue() - value);
        }
        if (getMaxTrueAdditionalDurability(stack) > getAdditionalDurability(stack)) {
            setAdditionalDurability(getAdditionalDurability(stack) + value, stack);
        }
    }

    default int getMaxTrueAdditionalDurability(ItemStack stack) {
        return (int) (stack.getMaxDamage() * (getMaxAdditionalDurability(stack) * 0.01));
    }

    default int getMaxAdditionalDurability(ItemStack stack) {
        return stack.getOrDefault(SdataComponents.MAX_ADDITIONAL_DURABILITY.get(), 0);
    }

    default void setMaxAdditionalDurability(int value, ItemStack stack) {
        stack.set(SdataComponents.MAX_ADDITIONAL_DURABILITY.get(), value);
    }

    default int getAdditionalDurability(ItemStack stack) {
        return stack.getOrDefault(SdataComponents.EXTRA_DURABILITY.get(), 0);
    }

    default void setAdditionalDurability(int value, ItemStack stack) {
        stack.set(SdataComponents.EXTRA_DURABILITY.get(), value);
    }

    default void hurtExtraDurability(ItemStack stack, int value, @Nullable LivingEntity living) {
        setAdditionalDurability(getAdditionalDurability(stack) - value, stack);
    }
}