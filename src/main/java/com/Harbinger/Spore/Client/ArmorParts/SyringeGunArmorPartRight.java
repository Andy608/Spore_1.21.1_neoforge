package com.Harbinger.Spore.Client.ArmorParts;

import com.Harbinger.Spore.Client.Models.SyringeGunModelArm;
import com.Harbinger.Spore.Sitems.CustomModelArmorData;
import com.Harbinger.Spore.Sitems.SyringeGun;
import com.Harbinger.Spore.core.Sitems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class SyringeGunArmorPartRight extends BaseArmorRenderingBit{
    private final SyringeGunModelArm<LivingEntity> parentModel;
    private static final ResourceLocation TEXTURE = ResourceLocation.parse("spore:textures/item/syringe_gun_juice.png");
    private static final ResourceLocation TEXTURET = ResourceLocation.parse("spore:textures/item/syringe_gun_syringe.png");
    public SyringeGunArmorPartRight(SyringeGunModelArm<LivingEntity> parentModel,Supplier<EntityModel<LivingEntity>> model, Supplier<ModelPart> part, float x, float y, float z, float expand) {
        super(EquipmentSlot.MAINHAND, Sitems.SYRINGE_GUN.get(), model, part, x, y, z, expand);
        this.parentModel = parentModel;
    }

    @Override
    protected ModelPart getPiece(HumanoidModel<LivingEntity> model) {
        return model.rightArm;
    }

    @Override
    protected VertexConsumer consumer(MultiBufferSource source, CustomModelArmorData data, HumanoidModel<LivingEntity> model, LivingEntity livingEntity) {
        return ItemRenderer.getFoilBufferDirect(source, RenderType.entityTranslucent(data.getTextureLocation()), false, stack(livingEntity).hasFoil());
    }

    @Override
    public void tickMovement(LivingEntity livingEntity, PoseStack poseStack, HumanoidModel<LivingEntity> model, int light, MultiBufferSource buffer) {
        super.tickMovement(livingEntity, poseStack, model, light, buffer);
        ItemStack stack = livingEntity.getMainHandItem();
        if (stack.getItem().equals(item) && stack.getItem() instanceof SyringeGun syringeGun){
            List<Integer> clipColors = syringeGun.getClip(stack);
            applyTransformEx(poseStack,getPiece(model),this.x,this.y,this.z,this.expand,this.Xspin,this.Yspin,this.Zspin,() -> {
                handleColorRendering(parentModel.syringe, clipColors.get(0), poseStack,buffer,light);
                handleColorRendering(parentModel.syringe2, clipColors.get(1), poseStack,buffer,light);
                handleColorRendering(parentModel.syringe3, clipColors.get(2), poseStack,buffer,light);
                handleColorRendering(parentModel.syringe4, clipColors.get(3), poseStack,buffer,light);
            });
        }
    }
    public void handleColorRendering(ModelPart syringe, int color, PoseStack stack, MultiBufferSource source, int light) {
        if (color == 0) {
            return;
        }

        stack.pushPose();
        parentModel.syringeGun.translateAndRotate(stack);
        parentModel.magazine.translateAndRotate(stack);

        if (color != -1) {
            VertexConsumer consumer = source.getBuffer(RenderType.entityCutout(TEXTURE));
            syringe.render(stack, consumer, light, OverlayTexture.NO_OVERLAY,color);
        }
        VertexConsumer consumerS = source.getBuffer(RenderType.entityTranslucent(TEXTURET));
        syringe.render(stack, consumerS, light, OverlayTexture.NO_OVERLAY);

        stack.popPose();
    }
}
