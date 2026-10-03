package net.luckystudio.cozyhome.block.custom.seatable.couches;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;

// Made with Blockbench 4.11.2

// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

public class CouchCushionModel extends Model<Object> {
	private final ModelPart bb_main;
	public CouchCushionModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
        this.bb_main = root.getChild("bb_main");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-5.0F, -10.0F, -1.0F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 1.0F, -0.3927F, 0.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}

}