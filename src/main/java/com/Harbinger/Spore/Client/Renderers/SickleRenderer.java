package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Models.SickleModel;
import com.Harbinger.Spore.Sentities.Projectile.ThrownSickle;
import com.Harbinger.Spore.Spore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class SickleRenderer extends EntityRenderer<ThrownSickle> {
    private static final ResourceLocation SICKLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/infected_sickle.png");
    private static final ResourceLocation SPINE_TEXTURE = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/spine.png");
    private final SickleModel<ThrownSickle> model;

    public SickleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SickleModel<>(context.bakeLayer(SickleModel.LAYER_LOCATION));
    }

    @Override
    public void render(ThrownSickle sickle, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        int color = sickle.getColor();
        float r = (float) (color >> 16 & 255) / 255.0F;;
        float g = (float) (color >> 8 & 255) / 255.0F;;
        float b = (float) (color & 255) / 255.0F;;
        poseStack.pushPose();
        poseStack.translate(0,-1,0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, sickle.yRotO, sickle.getYRot())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, sickle.xRotO, sickle.getXRot())));
        poseStack.scale(1.8f,1.8f,1.8f);
        VertexConsumer sickleConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, model.renderType(getTextureLocation(sickle)), false, sickle.isFoil());
        model.renderToBuffer(poseStack, sickleConsumer, light, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
        Entity owner = sickle.getOwner();
        if (owner != null) {
            renderConnection(sickle,owner,poseStack,bufferSource,partialTicks,color);
        }

        super.render(sickle, entityYaw, partialTicks, poseStack, bufferSource, light);
    }



    @Override
    public ResourceLocation getTextureLocation(ThrownSickle sickle) {
        return SICKLE_TEXTURE;
    }

    private void renderConnection(ThrownSickle parent , Entity to, PoseStack stack,
                                      MultiBufferSource buffer, float partialTick,int color) {
        Vec3 start = parent.getPosition(partialTick).add(parent.getDeltaMovement().normalize().scale(-0.3));
        Vec3 vec3 = (new Vec3(0.2, 1.35, 0.6)).yRot(-to.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
        Vec3 end = to.getPosition(partialTick).add(vec3.x,vec3.y,vec3.z);

        Vec3 direction = end.subtract(start);
        float length = (float) direction.length();
        length = Math.max(length, 1.5f);
        direction = direction.normalize();

        float yaw = (float)Math.atan2(direction.x, direction.z);
        float pitch = (float)-Math.asin(direction.y);
        stack.pushPose();
        {
            stack.mulPose(Axis.YP.rotation(yaw));
            stack.mulPose(Axis.XP.rotation(pitch));

            float startWidth = 0.5f;
            float startHeight = 0.5f;
            float endWidth = 0.5f;
            float endHeight = 0.5f;

            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer,RenderType.entityTranslucent(SPINE_TEXTURE),false,parent.isFoil());
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

    private void drawTaperedConnection(VertexConsumer vertexConsumer, Matrix4f matrix, PoseStack.Pose normal,
                                       float startWidth, float startHeight,
                                       float endWidth, float endHeight,
                                       float length,
                                       int overlay, int lightmap,
                                       int color) {
        // Half dimensions for start and end
        float hwStart = startWidth / 2f;
        float hhStart = startHeight / 2f;
        float hwEnd = endWidth / 2f;
        float hhEnd = endHeight / 2f;
        // Left side
        vertexConsumer.addVertex(matrix, -hwStart, -hhStart, 0)
                .setColor(color).setUv(0, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(normal, -1, 0, 0);
        vertexConsumer.addVertex(matrix, -hwStart, hhStart, 0)
                .setColor(color).setUv(1, 0)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(normal, -1, 0, 0);
        vertexConsumer.addVertex(matrix, -hwEnd, hhEnd, length)
                .setColor(color).setUv(1, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(normal, -1, 0, 0);
        vertexConsumer.addVertex(matrix, -hwEnd, -hhEnd, length)
                .setColor(color).setUv(0, 1)
                .setOverlay(overlay).setLight(lightmap)
                .setNormal(normal, -1, 0, 0);
    }
}
