package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Models.KnightModel;
import com.Harbinger.Spore.Client.Special.BaseInfectedRenderer;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Knight;
import com.Harbinger.Spore.Spore;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class KnightRenderer <Type extends Knight> extends BaseInfectedRenderer<Type , KnightModel<Type>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/knight.png");
    private static final ResourceLocation EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/knight.png");
    public KnightRenderer(EntityRendererProvider.Context context) {
        super(context, new KnightModel<>(context.bakeLayer(KnightModel.LAYER_LOCATION)), 0.5f);
    }
    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation eyeLayerTexture() {
        return EYES_TEXTURE;
    }
}