package com.Harbinger.Spore.Sitems;

import net.minecraft.resources.ResourceLocation;

public class LivingHelmet extends LivingExoskeleton  implements CustomModelArmorData{
    public LivingHelmet() {
        super(Type.HELMET);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
