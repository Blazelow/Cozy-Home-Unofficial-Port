package net.luckystudio.cozyhome.block.custom.seatable.chairs;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
public class ChairBlockEntityRenderer implements BlockEntityRenderer<ChairBlockEntity> {
    private final ModelPart chair;
    private final ModelPart cushion;
    private static final Map<ChairBlock.ChairType, ResourceLocation> CHAIR_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(ChairBlock.Type.OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/oak_chair.png"));
        map.put(ChairBlock.Type.SPRUCE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/spruce_chair.png"));
        map.put(ChairBlock.Type.BIRCH, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/birch_chair.png"));
        map.put(ChairBlock.Type.JUNGLE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/jungle_chair.png"));
        map.put(ChairBlock.Type.ACACIA, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/acacia_chair.png"));
        map.put(ChairBlock.Type.DARK_OAK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/dark_oak_chair.png"));
        map.put(ChairBlock.Type.MANGROVE, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/mangrove_chair.png"));
        map.put(ChairBlock.Type.CHERRY, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/cherry_chair.png"));
        map.put(ChairBlock.Type.BAMBOO, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/bamboo_chair.png"));
        map.put(ChairBlock.Type.CRIMSON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/crimson_chair.png"));
        map.put(ChairBlock.Type.WARPED, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/warped_chair.png"));
        map.put(ChairBlock.Type.QUARTZ, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/quartz_chair.png"));
        map.put(ChairBlock.Type.IRON, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/iron_chair.png"));
        map.put(ChairBlock.Type.GLASS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/glass_chair.png"));
        map.put(ChairBlock.Type.UNDEAD, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/undead_chair.png"));
        map.put(ChairBlock.Type.OMINOUS, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_inactive.png"));
    });

    private static final Map<Item, ResourceLocation> CUSHION_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(ModItems.CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/cushion.png"));
        map.put(ModItems.HAY_CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/hay_cushion.png"));
        map.put(ModItems.TRADER_CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/trader_cushion.png"));
    });

    // How far does this block render.
    @Override
    public int getViewDistance() {
        return 64;
    }

    public ChairBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.chair = ctx.bakeLayer(ModEntityModelLayers.CHAIR);
        this.cushion = ctx.bakeLayer(ModEntityModelLayers.CUSHION);
    }

    @Override
    public void render(ChairBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        // Update position based on the `tucked` state of this chair
        handleSlide(entity, tickDelta);
        if (canTuck(entity)) {
            getLocationForTuck(entity, matrices);
        } else {
            matrices.translate(0.5, 1.5, 0.5);
        }
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(ModProperties.setSeatRotationFromRotation(entity.getBlockState())));

        BlockState blockState = entity.getBlockState();
        ChairBlock.ChairType chairType = ((ChairBlock)blockState.getBlock()).getChairType();

        RenderType chairRenderLayer = getChairRenderLayer(chairType, blockState);
        VertexConsumer chairVertexConsumer = vertexConsumers.getBuffer(chairRenderLayer);
        chair.render(matrices, chairVertexConsumer, light, overlay);

        if (!entity.isEmpty() && entity.getTheItem().getItem() instanceof CushionItem) {
            Item item = entity.getTheItem().getItem();
            int color = DyedItemColor.getOrDefault(entity.getTheItem(), -17170434);
            RenderType cushionRenderLayer = getCushionRenderLayer(item);
            VertexConsumer cushionVertexConsumer = vertexConsumers.getBuffer(cushionRenderLayer);
            cushion.render(matrices, cushionVertexConsumer, light, overlay, color);
        }

        matrices.popPose();
    }

    private boolean canTuck(ChairBlockEntity entity) {
        int rot = entity.getBlockState().getValue(ChairBlock.ROTATION);
        return rot == 0 || rot == 4 || rot == 8 || rot == 12;
    }

    private void handleSlide(ChairBlockEntity entity, float tickDelta) {
        // Update position based on the `tucked` state of this chair
        if (entity.getBlockState().getValue(ChairBlock.TUCKED)) {
            // Smoothly transition to the tucked offset
            entity.currentOffset = Math.min(entity.currentOffset + 0.1f * tickDelta, 0.625f);
        } else {
            // Smoothly transition back to the original position
            entity.currentOffset = Math.max(entity.currentOffset - 0.1f * tickDelta, 0.0f);
        }
    }

    private void getLocationForTuck(ChairBlockEntity entity, PoseStack matrices) {
        if (entity.getBlockState().getValue(ChairBlock.ROTATION) == 0) {
            matrices.translate(0.5, 1.5, 0.5 - entity.currentOffset); // Apply offset here
        }
        if (entity.getBlockState().getValue(ChairBlock.ROTATION) == 4) {
            matrices.translate(0.5 + entity.currentOffset, 1.5, 0.5); // Apply offset here
        }
        if (entity.getBlockState().getValue(ChairBlock.ROTATION) == 8) {
            matrices.translate(0.5, 1.5, 0.5 + entity.currentOffset); // Apply offset here
        }
        if (entity.getBlockState().getValue(ChairBlock.ROTATION) == 12) {
            matrices.translate(0.5 - entity.currentOffset, 1.5, 0.5); // Apply offset here
        }
    }

    public static RenderType getChairRenderLayer(ChairBlock.ChairType type, BlockState blockState) {
        ResourceLocation identifier;

        if (type == ChairBlock.Type.OMINOUS) {
            // If the chair type is ominous and a player is detected
            if (blockState.getValue(BlockStateProperties.TRIGGERED)) {
                identifier = ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_active.png");
            } else {
                identifier = ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_inactive.png");
            }
        } else {
            // If the chair type is not ominous, get the identifier from the texture map
            identifier = CHAIR_TEXTURES.get(type);
        }
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }

    public static RenderType getCushionRenderLayer(Item item) {
        ResourceLocation identifier = CUSHION_TEXTURES.get(item);
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }
}
