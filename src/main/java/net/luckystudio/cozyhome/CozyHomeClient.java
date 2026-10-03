package net.luckystudio.cozyhome;

// FABRIC-IMPORT: net.fabricmc.api.ClientModInitializer;
// FABRIC-IMPORT: net.fabricmc.fabric.api.client.rendering.v1.*;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockModel;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeModel;
import net.luckystudio.cozyhome.client.ModEntityModelLayers;
import net.luckystudio.cozyhome.client.ModRenderLayers;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.entity.custom.SeatRenderer;
import net.luckystudio.cozyhome.entity.model.*;
import net.luckystudio.cozyhome.item.renderer.BathtubItemRenderer;
import net.luckystudio.cozyhome.item.renderer.ChairItemRenderer;
import net.luckystudio.cozyhome.item.renderer.SofaItemRenderer;
import net.luckystudio.cozyhome.item.renderer.WallClockItemRenderer;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreen;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreenHandler;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreen;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreenHandler;
import net.luckystudio.cozyhome.util.ModModelPredicates;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
public class CozyHomeClient implements ClientModInitializer {

    public static final MenuType<StorageCounterScreenHandler> STORAGE_COUNTER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.SCREEN_HANDLER, CozyHome.id("storage_counter"), new MenuType<>(StorageCounterScreenHandler::new, FeatureFlagSet.empty()));

    public static final MenuType<DrawerScreenHandler> DRAWER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.SCREEN_HANDLER, CozyHome.id("drawer"), new MenuType<>(DrawerScreenHandler::new, FeatureFlagSet.empty()));

    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerEntityModelLayers();

        MenuScreens.register(STORAGE_COUNTER_SCREEN_HANDLER, StorageCounterScreen::new);
        MenuScreens.register(DRAWER_SCREEN_HANDLER, DrawerScreen::new);

        EntityRendererRegistry.register(ModEntities.SEAT_ENTITY, SeatRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SEAT, SeatEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.TELESCOPE, TelescopeModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, TelescopeBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SOFA, SofaModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, SofaBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SOFA_CUSHION, SofaCushionModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.COUCH_CUSHION, CouchCushionModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, CouchBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CHAIR, ChairModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, ChairBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CUSHION, CushionModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GRANDFATHER_CLOCK, GrandfatherClockModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.WALL_CLOCK, WallClockModel::getTexturedModelData);
        BlockEntityRenderers.register(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntityRenderer::new);

        ItemLike[] chairItems = {
                ModBlocks.OAK_CHAIR,
                ModBlocks.SPRUCE_CHAIR,
                ModBlocks.BIRCH_CHAIR,
                ModBlocks.JUNGLE_CHAIR,
                ModBlocks.ACACIA_CHAIR,
                ModBlocks.DARK_OAK_CHAIR,
                ModBlocks.MANGROVE_CHAIR,
                ModBlocks.CHERRY_CHAIR,
                ModBlocks.BAMBOO_CHAIR,
                ModBlocks.CRIMSON_CHAIR,
                ModBlocks.WARPED_CHAIR,
                ModBlocks.IRON_CHAIR,
                ModBlocks.GLASS_CHAIR,
                ModBlocks.UNDEAD_CHAIR,
                ModBlocks.OMINOUS_CHAIR
        };
        for (ItemLike chair : chairItems) {
            BuiltinItemRendererRegistry.INSTANCE.register(chair, new ChairItemRenderer());
        }

        ItemLike[] sofaItems = {
                ModBlocks.OAK_SOFA,
                ModBlocks.SPRUCE_SOFA,
                ModBlocks.BIRCH_SOFA,
                ModBlocks.JUNGLE_SOFA,
                ModBlocks.ACACIA_SOFA,
                ModBlocks.DARK_OAK_SOFA,
                ModBlocks.MANGROVE_SOFA,
                ModBlocks.CHERRY_SOFA,
                ModBlocks.BAMBOO_SOFA,
                ModBlocks.CRIMSON_SOFA,
                ModBlocks.WARPED_SOFA
        };
        for (ItemLike sofa : sofaItems) {
            BuiltinItemRendererRegistry.INSTANCE.register(sofa, new SofaItemRenderer());
        }

        ItemLike[] wallClockItems = {
                ModBlocks.OAK_WALL_CLOCK,
                ModBlocks.SPRUCE_WALL_CLOCK,
                ModBlocks.BIRCH_WALL_CLOCK,
                ModBlocks.JUNGLE_WALL_CLOCK,
                ModBlocks.ACACIA_WALL_CLOCK,
                ModBlocks.DARK_OAK_WALL_CLOCK,
                ModBlocks.MANGROVE_WALL_CLOCK,
                ModBlocks.CHERRY_WALL_CLOCK,
                ModBlocks.BAMBOO_WALL_CLOCK,
                ModBlocks.CRIMSON_WALL_CLOCK,
                ModBlocks.WARPED_WALL_CLOCK,
                ModBlocks.IRON_WALL_CLOCK,
                ModBlocks.GLASS_WALL_CLOCK,
                ModBlocks.UNDEAD_WALL_CLOCK,
                ModBlocks.OMINOUS_WALL_CLOCK
        };
        for (ItemLike wallClock : wallClockItems) {
            BuiltinItemRendererRegistry.INSTANCE.register(wallClock, new WallClockItemRenderer());
        }

        ItemLike[] bathtubsItems = {
                ModBlocks.STONE_BRICK_BATHTUB,
                ModBlocks.MOSSY_STONE_BRICK_BATHTUB,
                ModBlocks.GRANITE_BATHTUB,
                ModBlocks.DIORITE_BATHTUB,
                ModBlocks.ANDESITE_BATHTUB,
                ModBlocks.DEEPSLATE_BATHTUB,
                ModBlocks.CALCITE_BATHTUB,
                ModBlocks.TUFF_BATHTUB,
                ModBlocks.BRICK_BATHTUB,
                ModBlocks.MUD_BATHTUB,
                ModBlocks.SANDSTONE_BATHTUB,
                ModBlocks.RED_SANDSTONE_BATHTUB,
                ModBlocks.PRISMARINE_BATHTUB,
                ModBlocks.NETHER_BRICK_BATHTUB,
                ModBlocks.RED_NETHER_BRICK_BATHTUB,
                ModBlocks.BLACKSTONE_BATHTUB,
                ModBlocks.ENDSTONE_BATHTUB,
                ModBlocks.PURPUR_BATHTUB,
                ModBlocks.IRON_BATHTUB,
                ModBlocks.GOLD_BATHTUB,
        };
        for (ItemLike bathtubs : bathtubsItems) {
            BuiltinItemRendererRegistry.INSTANCE.register(bathtubs, new BathtubItemRenderer());
        }

        ModRenderLayers.registerBlockRenderLayers();
        ModRenderLayers.registerColorProviders();
        ModModelPredicates.registerModelPredicates();
    }
}