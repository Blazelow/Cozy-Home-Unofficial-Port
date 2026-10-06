package net.luckystudio.cozyhome.block.custom.seatable.sofas;
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
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.util.ModColorHandler;
import java.util.Map;
public class SofaBlockEntityRenderer implements BlockEntityRenderer<SofaBlockEntity, SofaBlockEntityRenderer.State> {
    private final ModelPart sofa;
    private final ModelPart cushion;
    private static final Map<SofaBlock.SofaType, Identifier> SOFA_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(SofaBlock.Type.OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/oak_sofa.png"));
        map.put(SofaBlock.Type.SPRUCE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/spruce_sofa.png"));
        map.put(SofaBlock.Type.BIRCH, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/birch_sofa.png"));
        map.put(SofaBlock.Type.JUNGLE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/jungle_sofa.png"));
        map.put(SofaBlock.Type.ACACIA, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/acacia_sofa.png"));
        map.put(SofaBlock.Type.DARK_OAK, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/dark_oak_sofa.png"));
        map.put(SofaBlock.Type.MANGROVE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/mangrove_sofa.png"));
        map.put(SofaBlock.Type.CHERRY, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/cherry_sofa.png"));
        map.put(SofaBlock.Type.BAMBOO, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/bamboo_sofa.png"));
        map.put(SofaBlock.Type.CRIMSON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/crimson_sofa.png"));
        map.put(SofaBlock.Type.WARPED, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/warped_sofa.png"));
        map.put(SofaBlock.Type.QUARTZ, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/quartz_sofa.png"));
        map.put(SofaBlock.Type.IRON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/iron_sofa.png"));
        map.put(SofaBlock.Type.GLASS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/glass_sofa.png"));
        map.put(SofaBlock.Type.UNDEAD, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/undead_sofa.png"));
        map.put(SofaBlock.Type.OMINOUS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/ominous_sofa_inactive.png"));
    });

    public static Identifier getSofaTexture(SofaBlock.SofaType type) {
        return SOFA_TEXTURES.get(type);
    }

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

    public SofaBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.sofa = ctx.bakeLayer(ModEntityModelLayers.SOFA);
        this.cushion = ctx.bakeLayer(ModEntityModelLayers.SOFA_CUSHION);
    }

    public static class State extends BlockEntityRenderState {
        public float rotationDegrees;
        public int color;
        public Identifier sofaTexture;
        public Identifier cushionTexture;
        public int cushionColor;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(SofaBlockEntity entity, State state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        BlockState blockState = entity.getBlockState();
        state.color = ModColorHandler.getBlockColor(entity, -17170434);
        state.rotationDegrees = ModProperties.setSeatRotationFromRotation(blockState);
        state.sofaTexture = SOFA_TEXTURES.get(((SofaBlock) blockState.getBlock()).getSofaType());
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
        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.rotationDegrees));

        // Render the frame (uncolored part)
        collector.submitModelPart(this.sofa.getChild("frame"), matrices, RenderTypes.entityCutoutZOffset(state.sofaTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress, 0);

        // Render the dyeable part
        collector.submitModelPart(this.sofa.getChild("dyeable"), matrices, RenderTypes.entityCutout(state.sofaTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, state.color, state.breakProgress, 0);

        if (state.cushionTexture != null) {
            collector.submitModelPart(cushion, matrices, RenderTypes.entityCutoutZOffset(state.cushionTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, state.cushionColor, state.breakProgress, 0);
        }

        matrices.popPose();
    }

}
