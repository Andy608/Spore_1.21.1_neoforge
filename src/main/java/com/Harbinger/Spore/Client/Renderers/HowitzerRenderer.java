package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Layers.HowitzerEmissiveLayer;
import com.Harbinger.Spore.Client.Layers.SporeRenderTypes;
import com.Harbinger.Spore.Client.Models.HowitzerModel;
import com.Harbinger.Spore.Client.Special.CalamityRenderer;
import com.Harbinger.Spore.Sentities.Calamities.Howitzer;
import com.Harbinger.Spore.Spore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HowitzerRenderer<Type extends Howitzer> extends CalamityRenderer<Type , HowitzerModel<Type>> {
    private static final ResourceLocation TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/howitzer.png");
    private static final ResourceLocation RADIATION =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/nuclear_howitzer.png");
    private static final ResourceLocation EYE_TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/howitzer_eyes.png");


    public HowitzerRenderer(EntityRendererProvider.Context context) {
        super(context, new HowitzerModel<>(context.bakeLayer(HowitzerModel.LAYER_LOCATION)), 4f);
        this.addLayer(new HowitzerNeonGreenLayer<>(this));
        this.addLayer(new HowitzerEmissiveLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return entity.isRadioactive() ? RADIATION : TEXTURE;
    }

    @Override
    public ResourceLocation eyeLayerTexture() {
        return EYE_TEXTURE;
    }

    static class HowitzerNeonGreenLayer <T extends Howitzer,M extends HowitzerModel<T>> extends RenderLayer<T, M>{
        public HowitzerNeonGreenLayer(RenderLayerParent<T, M> p_117346_) {
            super(p_117346_);
        }
        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T t, float v, float v1, float v2, float ageInTicks, float v4, float v5) {
            int ticksBoom = t.getSelfDetonation();
            if (!t.isInvisible() && t.isRadioactive() && ticksBoom > 0) {
                // Smooth pulsation 0.7–1.0
                float pulse = 0.7F + 0.3F * Mth.sin(ageInTicks * 0.2F);
                float progress = Mth.clamp(1.0F - (ticksBoom / 30.0F), 0.0F, 1.0F);

                float alpha = 0.15F + 0.45F * (pulse * progress);
                float red   = 0.2F + 0.1F * pulse;
                float green = 0.9F + 0.1F * pulse;
                float blue  = 0.2F;

                // Convert to 0–255 and pack RGBA → int
                int r = (int)(red   * 255.0F);
                int g = (int)(green * 255.0F);
                int b = (int)(blue  * 255.0F);
                int a = (int)(alpha * 255.0F);
                int color = (a << 24) | (r << 16) | (g << 8) | b;
                VertexConsumer consumer = multiBufferSource.getBuffer(SporeRenderTypes.glowingTranslucent(RADIATION));

                getParentModel().renderToBuffer(poseStack, consumer, i, OverlayTexture.NO_OVERLAY, color);
            }
        }
    }
}
