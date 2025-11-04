package com.Harbinger.Spore.Sitems;

import net.minecraft.resources.ResourceLocation;

public class LivingChestplate extends LivingExoskeleton implements CustomModelArmorData {
    public LivingChestplate() {
        super(Type.CHESTPLATE);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}