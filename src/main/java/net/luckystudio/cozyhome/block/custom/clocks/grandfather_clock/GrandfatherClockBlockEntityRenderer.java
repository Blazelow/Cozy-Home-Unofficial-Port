package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.TripleTallBlock;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
public class GrandfatherClockBlockEntityRenderer implements BlockEntityRenderer<GrandfatherClockBlockEntity> {
    private final GrandfatherClockModel grandfather_clock;
    private static final Map<GrandfatherClockBlock.GrandfatherClockType, ResourceLocation> grandfather_clock_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(GrandfatherClockBlock.Type.OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/oak_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.SPRUCE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/spruce_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.BIRCH, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/birch_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.JUNGLE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/jungle_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.ACACIA, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/acacia_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.DARK_OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/dark_oak_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.MANGROVE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/mangrove_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.CHERRY, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/cherry_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.BAMBOO, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/bamboo_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.CRIMSON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/crimson_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.WARPED, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/warped_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.PRINCESS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/princess_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.IRON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/iron_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.GLASS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/glass_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.UNDEAD, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/undead_grandfather_clock.png"));
        map.put(GrandfatherClockBlock.Type.OMINOUS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_inactive.png"));
    });

    // How far does this block render.
    @Override
    public int getRenderDistance() {
        return 64;
    }

    public GrandfatherClockBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.grandfather_clock = new GrandfatherClockModel(ctx.getLayerModelPart(ModEntityModelLayers.GRANDFATHER_CLOCK));
    }

    @Override
    public void render(GrandfatherClockBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getBlockState();

        if (blockState.getValue(ModProperties.TRIPLE_TALL_BLOCK) == TripleTallBlock.TOP) {
            matrices.pushPose();
            matrices.translate(0.5, -0.5, 0.5);
            matrices.mulPose(Axis.XP.rotationDegrees(180));
            matrices.mulPose(Axis.YP.rotationDegrees(ModProperties.setSeatRotationFromRotation(entity.getBlockState())));

            GrandfatherClockBlock.GrandfatherClockType clockType = ((GrandfatherClockBlock) blockState.getBlock()).getGrandfatherClockType();

            // Interpolate angles for smooth rendering
            float interpolatedHourAngle = Mth.lerp(tickDelta, entity.lastHourHandAngle, entity.currentHourHandAngle);
            float interpolatedMinuteAngle = Mth.lerp(tickDelta, entity.lastMinuteHandAngle, entity.currentMinuteHandAngle);
            float interpolatedPendulumAngle = Mth.lerp(tickDelta, entity.lastPendulumAngle, entity.currentPendulumAngle);

            // Set angles in the model
            this.grandfather_clock.setAngles(
                    interpolatedHourAngle * ((float) Math.PI / 180.0f),  // Hour hand (radians)
                    interpolatedMinuteAngle * ((float) Math.PI / 180.0f), // Minute hand (radians)
                    interpolatedPendulumAngle * ((float) Math.PI / 180.0f) // Pendulum swing (radians)
            );

            // Render the clock
            RenderType clockRenderLayer = getGrandfatherClockRenderLayer(clockType, blockState);
            VertexConsumer clockVertexConsumer = vertexConsumers.getBuffer(clockRenderLayer);
            grandfather_clock.render(matrices, clockVertexConsumer, light, overlay);
            matrices.popPose();
        }
    }




    public static RenderType getGrandfatherClockRenderLayer(GrandfatherClockBlock.GrandfatherClockType type, BlockState blockState) {
        ResourceLocation identifier;

        if (type == GrandfatherClockBlock.Type.OMINOUS) {
            // If the grandfather_clock type is TRIAL and a player is detected
            if (blockState.getValue(BlockStateProperties.TRIGGERED)) {
                identifier = ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_active.png");
            } else {
                identifier = ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/grandfather_clock/ominous_grandfather_clock_inactive.png");
            }
        } else {
            // If the grandfather_clock type is not TRIAL, get the identifier from the texture map
            identifier = grandfather_clock_TEXTURES.get(type);
        }
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }
}
