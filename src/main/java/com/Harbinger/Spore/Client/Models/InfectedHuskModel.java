package com.Harbinger.Spore.Client.Models;// Made with Blockbench 4.4.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Harbinger.Spore.Sentities.BasicInfected.InfectedHusk;
import com.Harbinger.Spore.Spore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class InfectedHuskModel<T extends InfectedHusk> extends EntityModel<T> {

	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "infectedhuskmodel"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightForLeg;
	private final ModelPart leftForLeg;
	private final ModelPart tendril1;
	private final ModelPart tendril2;


	public InfectedHuskModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.rightArm = this.body.getChild("RightArm");
		this.leftArm = this.body.getChild("LeftArm");
		this.rightLeg = this.body.getChild("RightLeg");
		this.leftLeg = this.body.getChild("LeftLeg");
		this.rightForLeg = rightLeg.getChild("rightForLeg");
		this.leftForLeg = leftLeg.getChild("leftForLeg");
		this.tendril1 = this.body.getChild("tendril");
		this.tendril2 = this.body.getChild("tendril2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(22, 21).addBox(-4.0F, 8.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 2.0F, 2.0F));

		PartDefinition spine = body.addOrReplaceChild("spine", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -4.5F, -4.5F, 4.0F, 5.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition back = spine.addOrReplaceChild("back", CubeListBuilder.create().texOffs(10, 43).addBox(0.0F, -4.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.0F, 0.0F, -2.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition back2 = spine.addOrReplaceChild("back2", CubeListBuilder.create().texOffs(42, 10).addBox(-2.0F, -4.0F, -2.25F, 3.0F, 4.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -2.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition spine2 = body.addOrReplaceChild("spine2", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -5.5F, -4.5F, 8.0F, 6.0F, 5.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.25F, 0.2618F, 0.0F, 0.0F));

		PartDefinition back3 = spine2.addOrReplaceChild("back3", CubeListBuilder.create().texOffs(38, 29).addBox(0.0F, -4.0F, -1.75F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, -2.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition back4 = spine2.addOrReplaceChild("back4", CubeListBuilder.create().texOffs(32, 6).addBox(-2.0F, -4.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, -2.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition tendril = body.addOrReplaceChild("tendril", CubeListBuilder.create().texOffs(48, 48).addBox(0.0F, -5.0F, -2.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 4.0F, 2.0F));

		PartDefinition tendril2 = body.addOrReplaceChild("tendril2", CubeListBuilder.create().texOffs(48, 48).addBox(0.0F, -5.0F, -2.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 2.0F, 1.0F));

		PartDefinition LeftLeg = body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 35).addBox(-1.25F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 10.0F, 0.0F));

		PartDefinition leftForLeg = LeftLeg.addOrReplaceChild("leftForLeg", CubeListBuilder.create().texOffs(14, 33).addBox(-1.25F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition RightLeg = body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(38, 39).addBox(-1.75F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 10.0F, 0.0F));

		PartDefinition rightForLeg = RightLeg.addOrReplaceChild("rightForLeg", CubeListBuilder.create().texOffs(28, 33).addBox(-1.75F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition LeftArm = body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 48).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 2.0F, -2.0F));

		PartDefinition LeftForArm = LeftArm.addOrReplaceChild("LeftForArm", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition cube_r1 = LeftForArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 43).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition RightArm = body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 2.0F, -2.0F));

		PartDefinition RightForArm = RightArm.addOrReplaceChild("RightForArm", CubeListBuilder.create().texOffs(46, 18).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(21, 14).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(18, 27).addBox(-3.5F, 0.0F, -3.75F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.xRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		float walkCycle = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
		float idleSway = Mth.sin(ageInTicks / 15.0F) / 10.0F;
		float idleSwaySlow = Mth.sin(ageInTicks / 25.0F) / 20.0F;

		if (entity.isAggressive()) {
			this.jaw.xRot = 0.4F + Mth.sin(ageInTicks / 6.0F) / 10.0F;
			this.body.xRot = 0.2F + idleSwaySlow;
			this.body.zRot = Mth.cos(ageInTicks / 10.0F) / 10.0F;

			this.rightArm.xRot = (float) Math.toRadians(-90) - (Mth.sin(ageInTicks / 4.0F) / 7.0F);
			this.leftArm.xRot = (float) Math.toRadians(-90) + (Mth.sin(ageInTicks / 4.0F) / 7.0F);

		} else if (limbSwingAmount > 0.15F) {
			this.rightArm.xRot = -walkCycle;
			this.leftArm.xRot = walkCycle;
			this.jaw.xRot = idleSwaySlow / 2.0F;

		} else {
			this.rightArm.zRot = idleSway;
			this.leftArm.zRot = -idleSway;
			this.jaw.xRot = idleSway / 2.0F;
		}
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
		this.rightLeg.xRot = -leftLeg.xRot;
		this.leftForLeg.xRot = leftLeg.xRot < 0 ? -leftLeg.xRot : 0;
		this.rightForLeg.xRot = rightLeg.xRot < 0 ? -rightLeg.xRot : 0;

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		this.tendril1.xRot = idleSway;
		this.tendril2.xRot = -idleSway;
		this.tendril1.zRot = -idleSway;
		this.tendril2.zRot = idleSway;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
	}
}