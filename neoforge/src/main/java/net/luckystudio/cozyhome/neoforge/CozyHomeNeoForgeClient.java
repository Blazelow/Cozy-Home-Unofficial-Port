package net.luckystudio.cozyhome.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

import net.luckystudio.cozyhome.CozyHome;
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

@Mod(value = CozyHome.MOD_ID, dist = Dist.CLIENT)
public class CozyHomeNeoForgeClient {

    public CozyHomeNeoForgeClient(IEventBus modEventBus) {
        ClientRegistrations.mirrorScreen();
        modEventBus.addListener(CozyHomeNeoForgeClient::registerScreens);
        modEventBus.addListener(CozyHomeNeoForgeClient::registerLayerDefinitions);
        modEventBus.addListener(CozyHomeNeoForgeClient::registerRenderers);
        modEventBus.addListener(CozyHomeNeoForgeClient::registerSpecialModelRenderers);
        modEventBus.addListener(CozyHomeNeoForgeClient::registerBlockTintSources);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.STORAGE_COUNTER_SCREEN_HANDLER, StorageCounterScreen::new);
        event.register(ModMenuTypes.DRAWER_SCREEN_HANDLER, DrawerScreen::new);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegistrations.layerDefinitions(event::registerLayerDefinition);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SEAT_ENTITY, SeatRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, TelescopeBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.ITEM_RACK_BLOCK_ENTITY, ToolRackBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, SofaBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, CouchBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, ChairBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntityRenderer::new);
    }

    // Items that are drawn by code instead of by a regular item model
    private static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        ClientRegistrations.specialModelRenderers(event::register);
    }

    private static void registerBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(ModRenderLayers.containsTintSources(), ModRenderLayers.containsBlocks());
        event.register(ModRenderLayers.dyedTintSources(), ModRenderLayers.dyedBlocks());
    }
}
