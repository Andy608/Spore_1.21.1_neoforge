package com.Harbinger.Spore.Client.Models;// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Harbinger.Spore.Sentities.EvolvedInfected.Knight;
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

public class KnightModel<T extends Knight> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation( ResourceLocation.fromNamespaceAndPath(Spore.MODID, "knightmodel"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart rightArm;
	private final ModelPart rightForearm;
	private final ModelPart rightFingers;
	private final ModelPart rightThumb;
	private final ModelPart leftLeg;
	private final ModelPart leftForeleg;
	private final ModelPart rightLeg;
	private final ModelPart rightForeleg;

	public KnightModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = body.getChild("head");
		this.jaw = head.getChild("jaw");
		this.rightArm = body.getChild("RightArm");
		this.rightForearm = rightArm.getChild("RightForArm");
		this.rightFingers = rightForearm.getChild("fingers");
		this.rightThumb = rightForearm.getChild("tumb");
		this.leftLeg = body.getChild("LeftLeg");
		this.leftForeleg = leftLeg.getChild("leftForLeg");
		this.rightLeg = body.getChild("RightLeg");
		this.rightForeleg = rightLeg.getChild("rightForLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 30).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-3.5F, 4.5F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(-0.3F))
		.texOffs(22, 39).addBox(-3.5F, 10.0F, -2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 63).addBox(3.0F, -3.0F, -3.0F, 2.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 63).addBox(5.0F, -7.0F, -3.0F, 2.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(17, 57).addBox(3.0F, 9.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(27, 55).addBox(-2.0F, -0.25F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(19, 66).addBox(-2.0F, 7.75F, -2.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-6, 81).addBox(1.0F, -15.0F, -5.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(-6, 81).addBox(4.0F, -20.0F, -5.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-6, 81).addBox(-3.0F, -25.0F, 0.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.2182F, 0.0F, 0.2182F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-6, 81).addBox(-2.0F, -21.0F, -6.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(-6, 81).addBox(-6.0F, -18.0F, -4.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(-6, 81).addBox(-12.0F, -28.0F, -6.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(-6, 81).addBox(-12.0F, -30.0F, -9.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 22).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(40, 41).addBox(-3.5F, 0.0F, -3.75F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition mushroom2 = head.addOrReplaceChild("mushroom2", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, -4.0F, -6.0F, 0.0F, 8.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-2.0F, -2.0F, -6.0F, 0.0F, 8.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition RightArm = body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 54).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition RightForArm = RightArm.addOrReplaceChild("RightForArm", CubeListBuilder.create().texOffs(52, 27).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition fingers = RightForArm.addOrReplaceChild("fingers", CubeListBuilder.create().texOffs(12, 55).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(12, 55).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, 0.0F));

		PartDefinition tumb = RightForArm.addOrReplaceChild("tumb", CubeListBuilder.create().texOffs(58, -3).addBox(0.0F, -1.0F, -3.0F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.0F));

		PartDefinition RightLeg = body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(42, 47).addBox(-1.75F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition rightForLeg = RightLeg.addOrReplaceChild("rightForLeg", CubeListBuilder.create().texOffs(14, 45).addBox(-1.75F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition LeftLeg = body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(28, 45).addBox(-1.25F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition leftForLeg = LeftLeg.addOrReplaceChild("leftForLeg", CubeListBuilder.create().texOffs(0, 44).addBox(-1.25F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition mushroom = body.addOrReplaceChild("mushroom", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 2.0F));

		PartDefinition tendril = body.addOrReplaceChild("tendril", CubeListBuilder.create(), PartPose.offset(5.0F, 6.0F, 2.0F));

		PartDefinition tendril2 = body.addOrReplaceChild("tendril2", CubeListBuilder.create(), PartPose.offset(5.0F, 6.0F, -2.0F));

		PartDefinition tendril3 = body.addOrReplaceChild("tendril3", CubeListBuilder.create(), PartPose.offset(7.0F, 0.0F, -1.0F));

		PartDefinition tendril4 = body.addOrReplaceChild("tendril4", CubeListBuilder.create(), PartPose.offset(7.0F, -3.0F, 1.0F));

		PartDefinition flower = body.addOrReplaceChild("flower", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -5.0F, -1.0F, 0.3054F, 0.0F, 0.4363F));

		PartDefinition cube_r4 = flower.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 75).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r5 = flower.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(10, 75).addBox(0.0F, 0.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r6 = flower.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(10, 75).addBox(-4.0F, 0.0F, 0.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r7 = flower.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(10, 75).addBox(-4.0F, 0.0F, -6.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		setupHeadAnimations(entity, ageInTicks);

		if (entity.isAggressive()) {
			handleAggressiveAnimations(entity, limbSwing, limbSwingAmount, ageInTicks);
		} else if (Math.abs(limbSwingAmount) > 0.15F) {
			handleWalkingAnimations(limbSwing, limbSwingAmount);
		} else {
			handleIdleAnimations(ageInTicks);
		}

		setupLegAnimations(limbSwing, limbSwingAmount);
	}

	private void setupHeadAnimations(T entity, float ageInTicks) {
		float jawMovement = Mth.sin(ageInTicks / 8) / 10;
		if (entity.isAggressive()) {
			jawMovement = Mth.sin(ageInTicks / 6) / 10;
		}

		jaw.xRot = jawMovement;
	}

	private void handleAggressiveAnimations(T entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
		rightArm.xRot = 25F + Mth.cos(limbSwing * 0.2F) * limbSwingAmount;
		rightForearm.xRot = -88.5F;
		body.zRot = Mth.cos(limbSwing / 4) / 10;

		if (entity.swinging) {
			handleSwingAttackAnimation();
		} else if (entity.attackAnim > 0) {
			handleAttackAnimation(entity);
		}
	}

	private void handleSwingAttackAnimation() {
		float attackProgress = 1.0F; // You might want to track this differently
		rightArm.xRot = -90F + attackProgress;
		rightForearm.xRot = 0;
	}

	private void handleAttackAnimation(T entity) {
		float attackProgress = 1.0F - Mth.abs(10 - 2 * entity.attackAnim) / 10.0F;
		rightArm.xRot = Mth.sin(attackProgress) * 2.0F;
	}

	private void handleWalkingAnimations(float limbSwing, float limbSwingAmount) {
		rightArm.xRot = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
		rightArm.zRot = 0;
		resetBodyRotation();
	}

	private void handleIdleAnimations(float ageInTicks) {
		resetBodyRotation();
		rightArm.zRot = Mth.sin(ageInTicks / 8) / 10;
		rightFingers.zRot = Mth.sin(ageInTicks / 4) / 8;
		rightThumb.xRot = -Mth.sin(ageInTicks / 4) / 8;
		rightForearm.xRot = 0;
	}

	private void setupLegAnimations(float limbSwing, float limbSwingAmount) {
		leftLeg.xRot = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
		rightLeg.xRot = Mth.cos(limbSwing * 0.8F) * -0.8F * limbSwingAmount;
		updateForelegAnimations();
	}

	private void updateForelegAnimations() {
		if (leftLeg.xRot < 0) {
			leftForeleg.xRot = -2 * leftLeg.xRot;
		}
		if (rightLeg.xRot < 0) {
			rightForeleg.xRot = -2 * rightLeg.xRot;
		}
	}

	private void resetBodyRotation() {
		body.zRot = 0;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
	}
}