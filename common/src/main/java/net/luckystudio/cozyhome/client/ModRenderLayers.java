package net.luckystudio.cozyhome.client;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.util.ModBlockUtilities;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.util.ModColorHandler;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ModRenderLayers {

    /** Blocks rendered with the cutout layer (transparent pixels). */
    public static void registerBlockRenderLayers() {
        for (Block block : cutoutBlocks()) {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        }
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // Blocks that can hold water or other liquids
        event.register((state, world, pos, tintIndex) ->
                ModBlockUtilities.getColorFromContainsState(state, world, pos), containsBlocks());

        // Blocks that are dyed through their block entity
        event.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return -17170434;
            return ModColorHandler.getBlockColor(world.getBlockEntity(pos), -17170434);
        }, dyedBlocks());
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> DyedItemColor.getOrDefault(stack, -17170434), dyedItems());
    }

    private static Block[] cutoutBlocks() {
        return new Block[]{
                ModBlocks.OAK_SINK_COUNTER,
                ModBlocks.SPRUCE_SINK_COUNTER,
                ModBlocks.BIRCH_SINK_COUNTER,
                ModBlocks.JUNGLE_SINK_COUNTER,
                ModBlocks.ACACIA_SINK_COUNTER,
                ModBlocks.DARK_OAK_SINK_COUNTER,
                ModBlocks.MANGROVE_SINK_COUNTER,
                ModBlocks.CHERRY_SINK_COUNTER,
                ModBlocks.BAMBOO_SINK_COUNTER,
                ModBlocks.CRIMSON_SINK_COUNTER,
                ModBlocks.WARPED_SINK_COUNTER,
                ModBlocks.OAK_TABLE,
                ModBlocks.SPRUCE_TABLE,
                ModBlocks.BIRCH_TABLE,
                ModBlocks.JUNGLE_TABLE,
                ModBlocks.ACACIA_TABLE,
                ModBlocks.DARK_OAK_TABLE,
                ModBlocks.MANGROVE_TABLE,
                ModBlocks.CHERRY_TABLE,
                ModBlocks.BAMBOO_TABLE,
                ModBlocks.CRIMSON_TABLE,
                ModBlocks.WARPED_TABLE,
                ModBlocks.IRON_TABLE,
                ModBlocks.GLASS_TABLE,
                ModBlocks.OMINOUS_TABLE,
                ModBlocks.UNDEAD_TABLE,
                ModBlocks.OAK_LAMP,
                ModBlocks.SPRUCE_LAMP,
                ModBlocks.BIRCH_LAMP,
                ModBlocks.JUNGLE_LAMP,
                ModBlocks.ACACIA_LAMP,
                ModBlocks.DARK_OAK_LAMP,
                ModBlocks.MANGROVE_LAMP,
                ModBlocks.CHERRY_LAMP,
                ModBlocks.BAMBOO_LAMP,
                ModBlocks.CRIMSON_LAMP,
                ModBlocks.WARPED_LAMP,
                ModBlocks.IRON_LAMP,
                ModBlocks.GLASS_LAMP,
                ModBlocks.UNDEAD_LAMP,
                ModBlocks.OMINOUS_LAMP,
                ModBlocks.OAK_WALL_MIRROR,
                ModBlocks.SPRUCE_WALL_MIRROR,
                ModBlocks.BIRCH_WALL_MIRROR,
                ModBlocks.JUNGLE_WALL_MIRROR,
                ModBlocks.ACACIA_WALL_MIRROR,
                ModBlocks.DARK_OAK_WALL_MIRROR,
                ModBlocks.MANGROVE_WALL_MIRROR,
                ModBlocks.CHERRY_WALL_MIRROR,
                ModBlocks.BAMBOO_WALL_MIRROR,
                ModBlocks.CRIMSON_WALL_MIRROR,
                ModBlocks.WARPED_WALL_MIRROR,
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
                ModBlocks.TELESCOPE
        };
    }

    private static Block[] containsBlocks() {
        return new Block[]{
                ModBlocks.OAK_SINK_COUNTER,
                ModBlocks.SPRUCE_SINK_COUNTER,
                ModBlocks.BIRCH_SINK_COUNTER,
                ModBlocks.JUNGLE_SINK_COUNTER,
                ModBlocks.ACACIA_SINK_COUNTER,
                ModBlocks.DARK_OAK_SINK_COUNTER,
                ModBlocks.MANGROVE_SINK_COUNTER,
                ModBlocks.CHERRY_SINK_COUNTER,
                ModBlocks.BAMBOO_SINK_COUNTER,
                ModBlocks.CRIMSON_SINK_COUNTER,
                ModBlocks.WARPED_SINK_COUNTER,
                ModBlocks.STONE_BRICK_SINK,
                ModBlocks.MOSSY_STONE_BRICK_SINK,
                ModBlocks.GRANITE_SINK,
                ModBlocks.DIORITE_SINK,
                ModBlocks.ANDESITE_SINK,
                ModBlocks.DEEPSLATE_SINK,
                ModBlocks.CALCITE_SINK,
                ModBlocks.TUFF_SINK,
                ModBlocks.BRICK_SINK,
                ModBlocks.MUD_SINK,
                ModBlocks.SANDSTONE_SINK,
                ModBlocks.RED_SANDSTONE_SINK,
                ModBlocks.PRISMARINE_SINK,
                ModBlocks.NETHER_BRICK_SINK,
                ModBlocks.RED_NETHER_BRICK_SINK,
                ModBlocks.BLACKSTONE_SINK,
                ModBlocks.ENDSTONE_SINK,
                ModBlocks.PURPUR_SINK,
                ModBlocks.IRON_SINK,
                ModBlocks.GOLD_SINK,
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
                ModBlocks.STONE_BRICK_FOUNTAIN,
                ModBlocks.STONE_BRICK_FOUNTAIN_SPOUT,
                ModBlocks.MOSSY_STONE_BRICK_FOUNTAIN,
                ModBlocks.MOSSY_STONE_BRICK_FOUNTAIN_SPOUT,
                ModBlocks.GRANITE_FOUNTAIN,
                ModBlocks.GRANITE_FOUNTAIN_SPOUT,
                ModBlocks.DIORITE_FOUNTAIN,
                ModBlocks.DIORITE_FOUNTAIN_SPOUT,
                ModBlocks.ANDESITE_FOUNTAIN,
                ModBlocks.ANDESITE_FOUNTAIN_SPOUT,
                ModBlocks.DEEPSLATE_FOUNTAIN,
                ModBlocks.DEEPSLATE_FOUNTAIN_SPOUT,
                ModBlocks.CALCITE_FOUNTAIN,
                ModBlocks.CALCITE_FOUNTAIN_SPOUT,
                ModBlocks.TUFF_FOUNTAIN,
                ModBlocks.TUFF_FOUNTAIN_SPOUT,
                ModBlocks.BRICK_FOUNTAIN,
                ModBlocks.BRICK_FOUNTAIN_SPOUT,
                ModBlocks.MUD_FOUNTAIN,
                ModBlocks.MUD_FOUNTAIN_SPOUT,
                ModBlocks.SANDSTONE_FOUNTAIN,
                ModBlocks.SANDSTONE_FOUNTAIN_SPOUT,
                ModBlocks.RED_SANDSTONE_FOUNTAIN,
                ModBlocks.RED_SANDSTONE_FOUNTAIN_SPOUT,
                ModBlocks.PRISMARINE_FOUNTAIN,
                ModBlocks.PRISMARINE_FOUNTAIN_SPOUT,
                ModBlocks.NETHER_BRICK_FOUNTAIN,
                ModBlocks.NETHER_BRICK_FOUNTAIN_SPOUT,
                ModBlocks.RED_NETHER_BRICK_FOUNTAIN,
                ModBlocks.RED_NETHER_BRICK_FOUNTAIN_SPOUT,
                ModBlocks.BLACKSTONE_FOUNTAIN,
                ModBlocks.BLACKSTONE_FOUNTAIN_SPOUT,
                ModBlocks.ENDSTONE_FOUNTAIN,
                ModBlocks.ENDSTONE_FOUNTAIN_SPOUT,
                ModBlocks.PURPUR_FOUNTAIN,
                ModBlocks.PURPUR_FOUNTAIN_SPOUT,
                ModBlocks.FALLING_LIQUID
        };
    }

    private static Block[] dyedBlocks() {
        return new Block[]{
                ModBlocks.OAK_COUCH,
                ModBlocks.SPRUCE_COUCH,
                ModBlocks.BIRCH_COUCH,
                ModBlocks.JUNGLE_COUCH,
                ModBlocks.ACACIA_COUCH,
                ModBlocks.DARK_OAK_COUCH,
                ModBlocks.MANGROVE_COUCH,
                ModBlocks.CHERRY_COUCH,
                ModBlocks.BAMBOO_COUCH,
                ModBlocks.CRIMSON_COUCH,
                ModBlocks.WARPED_COUCH,
                ModBlocks.OAK_LAMP,
                ModBlocks.SPRUCE_LAMP,
                ModBlocks.BIRCH_LAMP,
                ModBlocks.JUNGLE_LAMP,
                ModBlocks.ACACIA_LAMP,
                ModBlocks.DARK_OAK_LAMP,
                ModBlocks.MANGROVE_LAMP,
                ModBlocks.CHERRY_LAMP,
                ModBlocks.BAMBOO_LAMP,
                ModBlocks.CRIMSON_LAMP,
                ModBlocks.WARPED_LAMP,
                ModBlocks.IRON_LAMP,
                ModBlocks.GLASS_LAMP,
                ModBlocks.UNDEAD_LAMP,
                ModBlocks.OMINOUS_LAMP
        };
    }

    private static Item[] dyedItems() {
        return new Item[]{
                ModItems.CUSHION,
                ModBlocks.OAK_COUCH.asItem(),
                ModBlocks.SPRUCE_COUCH.asItem(),
                ModBlocks.BIRCH_COUCH.asItem(),
                ModBlocks.JUNGLE_COUCH.asItem(),
                ModBlocks.ACACIA_COUCH.asItem(),
                ModBlocks.DARK_OAK_COUCH.asItem(),
                ModBlocks.MANGROVE_COUCH.asItem(),
                ModBlocks.CHERRY_COUCH.asItem(),
                ModBlocks.BAMBOO_COUCH.asItem(),
                ModBlocks.CRIMSON_COUCH.asItem(),
                ModBlocks.WARPED_COUCH.asItem(),
                ModBlocks.OAK_LAMP.asItem(),
                ModBlocks.SPRUCE_LAMP.asItem(),
                ModBlocks.BIRCH_LAMP.asItem(),
                ModBlocks.JUNGLE_LAMP.asItem(),
                ModBlocks.ACACIA_LAMP.asItem(),
                ModBlocks.DARK_OAK_LAMP.asItem(),
                ModBlocks.MANGROVE_LAMP.asItem(),
                ModBlocks.CHERRY_LAMP.asItem(),
                ModBlocks.BAMBOO_LAMP.asItem(),
                ModBlocks.CRIMSON_LAMP.asItem(),
                ModBlocks.WARPED_LAMP.asItem(),
                ModBlocks.IRON_LAMP.asItem(),
                ModBlocks.GLASS_LAMP.asItem(),
                ModBlocks.UNDEAD_LAMP.asItem(),
                ModBlocks.OMINOUS_LAMP.asItem()
        };
    }
}
