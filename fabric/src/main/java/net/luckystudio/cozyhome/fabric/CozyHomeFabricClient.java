package net.luckystudio.cozyhome.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreen;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreen;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.toolrack.ToolRackBlockEntityRenderer;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModMenuTypes;
import net.luckystudio.cozyhome.client.ClientRegistrations;
import net.luckystudio.cozyhome.client.ModRenderLayers;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.entity.custom.SeatRenderer;

public class CozyHomeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.STORAGE_COUNTER_SCREEN_HANDLER, StorageCounterScreen::new);
        MenuScreens.register(ModMenuTypes.DRAWER_SCREEN_HANDLER, DrawerScreen::new);

        ClientRegistrations.mirrorScreen();
        ClientRegistrations.layerDefinitions((layer, definition) -> EntityModelLayerRegistry.registerModelLayer(layer, definition::get));

        EntityRendererRegistry.register(ModEntities.SEAT_ENTITY, SeatRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, TelescopeBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.ITEM_RACK_BLOCK_ENTITY, ToolRackBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, SofaBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, CouchBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, ChairBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModRenderLayers.cutoutBlocks());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), ModRenderLayers.translucentBlocks());

        ColorProviderRegistry.BLOCK.register(ModRenderLayers::containsColor, ModRenderLayers.containsBlocks());
        ColorProviderRegistry.BLOCK.register(ModRenderLayers::dyedBlockColor, ModRenderLayers.dyedBlocks());
        ColorProviderRegistry.ITEM.register(ModRenderLayers::dyedItemColor, ModRenderLayers.dyedItems());

        // Items that are drawn by a block entity model instead of a regular item model
        ClientRegistrations.itemRenderers((renderer, items) -> {
            BlockEntityWithoutLevelRenderer[] instance = new BlockEntityWithoutLevelRenderer[1];
            for (Item item : items) {
                BuiltinItemRendererRegistry.INSTANCE.register(item, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
                    if (instance[0] == null) instance[0] = renderer.get();
                    instance[0].renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                });
            }
        });
    }
}
