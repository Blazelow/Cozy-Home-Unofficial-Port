package net.luckystudio.cozyhome.entity.model;// Made with Blockbench 4.11.2

import net.luckystudio.cozyhome.CozyHome;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports


public class OminousChairModel extends EntityModel<Entity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "ominous_chair_model"), "main");
	private final ModelPart chair;
	private final ModelPart back;
	private final ModelPart seat;
	private final ModelPart spike;
	private final ModelPart spike2;
	private final ModelPart spike3;
	private final ModelPart bb_main;
	public OminousChairModel(ModelPart root) {
		this.chair = root.getChild("chair");
		this.back = root.getChild("back");
		this.seat = root.getChild("seat");
		this.spike = root.getChild("spike");
		this.spike2 = root.getChild("spike2");
		this.spike3 = root.getChild("spike3");
		this.bb_main = root.getChild("bb_main");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition chair = modelPartData.addOrReplaceChild("chair", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition back = chair.addOrReplaceChild("back", CubeListBuilder.create().texOffs(28, 0).addBox(-6.0F, -16.0F, 1.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -14.0F, 0.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 0).addBox(4.0F, -14.0F, 0.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.25F))
		.texOffs(0, 0).mirror().addBox(-6.0F, -14.0F, 0.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(8, 0).mirror().addBox(-6.0F, -14.0F, 0.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition seat = chair.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(16, 16).addBox(-6.0F, 0.0F, -11.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 5.0F));

		PartDefinition west_cover_r1 = seat.addOrReplaceChild("west_cover_r1", CubeListBuilder.create().texOffs(20, 20).addBox(0.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, -5.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition east_cover_r1 = seat.addOrReplaceChild("east_cover_r1", CubeListBuilder.create().texOffs(20, 20).addBox(-10.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, -5.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition back_cover_r1 = seat.addOrReplaceChild("back_cover_r1", CubeListBuilder.create().texOffs(40, 30).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

		PartDefinition front_cover_r1 = seat.addOrReplaceChild("front_cover_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -10.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition south_east_leg_outer_r1 = seat.addOrReplaceChild("south_east_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F))
		.texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0436F, 0.0F, 0.0436F));

		PartDefinition south_west_leg_outer_r1 = seat.addOrReplaceChild("south_west_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0436F, 0.0F, -0.0436F));

		PartDefinition north_west_leg_outer_r1 = seat.addOrReplaceChild("north_west_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, -10.0F, -0.0436F, -0.0019F, -0.0436F));

		PartDefinition north_east_leg_outer_r1 = seat.addOrReplaceChild("north_east_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F))
		.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, -10.0F, -0.0436F, 0.0F, 0.0436F));

		PartDefinition spike = modelPartData.addOrReplaceChild("spike", CubeListBuilder.create(), PartPose.offset(-1.5F, 13.0F, 2.0F));

		PartDefinition cube_r1 = spike.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r2 = spike.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spike2 = modelPartData.addOrReplaceChild("spike2", CubeListBuilder.create(), PartPose.offset(1.5F, 13.0F, 1.0F));

		PartDefinition cube_r3 = spike2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r4 = spike2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spike3 = modelPartData.addOrReplaceChild("spike3", CubeListBuilder.create(), PartPose.offset(-0.5F, 13.0F, -1.0F));

		PartDefinition cube_r5 = spike3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r6 = spike3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 2).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 38).addBox(-6.0F, -14.0F, -3.0F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		chair.render(matrices, vertices, light, overlay);
		spike.render(matrices, vertices, light, overlay);
		spike2.render(matrices, vertices, light, overlay);
		spike3.render(matrices, vertices, light, overlay);
		bb_main.render(matrices, vertices, light, overlay);
	}
}