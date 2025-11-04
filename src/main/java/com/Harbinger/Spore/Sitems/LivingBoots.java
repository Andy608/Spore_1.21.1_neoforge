package com.Harbinger.Spore.Sitems;

import net.minecraft.resources.ResourceLocation;

public class LivingBoots extends LivingExoskeleton implements CustomModelArmorData{
    public LivingBoots() {
        super(Type.BOOTS);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
