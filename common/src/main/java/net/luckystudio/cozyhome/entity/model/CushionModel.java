package net.luckystudio.cozyhome.entity.model;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Only provides the layer definition (model parts); rendering is done by the block entity renderers. */
public class CushionModel {
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(-12, 11).addBox(-6.0F, -10.01F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition front_r1 = bb_main.addOrReplaceChild("front_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -6.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition east_r1 = bb_main.addOrReplaceChild("east_r1", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, -6.0F, 0.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition west_r1 = bb_main.addOrReplaceChild("west_r1", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, -6.0F, 0.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition south_r1 = bb_main.addOrReplaceChild("south_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 6.0F, 0.0436F, 0.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
}
