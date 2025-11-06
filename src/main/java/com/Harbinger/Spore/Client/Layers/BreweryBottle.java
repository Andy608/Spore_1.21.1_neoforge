package com.Harbinger.Spore.Client.Layers;

import com.Harbinger.Spore.Sentities.Organoids.Brauerei;
import com.Harbinger.Spore.Spore;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BreweryBottle<T extends Brauerei> extends TranslucentLayer<T> {
    private static final ResourceLocation SYRINGE = ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/brewery_glass.png");

    public BreweryBottle(RenderLayerParent<T, EntityModel<T>> p_117346_) {
        super(p_117346_);
    }

    @Override
    public ResourceLocation getTexture(T type) {
        return SYRINGE;
    }
}
