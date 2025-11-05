package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Layers.SporeRenderTypes;
import com.Harbinger.Spore.Client.Models.*;
import com.Harbinger.Spore.Sentities.BaseEntities.HohlMultipart;
import com.Harbinger.Spore.Sentities.Calamities.Hohlfresser;
import com.Harbinger.Spore.Spore;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class HohlSegRenderer<Type extends HohlMultipart> extends LivingEntityRenderer<Type , EntityModel<Type>> {
    public static final Map<HohlMultipart.SegmentVariants, ResourceLocation> TEXTURE =
            Util.make(Maps.newEnumMap(HohlMultipart.SegmentVariants.class), (p_114874_) -> {
                p_114874_.put(HohlMultipart.SegmentVariants.DEFAULT,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/hohl/hohl_seg1.png"));
                p_114874_.put(HohlMultipart.SegmentVariants.MELEE,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/hohl/hohl_seg2.png"));
                p_114874_.put(HohlMultipart.SegmentVariants.ORGAN,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/hohl/hohl_seg3.png"));
            });
    private static final ResourceLocation INNARDS =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/worm_innards.png");
    private static final ResourceLocation TAIL =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/hohl/hohl_seg1.png");

    private final EntityModel<Type> mainSegment = this.getModel();
    private final EntityModel<Type> meleeSegment;
    private final EntityModel<Type> organSegment;
    private final EntityModel<Type> tailModel;

    public HohlSegRenderer(EntityRendererProvider.Context context) {
        super(context, new HohlfresserSeg1Model<>(context.bakeLayer(HohlfresserSeg1Model.LAYER_LOCATION)), 4f);
        meleeSegment = new HohlfresserSeg2Model<>(context.bakeLayer(HohlfresserSeg2Model.LAYER_LOCATION));
        organSegment = new HohlfresserSeg3Model<>(context.bakeLayer(HohlfresserSeg3Model.LAYER_LOCATION));
        tailModel = new hohlfresserTailModel<>(context.bakeLayer(hohlfresserTailModel.LAYER_LOCATION));
        this.addLayer(new HohlColors<>(this));
        this.addLayer(new HohlEmmisive<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return entity.isTail() ? TAIL :  TEXTURE.get(entity.getSegmentVariant());
    }

    @Override
    protected void scale(Type type, PoseStack stack, float p_115316_) {
        float size = type.getSize();
        stack.scale(size,size,size);
        super.scale(type, stack, p_115316_);
    }
    public EntityModel<Type> getSegmentModel(Type type){
        switch (type.getSegmentVariant()){
            case MELEE -> {
                return meleeSegment;
            }
            case ORGAN -> {
                return organSegment;
            }
        }
        return mainSegment;
    }

    @Override
    public void render(Type type, float val1, float val2, PoseStack stack, MultiBufferSource source, int light) {
        model = type.isTail() ? tailModel : getSegmentModel(type);
        super.render(type, val1, val2, stack, source, light);
        if (type.isInvisible()){
            return;
        }
        ClientLevel level = Minecraft.getInstance().level;
        int i = type.getParentIntId();
        if (level != null && i != -1){
            Entity parent = level.getEntity(i);
            if (parent != null){
                renderConnection(type,parent,stack,source,val2);
            }
        }
    }

    @Override
    protected boolean shouldShowName(Type p_115333_) {
        return false;
    }

    public class HohlColors<T extends HohlMultipart, M extends EntityModel<T>> extends RenderLayer<T, M> {
        public HohlColors(RenderLayerParent<T, M> p_117346_) {
            super(p_117346_);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int val, T type, float v, float v1, float v2, float v3, float v4, float v5) {
            if (!type.isInvisible()){
                if (type.getColor() == 0){return;}
                int i = type.getColor();
                int r = (i >> 16) & 0xFF;
                int g = (i >> 8) & 0xFF;
                int b = i & 0xFF;
                int halfColor = (0x80 << 24) | (r << 16) | (g << 8) | b;
                VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(type)));
                getParentModel().renderToBuffer(poseStack,consumer,val, OverlayTexture.NO_OVERLAY,halfColor);
            }
        }
    }
    private void renderConnection(Type parent, Entity to, PoseStack stack,
                                  MultiBufferSource buffer, float partialTick) {
        float i = to instanceof HohlMultipart hohlMultipart ? hohlMultipart.getSize() : to instanceof Hohlfresser ? 1.2f : 0f;
        Vec3 start = parent.getPosition(partialTick).add(0, (parent.getBbHeight() * 0.3f * parent.getSize()),0);
        Vec3 end = to.getPosition(partialTick).add(0, (to.getBbHeight() * 0.45f * i),0);

        Vec3 direction = end.subtract(start);
        float length = (float)direction.length();
        direction = direction.normalize();

        float yaw = (float)Math.atan2(direction.x, direction.z);
        float pitch = (float)-Math.asin(direction.y);
        int color = parent.getColor() == 0 ? -1 : parent.getColor();
        stack.pushPose();
        {
            Vec3 vec3 = parent.position().subtract(parent.position()).scale(-1);
            stack.translate(vec3.x, vec3.y+1, vec3.z);
            stack.mulPose(Axis.YP.rotation(yaw));
            stack.mulPose(Axis.XP.rotation(pitch));
            float inf = 0.6f;
            float startWidth = parent.getBbWidth()*inf * parent.getSize();
            float startHeight = parent.getBbHeight()*inf * parent.getSize();
            float endWidth = to.getBbWidth()*inf * i;
            float endHeight = to.getBbHeight()*inf * i;

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(INNARDS));
            PoseStack.Pose pose = stack.last();
            Matrix4f matrix = pose.pose();
            drawTaperedConnection(vertexConsumer, matrix, pose,
                    startWidth, startHeight,  // Start dimensions
                    endWidth, endHeight,      // End dimensions
                    length,                   // Distance between segments
                    OverlayTexture.NO_OVERLAY, 15728880,
                    color);              // RGBA color
        }
        stack.popPose();
    }

    private void drawTaperedConnection(VertexConsumer consumer, Matrix4f pose, PoseStack.Pose realPose,
                                       float startWidth, float startHeight,
                                       float endWidth, float endHeight,
                                       float length,
                                       int overlay, int lightmap,
                                       int packedColor) {

        // Half dimensions
        float hwStart = startWidth / 2f;
        float hhStart = startHeight / 2f;
        float hwEnd   = endWidth / 2f;
        float hhEnd   = endHeight / 2f;

        // Front face
        consumer.addVertex(pose, -hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, -1);
        consumer.addVertex(pose, hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, -1);
        consumer.addVertex(pose, hwStart, hhStart, 0)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, 0, -1);
        consumer.addVertex(pose, -hwStart, hhStart, 0)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, -1);

        // Back face
        consumer.addVertex(pose, -hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, 1);
        consumer.addVertex(pose, hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, 1);
        consumer.addVertex(pose, hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, 0, 1);
        consumer.addVertex(pose, -hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 0, 1);

        // Top side
        consumer.addVertex(pose, -hwStart, hhStart, 0)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, 1, 0);
        consumer.addVertex(pose, hwStart, hhStart, 0)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, 1, 0);
        consumer.addVertex(pose, hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, 1, 0);
        consumer.addVertex(pose, -hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, 1, 0);

        // Bottom side
        consumer.addVertex(pose, -hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,0, -1, 0);
        consumer.addVertex(pose, hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, -1, 0);
        consumer.addVertex(pose, hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, -1, 0);
        consumer.addVertex(pose, -hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 0, -1, 0);

        // Left side
        consumer.addVertex(pose, -hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,-1, 0, 0);
        consumer.addVertex(pose, -hwStart, hhStart, 0)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,-1, 0, 0);
        consumer.addVertex(pose, -hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,-1, 0, 0);
        consumer.addVertex(pose, -hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, -1, 0, 0);

        // Right side
        consumer.addVertex(pose, hwStart, -hhStart, 0)
                .setColor(packedColor).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 1, 0, 0);
        consumer.addVertex(pose, hwStart, hhStart, 0)
                .setColor(packedColor).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(realPose, 1, 0, 0);
        consumer.addVertex(pose, hwEnd, hhEnd, length)
                .setColor(packedColor).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,1, 0, 0);
        consumer.addVertex(pose, hwEnd, -hhEnd, length)
                .setColor(packedColor).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal( realPose,1, 0, 0);

    }


        static class HohlEmmisive <T extends HohlMultipart,M extends EntityModel<T>> extends RenderLayer<T, M> {
        private static final ResourceLocation TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/hohl/hohl_bile.png");
        public HohlEmmisive(RenderLayerParent<T, M> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!entity.isInvisible() && entity.getSegmentVariant() == HohlMultipart.SegmentVariants.ORGAN && !entity.isTail()){
                float alpha = 0.5F + 0.5F * Mth.sin(ageInTicks * 0.1F);
                int color = (int)(alpha * 255) << 24 | 0xFFFFFF;
                VertexConsumer vertexConsumer = buffer.getBuffer(SporeRenderTypes.glowingTranslucent(TEXTURE));
                getParentModel().renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
            }
        }
    }
}
