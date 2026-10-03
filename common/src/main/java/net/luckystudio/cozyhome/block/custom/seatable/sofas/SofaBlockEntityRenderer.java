package net.luckystudio.cozyhome.block.custom.seatable.sofas;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.state.BlockState;

import com.google.common.collect.Maps;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.util.ModColorHandler;
import java.util.Map;
public class SofaBlockEntityRenderer implements BlockEntityRenderer<SofaBlockEntity> {
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
        map.put(SofaBlock.Type.PRINCESS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/princess_sofa.png"));
        map.put(SofaBlock.Type.IRON, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/iron_sofa.png"));
        map.put(SofaBlock.Type.GLASS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/glass_sofa.png"));
        map.put(SofaBlock.Type.UNDEAD, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/undead_sofa.png"));
        map.put(SofaBlock.Type.OMINOUS, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "textures/block/sofa/ominous_sofa_inactive.png"));
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

    public SofaBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // Use a custom model layer (make sure to register it in the client mod initializer)
        this.sofa = ctx.bakeLayer(ModEntityModelLayers.SOFA);
        this.cushion = ctx.bakeLayer(ModEntityModelLayers.SOFA_CUSHION);
    }

    @Override
    public void render(SofaBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        // Update position based on the `tucked` state of this sofa

        int color = ModColorHandler.getBlockColor(entity, -17170434);

        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.mulPose(Axis.YP.rotationDegrees(ModProperties.setSeatRotationFromRotation(entity.getBlockState())));

        BlockState blockState = entity.getBlockState();
        SofaBlock.SofaType sofaType = ((SofaBlock)blockState.getBlock()).getSofaType();

        // Render the frame (uncolored part)
        RenderType sofaRenderLayer = getSofaRenderLayer(sofaType);
        VertexConsumer frameVertexConsumer = vertexConsumers.getBuffer(sofaRenderLayer);
        this.sofa.getChild("frame").render(matrices, frameVertexConsumer, light, overlay);

        // Render the dyeable part with a default color if no color is set
        VertexConsumer dyeableVertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutout(SOFA_TEXTURES.get(sofaType)));
        this.sofa.getChild("dyeable").render(matrices, dyeableVertexConsumer, light, overlay, color);

        if (!entity.isEmpty() && entity.getTheItem().getItem() instanceof CushionItem) {
            Item item = entity.getTheItem().getItem();
            int colorItem = DyedItemColor.getOrDefault(entity.getTheItem(), -17170434);
            RenderType cushionRenderLayer = getCushionRenderLayer(item);
            VertexConsumer cushionVertexConsumer = vertexConsumers.getBuffer(cushionRenderLayer);
            cushion.render(matrices, cushionVertexConsumer, light, overlay, colorItem);
        }

        matrices.popPose();
    }

    public static RenderType getSofaRenderLayer(SofaBlock.SofaType type) {
        Identifier identifier = SOFA_TEXTURES.get(type);
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }

    public static RenderType getCushionRenderLayer(Item item) {
        Identifier identifier = CUSHION_TEXTURES.get(item);
        return RenderType.entityCutoutNoCullZOffset(identifier);
    }
}
