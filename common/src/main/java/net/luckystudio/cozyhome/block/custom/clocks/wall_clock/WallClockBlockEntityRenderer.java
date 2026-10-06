package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import java.util.Map;
public class WallClockBlockEntityRenderer implements BlockEntityRenderer<WallClockBlockEntity, WallClockRenderState> {
    private final WallClockModel wall_clock;
    private static final Map<WallClockBlock.ClockType, Identifier> grandfather_clock_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(WallClockBlock.Type.OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/oak_wall_clock.png"));
        map.put(WallClockBlock.Type.SPRUCE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/spruce_wall_clock.png"));
        map.put(WallClockBlock.Type.BIRCH, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/birch_wall_clock.png"));
        map.put(WallClockBlock.Type.JUNGLE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/jungle_wall_clock.png"));
        map.put(WallClockBlock.Type.ACACIA, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/acacia_wall_clock.png"));
        map.put(WallClockBlock.Type.DARK_OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/dark_oak_wall_clock.png"));
        map.put(WallClockBlock.Type.MANGROVE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/mangrove_wall_clock.png"));
        map.put(WallClockBlock.Type.CHERRY, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/cherry_wall_clock.png"));
        map.put(WallClockBlock.Type.BAMBOO, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/bamboo_wall_clock.png"));
        map.put(WallClockBlock.Type.CRIMSON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/crimson_wall_clock.png"));
        map.put(WallClockBlock.Type.WARPED, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/warped_wall_clock.png"));
        map.put(WallClockBlock.Type.QUARTZ, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/quartz_wall_clock_hands.png"));
        map.put(WallClockBlock.Type.IRON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/iron_wall_clock.png"));
        map.put(WallClockBlock.Type.GLASS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/glass_wall_clock.png"));
        map.put(WallClockBlock.Type.UNDEAD, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/undead_wall_clock.png"));
        map.put(WallClockBlock.Type.OMINOUS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/ominous_wall_clock_inactive.png"));
    });

    // How far does this block render.
    @Override
    public int getViewDistance() {
        return 64;
    }

    public WallClockBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.wall_clock = new WallClockModel(ctx.bakeLayer(ModEntityModelLayers.WALL_CLOCK));
    }

    @Override
    public WallClockRenderState createRenderState() {
        return new WallClockRenderState();
    }

    @Override
    public void extractRenderState(WallClockBlockEntity entity, WallClockRenderState state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        BlockState blockState = entity.getBlockState();
        state.rotationDegrees = ModProperties.setSeatRotationFromFacing(blockState);
        WallClockBlock.ClockType clockType = ((WallClockBlock) blockState.getBlock()).getClockType();
        state.texture = getClockTexture(clockType);
        state.handsOnly = clockType == WallClockBlock.Type.QUARTZ;

        // Interpolate angles for smooth rendering (converted to radians)
        state.hourHandAngle = Mth.lerp(tickDelta, entity.lastHourHandAngle, entity.currentHourHandAngle) * ((float) Math.PI / 180.0f);
        state.minuteHandAngle = Mth.lerp(tickDelta, entity.lastMinuteHandAngle, entity.currentMinuteHandAngle) * ((float) Math.PI / 180.0f);
    }

    @Override
    public void submit(WallClockRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState camera) {
        matrices.pushPose();
        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.rotationDegrees));
        if (state.handsOnly) {
            // The quartz body is the normal block model, so only the moving hands are drawn here
            matrices.translate(0.0D, 0.0D, -1.0D / 64.0D);
        }
        collector.submitModel(this.wall_clock, state, matrices, RenderTypes.entityCutoutZOffset(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, state.breakProgress);
        matrices.popPose();
    }

    public static Identifier getClockTexture(WallClockBlock.ClockType type) {
        Identifier identifier = grandfather_clock_TEXTURES.get(type);
        return identifier;
    }
}
