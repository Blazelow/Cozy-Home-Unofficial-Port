package net.luckystudio.cozyhome.block.custom.telescope;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
public class TelescopeBlockEntityRenderer implements BlockEntityRenderer<TelescopeBlockEntity> {
    private final TelescopeModel model;

    public TelescopeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in your client mod initializer)
        this.model = new TelescopeModel(ctx.bakeLayer(ModEntityModelLayers.TELESCOPE));
    }

    @Override
    public void render(TelescopeBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();  // Save the current matrix stack
        // Move the model to the center top of the block
        matrices.translate(0.5, 2.5, 0.5);  // Position the model on top of the block
        // Flip the model upright (rotate 180 degrees around the X axis)
        matrices.mulPose(Axis.XP.rotationDegrees(180)); // Rotate the model 180 degrees around the X-axis
        matrices.mulPose(Axis.YP.rotationDegrees(getRotationAngle(entity))); // Rotate the model 180 degrees around the X-axis

        // Render the entire model
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/entity/telescope_head.png")));
        this.model.setRotations(entity.getYaw(), entity.getPitch());
        this.model.render(matrices, vertexConsumer, light, overlay);

        matrices.popPose();  // Restore the matrix stack
    }


    private float getRotationAngle(TelescopeBlockEntity entity) {
        // Rotate the model based on the block's facing direction
        Direction facing = entity.getBlockState().getValue(TelescopeBlock.FACING);
        return switch (facing) {
            case NORTH -> 0;  // Rotate to face north
            case SOUTH -> 180;    // No rotation needed for south
            case WEST -> -90;    // Rotate 90 degrees for west
            case EAST -> 90;   // Rotate -90 degrees for east
            default -> 0;       // Default to no rotation
        };
    }
}
