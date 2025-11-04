package com.Harbinger.Spore.Client.Models;

import com.Harbinger.Spore.Sentities.Utility.InfEvoClaw;
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

public class InfEvoClawModel<T extends InfEvoClaw> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "infevoclawmodel"), "main");
	private final ModelPart baseJoint;

	private final ModelPart middleF;
	private final ModelPart middleF2;
	private final ModelPart middleF3;
	private final ModelPart[] fingerT = new ModelPart[4];
	private final ModelPart[] fingerG = new ModelPart[4];

	public InfEvoClawModel(ModelPart root) {
		this.baseJoint = root.getChild("baseJoint");
		ModelPart base = this.baseJoint.getChild("base");

		this.middleF = base.getChild("middleF");
		this.middleF2 = middleF.getChild("middleF2");
		this.middleF3 = middleF2.getChild("middleF3");

		this.fingerT[0] = base.getChild("fingerT");
		this.fingerT[1] = base.getChild("fingerT2");
		this.fingerT[2] = base.getChild("fingerT3");
		this.fingerT[3] = base.getChild("fingerT4");

		this.fingerG[0] = fingerT[0].getChild("jointT").getChild("fingerG");
		this.fingerG[1] = fingerT[1].getChild("jointT2").getChild("fingerG2");
		this.fingerG[2] = fingerT[2].getChild("jointT3").getChild("fingerG3");
		this.fingerG[3] = fingerT[3].getChild("jointT4").getChild("fingerG4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition baseJoint = partdefinition.addOrReplaceChild("baseJoint", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 7.0F));

		PartDefinition base = baseJoint.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -1.25F, 0.0F, -1.5708F, 3.1416F, 0.0F));

		PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 9).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition middleF = base.addOrReplaceChild("middleF", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -5.75F, -1.25F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 1.0F));

		PartDefinition middleF2 = middleF.addOrReplaceChild("middleF2", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition cube_r2 = middleF2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 18).addBox(-1.0F, -5.75F, -1.25F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition middleF3 = middleF2.addOrReplaceChild("middleF3", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, -2.0F));

		PartDefinition cube_r3 = middleF3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, -5.75F, -1.25F, 0.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition fingerT = base.addOrReplaceChild("fingerT", CubeListBuilder.create(), PartPose.offset(1.25F, -7.0F, 0.0F));

		PartDefinition jointT = fingerT.addOrReplaceChild("jointT", CubeListBuilder.create().texOffs(32, 9).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition fingerG = jointT.addOrReplaceChild("fingerG", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jointG = fingerG.addOrReplaceChild("jointG", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition claw = jointG.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(8, 25).addBox(0.0F, -6.0F, -1.0F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition fingerT2 = base.addOrReplaceChild("fingerT2", CubeListBuilder.create(), PartPose.offset(1.25F, -11.0F, 1.0F));

		PartDefinition jointT2 = fingerT2.addOrReplaceChild("jointT2", CubeListBuilder.create().texOffs(30, 0).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

		PartDefinition fingerG2 = jointT2.addOrReplaceChild("fingerG2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jointG2 = fingerG2.addOrReplaceChild("jointG2", CubeListBuilder.create().texOffs(28, 25).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition claw2 = jointG2.addOrReplaceChild("claw2", CubeListBuilder.create().texOffs(22, 22).addBox(0.0F, -6.0F, -1.0F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition fingerT3 = base.addOrReplaceChild("fingerT3", CubeListBuilder.create(), PartPose.offset(-0.75F, -10.75F, 1.0F));

		PartDefinition jointT3 = fingerT3.addOrReplaceChild("jointT3", CubeListBuilder.create().texOffs(28, 16).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0036F));

		PartDefinition fingerG3 = jointT3.addOrReplaceChild("fingerG3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jointG3 = fingerG3.addOrReplaceChild("jointG3", CubeListBuilder.create().texOffs(14, 28).addBox(-0.5F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition claw3 = jointG3.addOrReplaceChild("claw3", CubeListBuilder.create().texOffs(22, 15).addBox(0.0F, -6.0F, -1.0F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition fingerT4 = base.addOrReplaceChild("fingerT4", CubeListBuilder.create(), PartPose.offset(-0.75F, -5.75F, 0.0F));

		PartDefinition jointT4 = fingerT4.addOrReplaceChild("jointT4", CubeListBuilder.create().texOffs(22, 9).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

		PartDefinition fingerG4 = jointT4.addOrReplaceChild("fingerG4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jointG4 = fingerG4.addOrReplaceChild("jointG4", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition claw4 = jointG4.addOrReplaceChild("claw4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -7.0F, -2.0F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float sinTime = Mth.sin(ageInTicks * 0.125f);
		float baseRotation = sinTime * 0.1667f;
		float fingerRotation = sinTime * 0.125f;

		float middleRotation = baseRotation * 0.5f;
		this.middleF.xRot = middleRotation;
		this.middleF2.xRot = middleRotation * 1.1f;
		this.middleF3.xRot = middleRotation * 1.2f;

		float[] yRotations = {baseRotation, baseRotation, -baseRotation, -baseRotation};
		float[] zRotations = {-fingerRotation * 0.75f, -fingerRotation * 0.875f, fingerRotation * 1.125f, -fingerRotation * 0.75f};
		float[] fingerYRotations = {baseRotation, baseRotation, -baseRotation, -baseRotation};

		for (int i = 0; i < 4; i++) {
			this.fingerT[i].yRot = yRotations[i];
			this.fingerT[i].zRot = zRotations[i];
			this.fingerG[i].yRot = fingerYRotations[i];
		}

		float secondaryWave = Mth.cos(ageInTicks * 0.2f) * 0.05f;
		this.middleF.xRot += secondaryWave;
		this.middleF2.xRot += secondaryWave * 0.8f;
		this.middleF3.xRot += secondaryWave * 0.6f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int alpha) {
		baseJoint.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
	}

}