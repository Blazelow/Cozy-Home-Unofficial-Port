package net.luckystudio.cozyhome.block.custom.seatable.sofas;// Made with Blockbench 4.11.2


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports


public class SofaModel extends Model {
	private final ModelPart dyeable;
	private final ModelPart frame;
	public SofaModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.dyeable = root.getChild("dyeable");
		this.frame = root.getChild("frame");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition dyeable = modelPartData.addOrReplaceChild("dyeable", CubeListBuilder.create().texOffs(48, 0).addBox(-7.0F, -3.0F, -16.0F, 14.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 7.0F));

		PartDefinition left_arm_r1 = dyeable.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(44, 14).addBox(-3.0F, -5.0F, -9.0F, 6.0F, 6.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -5.0F, -8.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition right_arm_r1 = dyeable.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(44, 14).mirror().addBox(-3.0F, -5.0F, -9.0F, 6.0F, 6.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.0F, -5.0F, -8.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition head_r1 = dyeable.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -26.5F, -4.5F, 16.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-8.0F, -18.5F, -2.5F, 16.0F, 21.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.5F, -0.1309F, 0.0F, 0.0F));

		PartDefinition frame = modelPartData.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(54, 38).mirror().addBox(-27.0F, -10.0F, -9.0F, 3.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(54, 38).addBox(-10.0F, -10.0F, -9.0F, 3.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-24.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(-12.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-24.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 43).addBox(-24.0F, -7.0F, -9.0F, 14.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(17.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 61).addBox(-8.0F, 2.5F, -2.5F, 16.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -5.5F, 6.5F, -0.1309F, 0.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		dyeable.render(matrices, vertices, light, overlay);
		frame.render(matrices, vertices, light, overlay);
	}
}
