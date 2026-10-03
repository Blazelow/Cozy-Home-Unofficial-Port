package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
public class WallClockBlockEntityRenderer implements BlockEntityRenderer<WallClockBlockEntity> {
    private final WallClockModel wall_clock;
    private static final Map<WallClockBlock.ClockType, ResourceLocation> grandfather_clock_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(WallClockBlock.Type.OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/oak_wall_clock.png"));
        map.put(WallClockBlock.Type.SPRUCE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/spruce_wall_clock.png"));
        map.put(WallClockBlock.Type.BIRCH, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/birch_wall_clock.png"));
        map.put(WallClockBlock.Type.JUNGLE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/jungle_wall_clock.png"));
        map.put(WallClockBlock.Type.ACACIA, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/acacia_wall_clock.png"));
        map.put(WallClockBlock.Type.DARK_OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/dark_oak_wall_clock.png"));
        map.put(WallClockBlock.Type.MANGROVE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/mangrove_wall_clock.png"));
        map.put(WallClockBlock.Type.CHERRY, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/cherry_wall_clock.png"));
        map.put(WallClockBlock.Type.BAMBOO, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/bamboo_wall_clock.png"));
        map.put(WallClockBlock.Type.CRIMSON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/crimson_wall_clock.png"));
        map.put(WallClockBlock.Type.WARPED, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/warped_wall_clock.png"));
        map.put(WallClockBlock.Type.PRINCESS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/princess_wall_clock.png"));
        map.put(WallClockBlock.Type.IRON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/iron_wall_clock.png"));
        map.put(WallClockBlock.Type.GLASS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/glass_wall_clock.png"));
        map.put(WallClockBlock.Type.UNDEAD, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/undead_wall_clock.png"));
        map.put(WallClockBlock.Type.OMINOUS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/wall_clock/ominous_wall_clock_inactive.png"));
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
    public void render(WallClockBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getBlockState();
        matrices.pushPose();
        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(ModProperties.setSeatRotationFromFacing(entity.getBlockState())));
        WallClockBlock.ClockType clockType = ((WallClockBlock) blockState.getBlock()).getClockType();

        // Interpolate angles for smooth rendering
        float interpolatedHourAngle = Mth.lerp(tickDelta, entity.lastHourHandAngle, entity.currentHourHandAngle);
        float interpolatedMinuteAngle = Mth.lerp(tickDelta, entity.lastMinuteHandAngle, entity.currentMinuteHandAngle);

        // Set angles in the model
        this.wall_clock.setAngles(
                interpolatedHourAngle * ((float) Math.PI / 180.0f),  // Hour hand (radians)
                interpolatedMinuteAngle * ((float) Math.PI / 180.0f) // Minute hand (radians)
        );

        // Render the clock
        RenderType clockRenderLayer = getClockRenderLayer(clockType);
        VertexConsumer clockVertexConsumer = vertexConsumers.getBuffer(clockRenderLayer);
        wall_clock.renderToBuffer(matrices, clockVertexConsumer, light, overlay, -1);
        matrices.popPose();
    }

    public static RenderType getClockRenderLayer(WallClockBlock.ClockType type) {
        ResourceLocation identifier = grandfather_clock_TEXTURES.get(type);
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }
}
