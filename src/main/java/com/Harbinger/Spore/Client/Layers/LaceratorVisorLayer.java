package com.Harbinger.Spore.Client.Layers;

import com.Harbinger.Spore.Sentities.Experiments.Lacerator;
import com.Harbinger.Spore.Spore;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LaceratorVisorLayer<T extends Lacerator> extends TranslucentLayer<T> {
    private static final ResourceLocation VISOR =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/lacerator_visor.png");

    public LaceratorVisorLayer(RenderLayerParent<T, EntityModel<T>> p_117346_) {
        super(p_117346_);
    }

    @Override
    public ResourceLocation getTexture(T type) {
        return VISOR;
    }
}
