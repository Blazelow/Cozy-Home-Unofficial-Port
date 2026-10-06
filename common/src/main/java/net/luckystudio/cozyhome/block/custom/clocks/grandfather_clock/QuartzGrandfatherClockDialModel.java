package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;

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

/** Centered hour markers and a gold centre pin for the quartz clock. */
public class QuartzGrandfatherClockDialModel extends Model {
    private final ModelPart dial;
    private final ModelPart knob;

    public QuartzGrandfatherClockDialModel(ModelPart root) {
        super(RenderType::entityCutoutNoCullZOffset);
        this.dial = root.getChild("dial");
        this.knob = root.getChild("knob");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("dial", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.5F, -4.5F, 0.40F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, -7.5F));
        root.addOrReplaceChild("knob", CubeListBuilder.create().texOffs(18, 0)
                        .addBox(-1.0F, -1.0F, -0.50F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, -7.5F));
        return LayerDefinition.create(meshDefinition, 32, 16);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        dial.render(matrices, vertices, light, overlay, color);
        knob.render(matrices, vertices, light, overlay, color);
    }
}
