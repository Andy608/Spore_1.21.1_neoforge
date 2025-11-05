package com.Harbinger.Spore.Client.Renderers;


import com.Harbinger.Spore.Client.Models.MoundModel;
import com.Harbinger.Spore.Client.Special.OrganoidMobRenderer;
import com.Harbinger.Spore.Sentities.Organoids.Mound;
import com.Harbinger.Spore.Spore;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoundRenderer<Type extends Mound> extends OrganoidMobRenderer<Type , MoundModel<Type>> {
    private static final ResourceLocation TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/mound.png");
    private static final ResourceLocation TEXTURE_LARGE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/mound_large.png");
    private static final ResourceLocation TEXTURE_LINKED =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/linked_mounds.png");
    private static final ResourceLocation TEXTURE_LARGE_LINKED =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/mound_large_linked.png");
    public MoundRenderer(EntityRendererProvider.Context context) {
        super(context, new MoundModel<>(context.bakeLayer(MoundModel.LAYER_LOCATION)), 0.5f);
    }
    @Override
    protected void scale(Mound mound, PoseStack poseStack, float p_115316_) {
        int scale = Math.max(mound.getAge(), 1);
        poseStack.scale((float) scale,(float)scale,(float)scale);
    }

    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        if (entity.getAge() >= 3){
            return entity.getLinked() ? TEXTURE_LARGE_LINKED:TEXTURE_LARGE;
        }else {
            return entity.getLinked() ? TEXTURE_LINKED:TEXTURE;
        }
    }
}
