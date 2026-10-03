package net.luckystudio.cozyhome.block.custom.telescope;


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
// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class TelescopeModel extends Model {
	private final ModelPart holder;
	private final ModelPart head;
	public TelescopeModel(ModelPart root) {
		super(RenderType::entityCutout);
        this.holder = root.getChild("holder");
		this.head = this.holder.getChild("head");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition holder = modelPartData.addOrReplaceChild("holder", CubeListBuilder.create().texOffs(0, 29).addBox(-3.0F, -2.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 19).addBox(-1.5F, 0.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition head = holder.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -5.5F, -10.2F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.5F, -6.5F, -0.3F, 5.0F, 5.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(21, -10).addBox(0.0F, -2.5F, -4.8F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -0.2F));
		return LayerDefinition.create(modelData, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.holder.render(matrices, vertices, light, overlay);
	}

	public void setRotations(float yaw, float pitch) {
		this.holder.yRot = (float) Math.toRadians(yaw);  // Rotate the yaw part
		this.head.xRot = (float) Math.toRadians(pitch);  // Rotate the pitch part
	}
}