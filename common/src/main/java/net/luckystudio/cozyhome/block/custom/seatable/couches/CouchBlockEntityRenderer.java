package net.luckystudio.cozyhome.block.custom.seatable.couches;
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
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import java.util.Map;
public class CouchBlockEntityRenderer implements BlockEntityRenderer<CouchBlockEntity, CouchBlockEntityRenderer.State> {
    private final ModelPart cushion;

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

    public CouchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.cushion = ctx.bakeLayer(ModEntityModelLayers.COUCH_CUSHION);
    }

    public static class State extends BlockEntityRenderState {
        public float rotationDegrees;
        public Identifier cushionTexture;
        public int cushionColor;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CouchBlockEntity entity, State state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        state.rotationDegrees = ModProperties.setSeatRotationFromShape(entity.getBlockState()) + 180;
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
        if (state.cushionTexture == null) return;
        matrices.pushPose();
        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(state.rotationDegrees));
        collector.submitModelPart(cushion, matrices, RenderTypes.entityCutoutZOffset(state.cushionTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, state.cushionColor, state.breakProgress, 0);
        matrices.popPose();
    }

}
