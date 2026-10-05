package net.luckystudio.cozyhome.client;

import java.util.function.Supplier;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockModel;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeModel;
import net.luckystudio.cozyhome.entity.model.CushionModel;
import net.luckystudio.cozyhome.entity.model.SeatEntityModel;
import net.luckystudio.cozyhome.item.renderer.BathtubItemRenderer;
import net.luckystudio.cozyhome.item.renderer.ChairItemRenderer;
import net.luckystudio.cozyhome.item.renderer.SofaItemRenderer;
import net.luckystudio.cozyhome.item.renderer.WallClockItemRenderer;

/** The client registrations that are identical on every loader; the loader module supplies the actual registry. */
public class ClientRegistrations {

    public interface LayerRegistrar {
        void register(ModelLayerLocation layer, Supplier<LayerDefinition> definition);
    }

    public interface ItemRendererRegistrar {
        /** The renderer is created lazily, the first time one of the items is drawn. */
        void register(Supplier<BlockEntityWithoutLevelRenderer> renderer, Item... items);
    }

    public static void layerDefinitions(LayerRegistrar registrar) {
        registrar.register(ModEntityModelLayers.SEAT, SeatEntityModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.TELESCOPE, TelescopeModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.SOFA, SofaModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.SOFA_CUSHION, SofaCushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.COUCH_CUSHION, CouchCushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.CHAIR, ChairModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.CUSHION, CushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.GRANDFATHER_CLOCK, GrandfatherClockModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.WALL_CLOCK, WallClockModel::getTexturedModelData);
    }

    /** Items that are drawn by a block entity model instead of a regular item model. */
    public static void itemRenderers(ItemRendererRegistrar registrar) {
        registrar.register(ChairItemRenderer::new,
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
        registrar.register(SofaItemRenderer::new,
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
        registrar.register(WallClockItemRenderer::new,
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
        registrar.register(BathtubItemRenderer::new,
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
