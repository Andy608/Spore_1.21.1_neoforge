package com.Harbinger.Spore.Client.Models;// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Harbinger.Spore.Sentities.EvolvedInfected.Spitter;
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

public class SpitterModel<T extends Spitter> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation( ResourceLocation.fromNamespaceAndPath(Spore.MODID, "spittermodel"), "main");
	private final ModelPart body;
	private final ModelPart bodywear;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart neck;
	private final ModelPart neckJoint;
	private final ModelPart neck2;
	private final ModelPart mutatedTube;
	private final ModelPart RightMandible;
	private final ModelPart LeftMandible;
	private final ModelPart RMJ;
	private final ModelPart RightMandible2;
	private final ModelPart LMJ;
	private final ModelPart LeftMandible2;
	private final ModelPart neckJoint2;
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart leftForeleg;
	private final ModelPart rightForeleg;
	private static final float WALK_THRESHOLD = 0.15F;
	private static final float ARM_WALK_SPEED = 0.8F;
	private static final float LEG_WALK_SPEED = 0.8F;
	private static final float IDLE_ARM_SPEED = 0.125f;
	private static final float NECK_SWAY_SPEED = 0.125f;
	private static final float MANDIBLE_SPEED_1 = 0.2f;
	private static final float MANDIBLE_SPEED_2 = 0.25f;
	private static final float MANDIBLE_SPEED_3 = 0.166f;

	public SpitterModel(ModelPart root) {
		this.body = root.getChild("body");
		this.bodywear = root.getChild("bodywear");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.neck = root.getChild("neck");
		this.neckJoint = neck.getChild("neckJoint");
		this.neck2 = neckJoint.getChild("neck2");
		this.mutatedTube = neck.getChild("mutated_tube");
		this.RightMandible = neck2.getChild("RightMandible");
		this.LeftMandible = neck2.getChild("LeftMandible");
		this.RMJ = RightMandible.getChild("RMJ");
		this.RightMandible2 = RMJ.getChild("RightMandible2");
		this.LMJ = LeftMandible.getChild("LMJ");
		this.LeftMandible2 = LMJ.getChild("LeftMandible2");
		this.neckJoint2 = neck2.getChild("neckJoint2");
		this.head = neckJoint2.getChild("head");
		this.nose = head.getChild("nose");
		this.leftForeleg = LeftLeg.getChild("leftForLeg");
		this.rightForeleg = RightLeg.getChild("rightForLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 9.0F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(74, 1).addBox(-5.0F, -22.0F, -2.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0495F, 0.045F, 0.258F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(74, 1).addBox(-1.0F, -19.0F, -4.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.1687F, -0.045F, -0.258F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(72, 0).addBox(-6.0F, -22.0F, -7.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.5F, 2.75F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 10).addBox(-4.0F, -2.5F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -7.0F, 3.0F, 0.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(22, 25).addBox(-4.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, -0.25F, 0.4363F, 0.0F, 0.0F));

		PartDefinition bodywear = partdefinition.addOrReplaceChild("bodywear", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 9.0F, -3.25F, 8.0F, 9.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.25F, 0.0436F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(44, 51).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 4.0F, -3.0F));

		PartDefinition RightForArm = RightArm.addOrReplaceChild("RightForArm", CubeListBuilder.create().texOffs(55, 19).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r4 = RightForArm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(48, 5).addBox(0.0F, 0.0F, -1.5F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.0F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r5 = RightForArm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 53).addBox(0.0F, -2.0F, -1.5F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 51).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 4.0F, -3.0F));

		PartDefinition LeftForArm = LeftArm.addOrReplaceChild("LeftForArm", CubeListBuilder.create().texOffs(55, 19).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition cube_r6 = LeftForArm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 13).addBox(0.5F, 0.0F, -1.5F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r7 = LeftForArm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 29).addBox(0.5F, -2.0F, -1.5F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, -0.1745F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(16, 45).addBox(-1.75F, 0.0F, -2.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition rightForLeg = RightLeg.addOrReplaceChild("rightForLeg", CubeListBuilder.create().texOffs(44, 19).addBox(-1.75F, 0.0F, -2.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 41).addBox(-1.25F, 0.0F, -2.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition leftForLeg = LeftLeg.addOrReplaceChild("leftForLeg", CubeListBuilder.create().texOffs(38, 40).addBox(-1.25F, 0.0F, -2.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -3.0F));

		PartDefinition neckJoint = neck.addOrReplaceChild("neckJoint", CubeListBuilder.create().texOffs(23, 38).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition neck2 = neckJoint.addOrReplaceChild("neck2", CubeListBuilder.create(), PartPose.offset(0.0F, -1.75F, 0.0F));

		PartDefinition neckJoint2 = neck2.addOrReplaceChild("neckJoint2", CubeListBuilder.create().texOffs(24, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition head = neckJoint2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(52, 36).addBox(-3.5F, -2.0F, -4.5F, 7.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -9.8F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.2F))
		.texOffs(62, 0).addBox(-4.0F, -10.0F, -5.0F, 8.0F, 7.75F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(28, 19).addBox(-4.0F, -2.0F, 1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-3.5F, -2.0F, -4.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(24, 2).addBox(3.5F, -2.0F, -4.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.25F, -2.0F, -1.1345F, 0.0F, 0.0F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(25, 61).addBox(-6.0F, -34.0F, -15.0F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 27.0F, 5.0F, -0.2597F, 0.0338F, 0.1265F));

		PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(25, 61).addBox(-1.0F, -31.0F, -7.0F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 27.0F, 5.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(72, 0).addBox(-8.0F, -31.0F, -4.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 27.0F, 5.0F, 0.0495F, 0.045F, 0.258F));

		PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(72, 0).addBox(-6.0F, -34.0F, -12.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 27.0F, 5.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0F, -3.25F, -4.5F));

		PartDefinition cube_r12 = nose.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.1207F, -2.0141F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -0.25F, -2.0F, 0.9163F, 0.0F, 0.0F));

		PartDefinition cube_r13 = nose.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r14 = nose.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -0.75F, -3.85F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, 0.0F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r15 = nose.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(4, 0).addBox(0.0F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r16 = nose.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(2, 0).addBox(-0.5F, -1.0F, -3.75F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r17 = nose.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(4, 2).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition RightMandible = neck2.addOrReplaceChild("RightMandible", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.25F, -1.5F));

		PartDefinition RMJ = RightMandible.addOrReplaceChild("RMJ", CubeListBuilder.create().texOffs(11, 37).addBox(0.0F, -2.5F, -6.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 7).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.4363F, 0.0F));

		PartDefinition RightMandible2 = RMJ.addOrReplaceChild("RightMandible2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition RMJ2 = RightMandible2.addOrReplaceChild("RMJ2", CubeListBuilder.create().texOffs(11, 35).addBox(0.0F, -2.5F, -6.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(46, 0).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, -0.3927F, 0.0F));

		PartDefinition LeftMandible = neck2.addOrReplaceChild("LeftMandible", CubeListBuilder.create(), PartPose.offset(3.0F, -0.25F, -1.5F));

		PartDefinition LMJ = LeftMandible.addOrReplaceChild("LMJ", CubeListBuilder.create().texOffs(32, 2).addBox(0.0F, -2.5F, -6.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(44, 32).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, -0.4363F, 0.0F));

		PartDefinition LeftMandible2 = LMJ.addOrReplaceChild("LeftMandible2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition LMJ2 = LeftMandible2.addOrReplaceChild("LMJ2", CubeListBuilder.create().texOffs(28, 17).addBox(0.0F, -2.5F, -6.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 1).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.3927F, 0.0F));

		PartDefinition mutated_tube = neck.addOrReplaceChild("mutated_tube", CubeListBuilder.create().texOffs(54, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

		PartDefinition mutated_tube2 = mutated_tube.addOrReplaceChild("mutated_tube2", CubeListBuilder.create().texOffs(8, 54).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition mutated_tube3 = mutated_tube2.addOrReplaceChild("mutated_tube3", CubeListBuilder.create().texOffs(53, 48).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition mutated_tube4 = mutated_tube3.addOrReplaceChild("mutated_tube4", CubeListBuilder.create().texOffs(27, 45).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition mutated_tube5 = mutated_tube4.addOrReplaceChild("mutated_tube5", CubeListBuilder.create().texOffs(52, 30).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(49, 39).addBox(-3.0F, -3.0F, -2.55F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -0.3491F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		setupArmAnimations(limbSwing, limbSwingAmount, ageInTicks);
		setupLegAnimations(limbSwing, limbSwingAmount);
		setupHeadAndNeckAnimations(netHeadYaw, headPitch, ageInTicks);
		setupMandibleAnimations(ageInTicks);
	}

	private void setupArmAnimations(float limbSwing, float limbSwingAmount, float ageInTicks) {
		if (Math.abs(limbSwingAmount) > WALK_THRESHOLD) {
			// Walking arm animation
			float armMovement = Mth.cos(limbSwing * ARM_WALK_SPEED) * ARM_WALK_SPEED * limbSwingAmount;
			RightArm.xRot = armMovement;
			LeftArm.xRot = -armMovement;
			RightArm.zRot = 0;
			LeftArm.zRot = 0;
		} else {
			// Idle arm animation
			float idleSway = Mth.sin(ageInTicks * IDLE_ARM_SPEED) * 0.1f;
			RightArm.zRot = idleSway;
			LeftArm.zRot = -idleSway;
		}
	}

	private void setupLegAnimations(float limbSwing, float limbSwingAmount) {
		float legMovement = Mth.cos(limbSwing * LEG_WALK_SPEED) * LEG_WALK_SPEED * limbSwingAmount;

		LeftLeg.xRot = legMovement;
		RightLeg.xRot = -legMovement;

		updateForelegAnimations();
	}

	private void updateForelegAnimations() {
		// Mirror foreleg rotation when leg is bent backward
		if (LeftLeg.xRot < 0) {
			leftForeleg.xRot = -LeftLeg.xRot;
		}
		if (RightLeg.xRot < 0) {
			rightForeleg.xRot = -RightLeg.xRot;
		}
	}

	private void setupHeadAndNeckAnimations(float netHeadYaw, float headPitch, float ageInTicks) {
		neck.yRot = netHeadYaw / (180F / (float) Math.PI);
		neck.xRot = headPitch / (90F / (float) Math.PI);

        neck2.xRot = Mth.sin(ageInTicks * NECK_SWAY_SPEED) * 0.125f;
		mutatedTube.xRot = Mth.sin(ageInTicks * NECK_SWAY_SPEED) * 0.1f;

		nose.xRot = 1 + Mth.sin(ageInTicks * NECK_SWAY_SPEED) * 0.125f;
	}

	private void setupMandibleAnimations(float ageInTicks) {
		setupPrimaryMandibleAnimations(ageInTicks);
		setupSecondaryMandibleAnimations(ageInTicks);
	}

	private void setupPrimaryMandibleAnimations(float ageInTicks) {
		float mandibleWave1 = Mth.sin(ageInTicks * MANDIBLE_SPEED_1);
		float mandibleWave2 = Mth.sin(ageInTicks * MANDIBLE_SPEED_2);

		RightMandible.xRot = mandibleWave1 * 0.25f;
		LeftMandible.xRot = mandibleWave1 * 0.166f;

		RightMandible.zRot = mandibleWave1 * 0.25f;
		LeftMandible.zRot = -mandibleWave2 * 0.2f;
	}

	private void setupSecondaryMandibleAnimations(float ageInTicks) {
		float mandibleWave3 = Mth.sin(ageInTicks * MANDIBLE_SPEED_3);
		float mandibleWave4 = Mth.sin(ageInTicks * MANDIBLE_SPEED_1);

		RightMandible2.xRot = mandibleWave3 * 0.2f;
		LeftMandible2.xRot = mandibleWave4 * 0.166f;

		RightMandible2.zRot = mandibleWave4 * 0.166f;
		LeftMandible2.zRot = -mandibleWave3 * 0.2f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
		bodywear.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay,alpha);
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
		neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
	}
}