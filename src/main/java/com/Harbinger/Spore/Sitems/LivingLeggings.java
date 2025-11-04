package com.Harbinger.Spore.Sitems;

import net.minecraft.resources.ResourceLocation;

public class LivingLeggings extends LivingExoskeleton implements CustomModelArmorData{
    public LivingLeggings() {
        super(Type.LEGGINGS);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
