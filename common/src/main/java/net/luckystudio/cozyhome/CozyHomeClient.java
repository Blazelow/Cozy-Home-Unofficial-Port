package net.luckystudio.cozyhome;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockModel;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreen;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreen;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeModel;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModMenuTypes;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.client.ModRenderLayers;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.entity.custom.SeatRenderer;
import net.luckystudio.cozyhome.entity.model.CushionModel;
import net.luckystudio.cozyhome.entity.model.SeatEntityModel;
import net.luckystudio.cozyhome.item.renderer.BathtubItemRenderer;
import net.luckystudio.cozyhome.item.renderer.ChairItemRenderer;
import net.luckystudio.cozyhome.item.renderer.SofaItemRenderer;
import net.luckystudio.cozyhome.item.renderer.WallClockItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@Mod(value = CozyHome.MOD_ID, dist = Dist.CLIENT)
public class CozyHomeClient {

    public CozyHomeClient(IEventBus modEventBus) {
        modEventBus.addListener(CozyHomeClient::registerScreens);
        modEventBus.addListener(CozyHomeClient::registerLayerDefinitions);
        modEventBus.addListener(CozyHomeClient::registerRenderers);
        modEventBus.addListener(CozyHomeClient::registerClientExtensions);
        modEventBus.addListener(CozyHomeClient::clientSetup);
        modEventBus.addListener(ModRenderLayers::registerBlockColors);
        modEventBus.addListener(ModRenderLayers::registerItemColors);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ModRenderLayers::registerBlockRenderLayers);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.STORAGE_COUNTER_SCREEN_HANDLER, StorageCounterScreen::new);
        event.register(ModMenuTypes.DRAWER_SCREEN_HANDLER, DrawerScreen::new);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityModelLayers.SEAT, SeatEntityModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.TELESCOPE, TelescopeModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.SOFA, SofaModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.SOFA_CUSHION, SofaCushionModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.COUCH_CUSHION, CouchCushionModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.CHAIR, ChairModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.CUSHION, CushionModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.GRANDFATHER_CLOCK, GrandfatherClockModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityModelLayers.WALL_CLOCK, WallClockModel::getTexturedModelData);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SEAT_ENTITY, SeatRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, TelescopeBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, SofaBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, CouchBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, ChairBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntityRenderer::new);
    }

    // Items that are drawn by a block entity model instead of a regular item model
    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new ChairItemRenderer();
                return renderer;
            }
        }, 
                ModBlocks.OAK_CHAIR.asItem(),
                ModBlocks.SPRUCE_CHAIR.asItem(),
                ModBlocks.BIRCH_CHAIR.asItem(),
                ModBlocks.JUNGLE_CHAIR.asItem(),
                ModBlocks.ACACIA_CHAIR.asItem(),
                ModBlocks.DARK_OAK_CHAIR.asItem(),
                ModBlocks.MANGROVE_CHAIR.asItem(),
                ModBlocks.CHERRY_CHAIR.asItem(),
                ModBlocks.BAMBOO_CHAIR.asItem(),
                ModBlocks.CRIMSON_CHAIR.asItem(),
                ModBlocks.WARPED_CHAIR.asItem(),
                ModBlocks.IRON_CHAIR.asItem(),
                ModBlocks.GLASS_CHAIR.asItem(),
                ModBlocks.UNDEAD_CHAIR.asItem(),
                ModBlocks.OMINOUS_CHAIR.asItem());

        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new SofaItemRenderer();
                return renderer;
            }
        }, 
                ModBlocks.OAK_SOFA.asItem(),
                ModBlocks.SPRUCE_SOFA.asItem(),
                ModBlocks.BIRCH_SOFA.asItem(),
                ModBlocks.JUNGLE_SOFA.asItem(),
                ModBlocks.ACACIA_SOFA.asItem(),
                ModBlocks.DARK_OAK_SOFA.asItem(),
                ModBlocks.MANGROVE_SOFA.asItem(),
                ModBlocks.CHERRY_SOFA.asItem(),
                ModBlocks.BAMBOO_SOFA.asItem(),
                ModBlocks.CRIMSON_SOFA.asItem(),
                ModBlocks.WARPED_SOFA.asItem());

        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new WallClockItemRenderer();
                return renderer;
            }
        }, 
                ModBlocks.OAK_WALL_CLOCK.asItem(),
                ModBlocks.SPRUCE_WALL_CLOCK.asItem(),
                ModBlocks.BIRCH_WALL_CLOCK.asItem(),
                ModBlocks.JUNGLE_WALL_CLOCK.asItem(),
                ModBlocks.ACACIA_WALL_CLOCK.asItem(),
                ModBlocks.DARK_OAK_WALL_CLOCK.asItem(),
                ModBlocks.MANGROVE_WALL_CLOCK.asItem(),
                ModBlocks.CHERRY_WALL_CLOCK.asItem(),
                ModBlocks.BAMBOO_WALL_CLOCK.asItem(),
                ModBlocks.CRIMSON_WALL_CLOCK.asItem(),
                ModBlocks.WARPED_WALL_CLOCK.asItem(),
                ModBlocks.IRON_WALL_CLOCK.asItem(),
                ModBlocks.GLASS_WALL_CLOCK.asItem(),
                ModBlocks.UNDEAD_WALL_CLOCK.asItem(),
                ModBlocks.OMINOUS_WALL_CLOCK.asItem());

        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new BathtubItemRenderer();
                return renderer;
            }
        }, 
                ModBlocks.STONE_BRICK_BATHTUB.asItem(),
                ModBlocks.MOSSY_STONE_BRICK_BATHTUB.asItem(),
                ModBlocks.GRANITE_BATHTUB.asItem(),
                ModBlocks.DIORITE_BATHTUB.asItem(),
                ModBlocks.ANDESITE_BATHTUB.asItem(),
                ModBlocks.DEEPSLATE_BATHTUB.asItem(),
                ModBlocks.CALCITE_BATHTUB.asItem(),
                ModBlocks.TUFF_BATHTUB.asItem(),
                ModBlocks.BRICK_BATHTUB.asItem(),
                ModBlocks.MUD_BATHTUB.asItem(),
                ModBlocks.SANDSTONE_BATHTUB.asItem(),
                ModBlocks.RED_SANDSTONE_BATHTUB.asItem(),
                ModBlocks.PRISMARINE_BATHTUB.asItem(),
                ModBlocks.NETHER_BRICK_BATHTUB.asItem(),
                ModBlocks.RED_NETHER_BRICK_BATHTUB.asItem(),
                ModBlocks.BLACKSTONE_BATHTUB.asItem(),
                ModBlocks.ENDSTONE_BATHTUB.asItem(),
                ModBlocks.PURPUR_BATHTUB.asItem(),
                ModBlocks.IRON_BATHTUB.asItem(),
                ModBlocks.GOLD_BATHTUB.asItem());
    }
}
