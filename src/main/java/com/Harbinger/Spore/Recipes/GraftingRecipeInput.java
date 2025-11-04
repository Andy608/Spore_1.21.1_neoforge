package com.Harbinger.Spore.Recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record GraftingRecipeInput(ItemStack[] stack) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return stack[i];
    }

    @Override
    public int size() {
        return 24;
    }
    @Override
    public boolean isEmpty() {
        for (ItemStack item : stack) {
            if (item != null && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
