package net.luckystudio.cozyhome.block.custom.seatable.chairs;
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
import net.minecraft.client.renderer.rendertype.RenderType;

/**
 * This is the model of the chair.
 */
public class ChairModel extends Model {
    private final ModelPart chair;
    private final ModelPart back;
    private final ModelPart seat;
    private final ModelPart bb_main;

    public ChairModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.chair = root.getChild("chair");
        this.back = root.getChild("back");
        this.seat = root.getChild("seat");
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

        PartDefinition south_east_leg_outer_r1 = seat.addOrReplaceChild("south_east_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F))
                .texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0436F, 0.0F, 0.0436F));

        PartDefinition south_west_leg_outer_r1 = seat.addOrReplaceChild("south_west_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0436F, 0.0F, -0.0436F));

        PartDefinition north_west_leg_outer_r1 = seat.addOrReplaceChild("north_west_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, -10.0F, -0.0436F, -0.0019F, -0.0436F));

        PartDefinition north_east_leg_outer_r1 = seat.addOrReplaceChild("north_east_leg_outer_r1", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F))
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, -10.0F, -0.0436F, 0.0F, 0.0436F));

        PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition west_cover_r1 = bb_main.addOrReplaceChild("west_cover_r1", CubeListBuilder.create().texOffs(20, 20).addBox(0.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition east_cover_r1 = bb_main.addOrReplaceChild("east_cover_r1", CubeListBuilder.create().texOffs(20, 20).addBox(-10.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition back_cover_r1 = bb_main.addOrReplaceChild("back_cover_r1", CubeListBuilder.create().texOffs(40, 30).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 5.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition front_cover_r1 = bb_main.addOrReplaceChild("front_cover_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -5.0F, -0.0436F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        chair.render(matrices, vertices, light, overlay);
        bb_main.render(matrices, vertices, light, overlay);
    }
}
