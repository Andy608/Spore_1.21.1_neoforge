package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Models.ProtectorModel;
import com.Harbinger.Spore.Client.Special.BaseInfectedRenderer;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Protector;
import com.Harbinger.Spore.Spore;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ProtectorRenderer<Type extends Protector> extends BaseInfectedRenderer<Type , ProtectorModel<Type>> {
    private static final ResourceLocation EMPTY = ResourceLocation.parse("spore:textures/entity/empty.png");
    private static final ResourceLocation TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/protector.png");
    private static final ResourceLocation EYES_TEXTURE =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/protector.png");
    public ProtectorRenderer(EntityRendererProvider.Context context) {
        super(context, new ProtectorModel<>(context.bakeLayer(ProtectorModel.LAYER_LOCATION),false), 0.5f);
        this.addLayer(new ProtectorArmorRenderer<>(this,context.getModelManager()));
        this.addLayer(new PearlsLayer<>(this,context.getItemInHandRenderer()));
    }
    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation eyeLayerTexture() {
        return EYES_TEXTURE;
    }

    private static class PearlsLayer <T extends Protector, M extends ProtectorModel<T>> extends RenderLayer<T, M>{
        private final ItemInHandRenderer itemInHandRenderer;
        private final ProtectorModel<T> model;
        public PearlsLayer(RenderLayerParent<T, M> parent, ItemInHandRenderer itemInHandRenderer) {
            super(parent);
            this.itemInHandRenderer = itemInHandRenderer;
            this.model = this.getParentModel();
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T t, float v, float v1, float v2, float v3, float v4, float v5) {
            if (t.getPearls() > 0){
                ItemStack stack = new ItemStack(Items.ENDER_PEARL);
                poseStack.pushPose();
                this.model.LeftArm.translateAndRotate(poseStack);
                poseStack.translate(0,0.75,0);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                itemInHandRenderer.renderItem(t,stack, ItemDisplayContext.FIXED,true,poseStack,multiBufferSource,i);
                poseStack.popPose();
            }
        }
    }
    private static class ProtectorArmorRenderer <T extends Protector> extends RenderLayer<T, ProtectorModel<T>> {
        public final List<ModelPart> helmetModels = new ArrayList<>();
        public final List<ModelPart> bootsModels = new ArrayList<>();
        private final TextureAtlas armorTrimAtlas;
        private static final ResourceLocation BLOOD_LAYER1 = ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/overlay/blood_overlay.png");
        public ProtectorArmorRenderer(RenderLayerParent<T, ProtectorModel<T>> modelRenderLayerParent, ModelManager manager) {
            super(modelRenderLayerParent);
            armorTrimAtlas = manager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
            this.helmetModels.add(this.getParentModel().headWear);
            this.bootsModels.add(this.getParentModel().RightBoot);
            this.bootsModels.add(this.getParentModel().LeftBoot);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T t, float v, float v1, float v2, float v3, float v4, float v5) {
            renderArmorPart(t,EquipmentSlot.HEAD, helmetModels,poseStack,multiBufferSource, i);
            renderArmorPart(t,EquipmentSlot.FEET, bootsModels,poseStack,multiBufferSource, i);
        }
        private void renderArmorPart(T entity, EquipmentSlot slot , List<ModelPart> parts, PoseStack stack, MultiBufferSource bufferSource, int packedLight){
            ItemStack itemStack = entity.getItemBySlot(slot);
            boolean flag = itemStack.hasFoil();
            if (itemStack.getItem() instanceof ArmorItem armorItem){
                ArmorMaterial armormaterial = armorItem.getMaterial().value();
                renderArmor(parts,stack,bufferSource,packedLight,OverlayTexture.NO_OVERLAY,-1,this.getArmorResource(entity, itemStack,armormaterial, slot),flag);
                ArmorTrim armortrim = (ArmorTrim)itemStack.get(DataComponents.TRIM);
                if (armortrim != null) {
                    this.renderTrim(armorItem.getMaterial(), stack, bufferSource, packedLight, armortrim, parts);
                }
            }
        }
        private void renderArmor(List<ModelPart> parts, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int alpha,ResourceLocation location,boolean glint){
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(location));
            this.getParentModel().Protector.getAllParts().forEach(modelPart -> {setInvisible(modelPart,parts);});
            this.getParentModel().Protector.render(stack, consumer, packedLight, packedOverlay,  alpha);
            if (glint){
                this.getParentModel().Protector.render(stack, bufferSource.getBuffer(RenderType.entityGlint()), packedLight, packedOverlay,  alpha);
            }
            renderBloodLayer(this.getParentModel().Protector,stack,bufferSource,packedLight);
        }

        private void setInvisible(ModelPart part,List<ModelPart> parts){
            part.skipDraw = !parts.contains(part);
        }


        private void renderTrim(Holder<ArmorMaterial> armorMaterialHolder, PoseStack stack, MultiBufferSource source, int light, ArmorTrim armorTrim, List<ModelPart> parts) {
            TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(armorTrim.outerTexture(armorMaterialHolder));
            VertexConsumer vertexconsumer = textureatlassprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(armorTrim.pattern().value().decal())));
            this.getParentModel().Protector.getAllParts().forEach(modelPart -> {setInvisible(modelPart,parts);});
            this.getParentModel().Protector.render(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
        }
        public ResourceLocation getArmorResource(Entity entity, ItemStack stack,ArmorMaterial material, EquipmentSlot slot) {
            if (material.layers().isEmpty()){
                return EMPTY;
            }
            return  ClientHooks.getArmorTexture(entity, stack, material.layers().getFirst(),false, slot);
        }
        private void renderBloodLayer(ModelPart part, PoseStack stack, MultiBufferSource bufferSource, int packedLight){
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(BLOOD_LAYER1));
            part.render(stack,consumer,packedLight,OverlayTexture.NO_OVERLAY);
        }
    }

}