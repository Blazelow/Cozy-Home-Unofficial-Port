package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;
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

public class GrandfatherClockModel extends Model<GrandfatherClockRenderState> {
	private final ModelPart main;
	private final ModelPart minHand;
	private final ModelPart hourHand;
	private final ModelPart pendulum;
	public GrandfatherClockModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutZOffset);
        this.main = root.getChild("main");
		this.minHand = root.getChild("min_hand");
		this.hourHand = root.getChild("hour_hand");
		this.pendulum = root.getChild("pendulum");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition main = modelPartData.addOrReplaceChild("main", CubeListBuilder.create().texOffs(56, 95).addBox(-5.0F, -15.0F, 2.5F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(56, 73).addBox(-7.0F, -15.0F, 0.5F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -7.0F, 0.5F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(42, 11).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 73).addBox(-7.0F, 7.0F, 0.5F, 14.0F, 28.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(27, 44).addBox(-6.0F, 7.0F, 12.5F, 12.0F, 28.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(53, 35).addBox(5.0F, 7.0F, 2.5F, 1.0F, 28.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(75, 35).addBox(-6.0F, 7.0F, 2.5F, 1.0F, 28.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-6.0F, 7.0F, 1.5F, 12.0F, 28.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-7.0F, 35.0F, 0.5F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(97, 45).addBox(-4.0F, 10.0F, 1.5F, 8.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(-4.5F, -4.5F, 0.4F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 14).addBox(-1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 14).addBox(-1.0F, 3.0F, 6.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 0).addBox(-3.0F, -3.0F, 4.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -7.5F));

		PartDefinition min_hand = modelPartData.addOrReplaceChild("min_hand", CubeListBuilder.create().texOffs(48, 9).addBox(-0.5F, -4.5F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -7.5F));

		PartDefinition hour_hand = modelPartData.addOrReplaceChild("hour_hand", CubeListBuilder.create().texOffs(50, 10).addBox(-0.5F, -3.5F, 0.25F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -7.5F));

		PartDefinition pendulum = modelPartData.addOrReplaceChild("pendulum", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}

	@Override
	public void setupAnim(GrandfatherClockRenderState state) {
		super.setupAnim(state);
		this.minHand.zRot = state.minuteHandAngle;
		this.hourHand.zRot = state.hourHandAngle;
		this.pendulum.zRot = state.pendulumAngle;
		this.main.visible = !state.handsOnly;
		this.pendulum.visible = !state.handsOnly;
	}
}