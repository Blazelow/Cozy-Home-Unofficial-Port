package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;// Made with Blockbench 4.11.2


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



public class WallClockModel extends Model {
	private final ModelPart hour_hand;
	private final ModelPart minute_hand;
	private final ModelPart bb_main;
	public WallClockModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.hour_hand = root.getChild("hour_hand");
		this.minute_hand = root.getChild("minute_hand");
		this.bb_main = root.getChild("bb_main");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition hour_hand = modelPartData.addOrReplaceChild("hour_hand", CubeListBuilder.create().texOffs(28, 3).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 7.16F));

		PartDefinition minute_hand = modelPartData.addOrReplaceChild("minute_hand", CubeListBuilder.create().texOffs(26, 2).addBox(-0.5F, -3.5F, -0.17F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 7.0F));

		PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -14.0F, 7.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(-0.5F, -8.5F, 6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-4.0F, -12.0F, 7.4F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-3.5F, -11.5F, 7.3F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(15, 14).addBox(-4.0F, -4.0F, 7.0F, 8.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(4.0F, -12.0F, 7.0F, 0.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 14).addBox(-4.0F, -12.0F, 7.0F, 0.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-6.0F, -16.0F, 7.0F, 12.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-6.0F, -2.0F, 7.0F, 12.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 16).addBox(-8.0F, -16.0F, 7.0F, 2.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(28, 16).mirror().addBox(6.0F, -16.0F, 7.0F, 2.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame_top_r1 = bb_main.addOrReplaceChild("frame_top_r1", CubeListBuilder.create().texOffs(15, 13).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 7.5F, 0.0F, 0.0F, -3.1416F));
		return LayerDefinition.create(modelData, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		hour_hand.render(matrices, vertices, light, overlay);
		minute_hand.render(matrices, vertices, light, overlay);
		bb_main.render(matrices, vertices, light, overlay);
	}

	public void setAngles(float hourHandTurnAmount, float minuteHandTurnAmount) {
		this.minute_hand.roll = minuteHandTurnAmount;
		this.hour_hand.roll = hourHandTurnAmount;
	}
}