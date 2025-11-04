package com.Harbinger.Spore.Sitems.BaseWeapons;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class SporePickaxeItems extends SporeDiggerTools{
    public SporePickaxeItems(double meleeDamage, double meleeReach, double meleeRecharge, int durability, int miningLevel) {
        super(meleeDamage, meleeReach, meleeRecharge, durability, miningLevel, BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }
}
