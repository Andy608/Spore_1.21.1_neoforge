package com.Harbinger.Spore.Client.Layers;

import com.Harbinger.Spore.Client.Models.NuckelaveArmorModel;
import com.Harbinger.Spore.Client.Models.NuckelaveModel;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Nuclealave;
import com.Harbinger.Spore.Spore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.ArrayList;
import java.util.List;

public class NucleaChestplateLayer<T extends Nuclealave> extends RenderLayer<T, NuckelaveModel<T>> {
    private static final ResourceLocation EMPTY = ResourceLocation.parse("spore:textures/entity/empty.png");
    private final TextureAtlas armorTrimAtlas;
    public final NuckelaveArmorModel<T> Nucklemodel;
    public final List<ModelPart> helmetModels = new ArrayList<>();
    public final List<ModelPart> chestModels = new ArrayList<>();
    public final List<ModelPart> pantsModels = new ArrayList<>();
    public final List<ModelPart> bootsModels = new ArrayList<>();
    private static final ResourceLocation BLOOD_LAYER1 =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/overlay/blood_overlay.png");
    private static final ResourceLocation BLOOD_LAYER2 =  ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/overlay/blood_overlay_2.png");
    public NucleaChestplateLayer(RenderLayerParent<T, NuckelaveModel<T>> p_117346_, EntityModelSet set, ModelManager manager) {
        super(p_117346_);
        this.Nucklemodel = new NuckelaveArmorModel<>(set.bakeLayer(NuckelaveArmorModel.LAYER_LOCATION));
        armorTrimAtlas = manager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        this.helmetModels.add(this.getParentModel().HeadWear);
        this.chestModels.add(this.getParentModel().LeftArmWear);
        this.chestModels.add(this.getParentModel().RightArmWear);
        this.pantsModels.add(this.getParentModel().BackRightLegWear);
        this.pantsModels.add(this.getParentModel().FrontRightLegWear);
        this.bootsModels.add(this.getParentModel().BackLeftFootWear);
        this.bootsModels.add(this.getParentModel().FrontRightFootWear);
    }


    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int value, T type, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        renderToBufferPerArmorPiece(type,stack,bufferSource,value);
        renderArmorBuffer(type,stack,bufferSource,value);
    }
    public void renderToBufferPerArmorPiece(T entity , PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        renderArmorPart(entity,EquipmentSlot.HEAD,helmetModels,poseStack,bufferSource, packedLight);
        renderArmorPart(entity,EquipmentSlot.CHEST,chestModels,poseStack,bufferSource, packedLight);
        renderArmorPart(entity,EquipmentSlot.LEGS,pantsModels,poseStack,bufferSource, packedLight);
        renderArmorPart(entity,EquipmentSlot.FEET,bootsModels,poseStack,bufferSource, packedLight);
    }
    private void renderArmorPart(T entity, EquipmentSlot slot , List<ModelPart> parts, PoseStack stack, MultiBufferSource bufferSource, int packedLight){
        ItemStack itemStack = entity.getItemBySlot(slot);
        boolean flag = itemStack.hasFoil();
        if (itemStack.getItem() instanceof ArmorItem armorItem){
            ArmorMaterial armormaterial = armorItem.getMaterial().value();
            renderArmor(parts,slot,stack,bufferSource,packedLight,OverlayTexture.NO_OVERLAY,-1,this.getArmorResource(entity, itemStack,armormaterial, slot),flag);
            ArmorTrim armortrim = (ArmorTrim)itemStack.get(DataComponents.TRIM);
            if (armortrim != null) {
                this.renderTrim(armorItem.getMaterial(), stack, bufferSource, packedLight, armortrim, parts);
            }
        }
    }
    private void renderArmorBuffer(T entity , PoseStack stack, MultiBufferSource bufferSource, int packedLight){
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        boolean flag = itemStack.hasFoil();
        if (itemStack.getItem() instanceof ArmorItem armorItem){
            ArmorMaterial armormaterial = armorItem.getMaterial().value();
            renderChestplate(stack,bufferSource,packedLight,OverlayTexture.NO_OVERLAY,-1,this.getArmorResource(entity, itemStack,armormaterial, EquipmentSlot.CHEST),flag);
            ArmorTrim armortrim = (ArmorTrim)itemStack.get(DataComponents.TRIM);
            if (armortrim != null) {
                this.renderTrim(armorItem.getMaterial(), stack, bufferSource, packedLight, armortrim, List.of(Nucklemodel.ChestPlate));
            }
        }
    }
    private void renderChestplate(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int alpha ,ResourceLocation location,boolean glint){
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(location));
        Nucklemodel.ChestPlate.render(poseStack, consumer, packedLight, packedOverlay, alpha);
        if (glint){
            Nucklemodel.ChestPlate.render(poseStack, bufferSource.getBuffer(RenderType.entityGlint()), packedLight, packedOverlay, alpha);
        }
        renderBloodLayer(Nucklemodel.ChestPlate,EquipmentSlot.CHEST,poseStack,bufferSource,packedLight);
    }
    private void setInvisible(ModelPart part,List<ModelPart> parts){
        part.skipDraw = !parts.contains(part);
    }

    private void renderArmor(List<ModelPart> parts,EquipmentSlot slot, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int alpha,ResourceLocation location,boolean glint){
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(location));
        this.getParentModel().Nuckelavee.getAllParts().forEach(modelPart -> {setInvisible(modelPart,parts);});
        this.getParentModel().Nuckelavee.render(stack, consumer, packedLight, packedOverlay,  alpha);
        if (glint){
            this.getParentModel().Nuckelavee.render(stack, bufferSource.getBuffer(RenderType.entityGlint()), packedLight, packedOverlay,  alpha);
        }
        renderBloodLayer(this.getParentModel().Nuckelavee,slot,stack,bufferSource,packedLight);
    }

    private void renderTrim(Holder<ArmorMaterial> armorMaterialHolder, PoseStack stack, MultiBufferSource source, int light, ArmorTrim armorTrim, List<ModelPart> parts) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(armorTrim.outerTexture(armorMaterialHolder));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(armorTrim.pattern().value().decal())));
        this.getParentModel().Nuckelavee.getAllParts().forEach(modelPart -> {setInvisible(modelPart,parts);});
        this.getParentModel().Nuckelavee.render(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
    }
    public ResourceLocation getArmorResource(Entity entity, ItemStack stack,ArmorMaterial material, EquipmentSlot slot) {
        if (material.layers().isEmpty()){
            return EMPTY;
        }
        return  ClientHooks.getArmorTexture(entity, stack, material.layers().getFirst(),false, slot);
    }
    private void renderBloodLayer(ModelPart part,EquipmentSlot slot, PoseStack stack, MultiBufferSource bufferSource, int packedLight){
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(slot == EquipmentSlot.LEGS ? BLOOD_LAYER2 : BLOOD_LAYER1));
        part.render(stack,consumer,packedLight,OverlayTexture.NO_OVERLAY);
    }
}
