package net.luckystudio.cozyhome.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

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
        ClientRegistrations.mirrorScreen();
        MenuScreens.register(ModMenuTypes.STORAGE_COUNTER_SCREEN_HANDLER, StorageCounterScreen::new);
        MenuScreens.register(ModMenuTypes.DRAWER_SCREEN_HANDLER, DrawerScreen::new);

        ClientRegistrations.layerDefinitions((layer, definition) -> ModelLayerRegistry.registerModelLayer(layer, definition::get));

        EntityRenderers.register(ModEntities.SEAT_ENTITY, SeatRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, TelescopeBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.ITEM_RACK_BLOCK_ENTITY, ToolRackBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, SofaBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, CouchBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, ChairBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntityRenderer::new);

        ClientRegistrations.specialModelRenderers((id, codec) -> SpecialModelRenderers.ID_MAPPER.put(id, codec));

        BlockColorRegistry.register(ModRenderLayers.containsTintSources(), ModRenderLayers.containsBlocks());
        BlockColorRegistry.register(ModRenderLayers.dyedTintSources(), ModRenderLayers.dyedBlocks());
    }
}
