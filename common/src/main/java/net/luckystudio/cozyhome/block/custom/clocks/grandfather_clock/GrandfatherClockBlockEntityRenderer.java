package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.TripleTallBlock;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import java.util.Map;
public class GrandfatherClockBlockEntityRenderer implements BlockEntityRenderer<GrandfatherClockBlockEntity, GrandfatherClockRenderState> {
    private final GrandfatherClockModel grandfather_clock;
    private static final Map<GrandfatherClockBlock.GrandfatherClockType, Identifier> grandfather_clock_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(GrandfatherClockBlock.Type.OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/oak_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.SPRUCE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/spruce_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.BIRCH, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/birch_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.JUNGLE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/jungle_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.ACACIA, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/acacia_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.DARK_OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/dark_oak_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.MANGROVE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/mangrove_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.CHERRY, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/cherry_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.BAMBOO, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/bamboo_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.CRIMSON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/crimson_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.WARPED, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/warped_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.PRINCESS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/princess_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.IRON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/iron_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.GLASS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/glass_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.UNDEAD, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/undead_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.OMINOUS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_inactive.png"));
    });

    // How far does this block render.
    @Override
    public int getViewDistance() {
        return 64;
    }

    public GrandfatherClockBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.grandfather_clock = new GrandfatherClockModel(ctx.bakeLayer(ModEntityModelLayers.GRANDFATHER_CLOCK));
    }

    @Override
    public GrandfatherClockRenderState createRenderState() {
        return new GrandfatherClockRenderState();
    }

    @Override
    public void extractRenderState(GrandfatherClockBlockEntity entity, GrandfatherClockRenderState state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        BlockState blockState = entity.getBlockState();
        state.top = blockState.getValue(ModProperties.TRIPLE_TALL_BLOCK) == TripleTallBlock.TOP;
        if (!state.top) return;

        state.rotationDegrees = ModProperties.setSeatRotationFromRotation(blockState);
        GrandfatherClockBlock.GrandfatherClockType clockType = ((GrandfatherClockBlock) blockState.getBlock()).getGrandfatherClockType();
        state.texture = getGrandfatherClockTexture(clockType, blockState);

        // Interpolate angles for smooth rendering (converted to radians)
        state.hourHandAngle = Mth.lerp(tickDelta, entity.lastHourHandAngle, entity.currentHourHandAngle) * ((float) Math.PI / 180.0f);
        state.minuteHandAngle = Mth.lerp(tickDelta, entity.lastMinuteHandAngle, entity.currentMinuteHandAngle) * ((float) Math.PI / 180.0f);
        state.pendulumAngle = Mth.lerp(tickDelta, entity.lastPendulumAngle, entity.currentPendulumAngle) * ((float) Math.PI / 180.0f);
    }

    @Override
    public void submit(GrandfatherClockRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.top) return;
        matrices.pushPose();
        matrices.translate(0.5, -0.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.rotationDegrees));
        collector.submitModel(this.grandfather_clock, state, matrices, RenderTypes.entityCutoutZOffset(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, state.breakProgress);
        matrices.popPose();
    }

    public static Identifier getGrandfatherClockTexture(GrandfatherClockBlock.GrandfatherClockType type, BlockState blockState) {
        Identifier identifier;

        if (type == GrandfatherClockBlock.Type.OMINOUS) {
            // If the grandfather_clock type is TRIAL and a player is detected
            if (blockState.getValue(BlockStateProperties.TRIGGERED)) {
                identifier = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_active.png");
            } else {
                identifier = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_inactive.png");
            }
        } else {
            // If the grandfather_clock type is not TRIAL, get the identifier from the texture map
            identifier = grandfather_clock_TEXTURES.get(type);
        }
        return identifier;
    }
}
