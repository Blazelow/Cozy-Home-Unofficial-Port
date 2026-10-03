package net.luckystudio.cozyhome.block.custom.seatable.couches;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
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
public class CouchBlockEntityRenderer implements BlockEntityRenderer<CouchBlockEntity> {
    private final ModelPart cushion;

    private static final Map<Item, ResourceLocation> CUSHION_TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(ModItems.CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/cushion.png"));
        map.put(ModItems.HAY_CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/hay_cushion.png"));
        map.put(ModItems.TRADER_CUSHION, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/cushion/trader_cushion.png"));
    });

    // How far does this block render.
    @Override
    public int getRenderDistance() {
        return 64;
    }

    public CouchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.cushion = ctx.getLayerModelPart(ModEntityModelLayers.COUCH_CUSHION);
    }

    @Override
    public void render(CouchBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();

        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(ModProperties.setSeatRotationFromShape(entity.getBlockState()) + 180));

        // Update position based on the `tucked` state of this couch
        if (!entity.isEmpty() && entity.getStack().getItem() instanceof CushionItem) {
            Item item = entity.getStack().getItem();
            int colorItem = DyedItemColor.getColor(entity.getStack(), -17170434);
            RenderType cushionRenderLayer = getCushionRenderLayer(item);
            VertexConsumer cushionVertexConsumer = vertexConsumers.getBuffer(cushionRenderLayer);
            cushion.render(matrices, cushionVertexConsumer, light, overlay, colorItem);
        }

        matrices.popPose();
    }

    public static RenderType getCushionRenderLayer(Item item) {
        ResourceLocation identifier = CUSHION_TEXTURES.get(item);
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }
}
