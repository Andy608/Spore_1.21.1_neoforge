
package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PlatedExoskeleton extends SporeBaseArmor implements CustomModelArmorData{
    private static final ResourceLocation location = ResourceLocation.parse("spore:textures/armor/plated_armor_set.png");
    public PlatedExoskeleton(Type type) {
        super(type, new int[]{
                SConfig.SERVER.boots_durability1.get(), SConfig.SERVER.pants_durability1.get(), SConfig.SERVER.chestplate_durability1.get(), SConfig.SERVER.helmet_durability1.get()},new int[]{
                SConfig.SERVER.boots_protection1.get(), SConfig.SERVER.pants_protection1.get(), SConfig.SERVER.chestplate_protection1.get(), SConfig.SERVER.helmet_protection1.get()
        }, SConfig.SERVER.armor_toughness1.get(),SConfig.SERVER.knockback_resistance1.get());
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack itemStack) {
        return super.isValidRepairItem(stack, itemStack) || itemStack.getItem() == Sitems.ARMOR_FRAGMENT.get();
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return location;
    }
}
