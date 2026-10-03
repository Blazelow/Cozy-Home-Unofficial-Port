package net.luckystudio.cozyhome.block.custom.seatable.chairs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import java.util.Map;
public class ChairBlockEntityRenderer implements BlockEntityRenderer<ChairBlockEntity, ChairBlockEntityRenderer.State> {
    private final ModelPart chair;
    private final ModelPart cushion;
    private static final Map<ChairBlock.ChairType, Identifier> CHAIR_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(ChairBlock.Type.OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/oak_chair.png"));
        map.put(ChairBlock.Type.SPRUCE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/spruce_chair.png"));
        map.put(ChairBlock.Type.BIRCH, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/birch_chair.png"));
        map.put(ChairBlock.Type.JUNGLE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/jungle_chair.png"));
        map.put(ChairBlock.Type.ACACIA, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/acacia_chair.png"));
        map.put(ChairBlock.Type.DARK_OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/dark_oak_chair.png"));
        map.put(ChairBlock.Type.MANGROVE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/mangrove_chair.png"));
        map.put(ChairBlock.Type.CHERRY, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/cherry_chair.png"));
        map.put(ChairBlock.Type.BAMBOO, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/bamboo_chair.png"));
        map.put(ChairBlock.Type.CRIMSON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/crimson_chair.png"));
        map.put(ChairBlock.Type.WARPED, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/warped_chair.png"));
        map.put(ChairBlock.Type.PRINCESS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/princess_chair.png"));
        map.put(ChairBlock.Type.IRON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/iron_chair.png"));
        map.put(ChairBlock.Type.GLASS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/glass_chair.png"));
        map.put(ChairBlock.Type.UNDEAD, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/undead_chair.png"));
        map.put(ChairBlock.Type.OMINOUS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_inactive.png"));
    });

    private static final Map<Item, Identifier> CUSHION_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(ModItems.CUSHION, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/cushion.png"));
        map.put(ModItems.HAY_CUSHION, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/hay_cushion.png"));
        map.put(ModItems.TRADER_CUSHION, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/trader_cushion.png"));
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

    public static class State extends BlockEntityRenderState {
        public double x, y, z;
        public float rotationDegrees;
        public Identifier chairTexture;
        public Identifier cushionTexture;
        public int cushionColor;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ChairBlockEntity entity, State state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        BlockState blockState = entity.getBlockState();

        // Update position based on the `tucked` state of this chair
        handleSlide(entity, tickDelta);
        state.x = 0.5;
        state.y = 1.5;
        state.z = 0.5;
        if (canTuck(entity)) {
            switch (blockState.getValue(ChairBlock.ROTATION)) {
                case 0 -> state.z = 0.5 - entity.currentOffset;
                case 4 -> state.x = 0.5 + entity.currentOffset;
                case 8 -> state.z = 0.5 + entity.currentOffset;
                case 12 -> state.x = 0.5 - entity.currentOffset;
            }
        }
        state.rotationDegrees = ModProperties.setSeatRotationFromRotation(blockState);

        ChairBlock.ChairType chairType = ((ChairBlock) blockState.getBlock()).getChairType();
        state.chairTexture = getChairTexture(chairType, blockState);

        if (!entity.isEmpty() && entity.getTheItem().getItem() instanceof CushionItem) {
            Item item = entity.getTheItem().getItem();
            state.cushionColor = DyedItemColor.getOrDefault(entity.getTheItem(), -17170434);
            state.cushionTexture = CUSHION_TEXTURES.get(item);
        } else {
            state.cushionTexture = null;
        }
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState camera) {
        matrices.pushPose();
        matrices.translate(state.x, state.y, state.z);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.rotationDegrees));

        collector.submitModelPart(chair, matrices, RenderTypes.entityCutoutZOffset(state.chairTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, false, false, -1, state.breakProgress, 0);
        if (state.cushionTexture != null) {
            collector.submitModelPart(cushion, matrices, RenderTypes.entityCutoutZOffset(state.cushionTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, false, false, state.cushionColor, state.breakProgress, 0);
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

    public static Identifier getChairTexture(ChairBlock.ChairType type, BlockState blockState) {
        Identifier identifier;

        if (type == ChairBlock.Type.OMINOUS) {
            // If the chair type is ominous and a player is detected
            if (blockState.getValue(BlockStateProperties.TRIGGERED)) {
                identifier = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_active.png");
            } else {
                identifier = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/chair/ominous_chair_inactive.png");
            }
        } else {
            // If the chair type is not ominous, get the identifier from the texture map
            identifier = CHAIR_TEXTURES.get(type);
        }
        return identifier;
    }
}
