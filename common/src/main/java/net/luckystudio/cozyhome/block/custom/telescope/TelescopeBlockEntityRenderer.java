package net.luckystudio.cozyhome.block.custom.telescope;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
public class TelescopeBlockEntityRenderer implements BlockEntityRenderer<TelescopeBlockEntity, TelescopeRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/entity/telescope_head.png");
    private final TelescopeModel model;

    public TelescopeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in your client mod initializer)
        this.model = new TelescopeModel(ctx.bakeLayer(ModEntityModelLayers.TELESCOPE));
    }

    @Override
    public TelescopeRenderState createRenderState() {
        return new TelescopeRenderState();
    }

    @Override
    public void extractRenderState(TelescopeBlockEntity entity, TelescopeRenderState state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        state.yaw = entity.getYaw();
        state.pitch = entity.getPitch();
        state.facingDegrees = getRotationAngle(entity);
        Minecraft minecraft = Minecraft.getInstance();
        state.hidden = minecraft.player != null && minecraft.options.getCameraType().isFirstPerson()
                && TelescopeBlock.isLookingThrough(minecraft.player, entity.getBlockPos());
    }

    @Override
    public void submit(TelescopeRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.hidden) return;
        matrices.pushPose();  // Save the current matrix stack
        // Move the model to the center top of the block
        matrices.translate(0.5, 2.5, 0.5);
        // Flip the model upright (rotate 180 degrees around the X axis)
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.facingDegrees));

        collector.submitModel(this.model, state, matrices, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, state.breakProgress);

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
