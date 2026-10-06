package net.luckystudio.cozyhome.item;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final CreativeModeTab COZY_HOME =
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "cozyhome"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(ModBlocks.OAK_CHAIR))
                    .title(Component.translatable("itemgroup.cozyhome.cozyhome"))
                    .displayItems((parameters, output) -> {

                        // Counters
                        output.accept(ModBlocks.OAK_COUNTER);
                        output.accept(ModBlocks.SPRUCE_COUNTER);
                        output.accept(ModBlocks.BIRCH_COUNTER);
                        output.accept(ModBlocks.JUNGLE_COUNTER);
                        output.accept(ModBlocks.ACACIA_COUNTER);
                        output.accept(ModBlocks.DARK_OAK_COUNTER);
                        output.accept(ModBlocks.MANGROVE_COUNTER);
                        output.accept(ModBlocks.CHERRY_COUNTER);
                        output.accept(ModBlocks.BAMBOO_COUNTER);
                        output.accept(ModBlocks.CRIMSON_COUNTER);
                        output.accept(ModBlocks.WARPED_COUNTER);

                        // Storage Counters
                        output.accept(ModBlocks.OAK_STORAGE_COUNTER);
                        output.accept(ModBlocks.SPRUCE_STORAGE_COUNTER);
                        output.accept(ModBlocks.BIRCH_STORAGE_COUNTER);
                        output.accept(ModBlocks.JUNGLE_STORAGE_COUNTER);
                        output.accept(ModBlocks.ACACIA_STORAGE_COUNTER);
                        output.accept(ModBlocks.DARK_OAK_STORAGE_COUNTER);
                        output.accept(ModBlocks.MANGROVE_STORAGE_COUNTER);
                        output.accept(ModBlocks.CHERRY_STORAGE_COUNTER);
                        output.accept(ModBlocks.BAMBOO_STORAGE_COUNTER);
                        output.accept(ModBlocks.CRIMSON_STORAGE_COUNTER);
                        output.accept(ModBlocks.WARPED_STORAGE_COUNTER);

                        // Sink Counters
                        output.accept(ModBlocks.OAK_SINK_COUNTER);
                        output.accept(ModBlocks.SPRUCE_SINK_COUNTER);
                        output.accept(ModBlocks.BIRCH_SINK_COUNTER);
                        output.accept(ModBlocks.JUNGLE_SINK_COUNTER);
                        output.accept(ModBlocks.ACACIA_SINK_COUNTER);
                        output.accept(ModBlocks.DARK_OAK_SINK_COUNTER);
                        output.accept(ModBlocks.MANGROVE_SINK_COUNTER);
                        output.accept(ModBlocks.CHERRY_SINK_COUNTER);
                        output.accept(ModBlocks.BAMBOO_SINK_COUNTER);
                        output.accept(ModBlocks.CRIMSON_SINK_COUNTER);
                        output.accept(ModBlocks.WARPED_SINK_COUNTER);

                        // Tables
                        output.accept(ModBlocks.OAK_TABLE);
                        output.accept(ModBlocks.SPRUCE_TABLE);
                        output.accept(ModBlocks.BIRCH_TABLE);
                        output.accept(ModBlocks.JUNGLE_TABLE);
                        output.accept(ModBlocks.ACACIA_TABLE);
                        output.accept(ModBlocks.DARK_OAK_TABLE);
                        output.accept(ModBlocks.MANGROVE_TABLE);
                        output.accept(ModBlocks.CHERRY_TABLE);
                        output.accept(ModBlocks.BAMBOO_TABLE);
                        output.accept(ModBlocks.CRIMSON_TABLE);
                        output.accept(ModBlocks.WARPED_TABLE);
                        output.accept(ModBlocks.IRON_TABLE);
                        output.accept(ModBlocks.GLASS_TABLE);
                        output.accept(ModBlocks.UNDEAD_TABLE);
                        output.accept(ModBlocks.OMINOUS_TABLE);

                        // Chairs
                        output.accept(ModBlocks.OAK_CHAIR);
                        output.accept(ModBlocks.SPRUCE_CHAIR);
                        output.accept(ModBlocks.BIRCH_CHAIR);
                        output.accept(ModBlocks.JUNGLE_CHAIR);
                        output.accept(ModBlocks.ACACIA_CHAIR);
                        output.accept(ModBlocks.DARK_OAK_CHAIR);
                        output.accept(ModBlocks.MANGROVE_CHAIR);
                        output.accept(ModBlocks.CHERRY_CHAIR);
                        output.accept(ModBlocks.BAMBOO_CHAIR);
                        output.accept(ModBlocks.CRIMSON_CHAIR);
                        output.accept(ModBlocks.WARPED_CHAIR);
                        output.accept(ModBlocks.IRON_CHAIR);
                        output.accept(ModBlocks.GLASS_CHAIR);
                        output.accept(ModBlocks.UNDEAD_CHAIR);
                        output.accept(ModBlocks.OMINOUS_CHAIR);

                        // Wall Clocks
                        output.accept(ModBlocks.OAK_WALL_CLOCK);
                        output.accept(ModBlocks.SPRUCE_WALL_CLOCK);
                        output.accept(ModBlocks.BIRCH_WALL_CLOCK);
                        output.accept(ModBlocks.JUNGLE_WALL_CLOCK);
                        output.accept(ModBlocks.ACACIA_WALL_CLOCK);
                        output.accept(ModBlocks.DARK_OAK_WALL_CLOCK);
                        output.accept(ModBlocks.MANGROVE_WALL_CLOCK);
                        output.accept(ModBlocks.CHERRY_WALL_CLOCK);
                        output.accept(ModBlocks.BAMBOO_WALL_CLOCK);
                        output.accept(ModBlocks.CRIMSON_WALL_CLOCK);
                        output.accept(ModBlocks.WARPED_WALL_CLOCK);
                        output.accept(ModBlocks.IRON_WALL_CLOCK);
                        output.accept(ModBlocks.GLASS_WALL_CLOCK);
                        output.accept(ModBlocks.UNDEAD_WALL_CLOCK);
                        output.accept(ModBlocks.OMINOUS_WALL_CLOCK);

                        // Grandfather Clocks
                        output.accept(ModBlocks.OAK_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.SPRUCE_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.BIRCH_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.JUNGLE_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.ACACIA_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.DARK_OAK_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.MANGROVE_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.CHERRY_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.BAMBOO_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.CRIMSON_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.WARPED_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.IRON_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.GLASS_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.UNDEAD_GRANDFATHER_CLOCK);
                        output.accept(ModBlocks.OMINOUS_GRANDFATHER_CLOCK);

                        output.accept(ModBlocks.TENT);
                        output.accept(ModBlocks.OAK_MIRROR_STAND);
                        output.accept(ModBlocks.SPRUCE_MIRROR_STAND);
                        output.accept(ModBlocks.BIRCH_MIRROR_STAND);
                        output.accept(ModBlocks.JUNGLE_MIRROR_STAND);
                        output.accept(ModBlocks.ACACIA_MIRROR_STAND);
                        output.accept(ModBlocks.DARK_OAK_MIRROR_STAND);
                        output.accept(ModBlocks.MANGROVE_MIRROR_STAND);
                        output.accept(ModBlocks.CHERRY_MIRROR_STAND);
                        output.accept(ModBlocks.BAMBOO_MIRROR_STAND);
                        output.accept(ModBlocks.CRIMSON_MIRROR_STAND);
                        output.accept(ModBlocks.WARPED_MIRROR_STAND);
                        output.accept(ModBlocks.STUMP_CHAIR);
                        output.accept(ModBlocks.OAK_BENCH);
                        output.accept(ModBlocks.SPRUCE_BENCH);
                        output.accept(ModBlocks.BIRCH_BENCH);
                        output.accept(ModBlocks.JUNGLE_BENCH);
                        output.accept(ModBlocks.ACACIA_BENCH);
                        output.accept(ModBlocks.DARK_OAK_BENCH);
                        output.accept(ModBlocks.MANGROVE_BENCH);
                        output.accept(ModBlocks.CHERRY_BENCH);
                        output.accept(ModBlocks.BAMBOO_BENCH);
                        output.accept(ModBlocks.CRIMSON_BENCH);
                        output.accept(ModBlocks.WARPED_BENCH);
                        output.accept(ModBlocks.OAK_TOOL_RACK);
                        output.accept(ModBlocks.SPRUCE_TOOL_RACK);
                        output.accept(ModBlocks.BIRCH_TOOL_RACK);
                        output.accept(ModBlocks.JUNGLE_TOOL_RACK);
                        output.accept(ModBlocks.ACACIA_TOOL_RACK);
                        output.accept(ModBlocks.DARK_OAK_TOOL_RACK);
                        output.accept(ModBlocks.MANGROVE_TOOL_RACK);
                        output.accept(ModBlocks.CHERRY_TOOL_RACK);
                        output.accept(ModBlocks.BAMBOO_TOOL_RACK);
                        output.accept(ModBlocks.CRIMSON_TOOL_RACK);
                        output.accept(ModBlocks.WARPED_TOOL_RACK);
                        output.accept(ModBlocks.IRON_TOOL_RACK);
                        output.accept(ModBlocks.GLASS_TOOL_RACK);
                        output.accept(ModBlocks.UNDEAD_TOOL_RACK);
                        output.accept(ModBlocks.OMINOUS_TOOL_RACK);

                        // Lamps
                        output.accept(ModBlocks.OAK_LAMP);
                        output.accept(ModBlocks.SPRUCE_LAMP);
                        output.accept(ModBlocks.BIRCH_LAMP);
                        output.accept(ModBlocks.JUNGLE_LAMP);
                        output.accept(ModBlocks.ACACIA_LAMP);
                        output.accept(ModBlocks.DARK_OAK_LAMP);
                        output.accept(ModBlocks.MANGROVE_LAMP);
                        output.accept(ModBlocks.CHERRY_LAMP);
                        output.accept(ModBlocks.BAMBOO_LAMP);
                        output.accept(ModBlocks.CRIMSON_LAMP);
                        output.accept(ModBlocks.WARPED_LAMP);
                        output.accept(ModBlocks.IRON_LAMP);
                        output.accept(ModBlocks.GLASS_LAMP);
                        output.accept(ModBlocks.UNDEAD_LAMP);
                        output.accept(ModBlocks.OMINOUS_LAMP);

                        // Sofas
                        output.accept(ModBlocks.OAK_SOFA);
                        output.accept(ModBlocks.SPRUCE_SOFA);
                        output.accept(ModBlocks.BIRCH_SOFA);
                        output.accept(ModBlocks.JUNGLE_SOFA);
                        output.accept(ModBlocks.ACACIA_SOFA);
                        output.accept(ModBlocks.DARK_OAK_SOFA);
                        output.accept(ModBlocks.MANGROVE_SOFA);
                        output.accept(ModBlocks.CHERRY_SOFA);
                        output.accept(ModBlocks.BAMBOO_SOFA);
                        output.accept(ModBlocks.CRIMSON_SOFA);
                        output.accept(ModBlocks.WARPED_SOFA);

                        // Couches
                        output.accept(ModBlocks.OAK_COUCH);
                        output.accept(ModBlocks.SPRUCE_COUCH);
                        output.accept(ModBlocks.BIRCH_COUCH);
                        output.accept(ModBlocks.JUNGLE_COUCH);
                        output.accept(ModBlocks.ACACIA_COUCH);
                        output.accept(ModBlocks.DARK_OAK_COUCH);
                        output.accept(ModBlocks.MANGROVE_COUCH);
                        output.accept(ModBlocks.CHERRY_COUCH);
                        output.accept(ModBlocks.BAMBOO_COUCH);
                        output.accept(ModBlocks.CRIMSON_COUCH);
                        output.accept(ModBlocks.WARPED_COUCH);

                        // Desks
                        output.accept(ModBlocks.OAK_DESK);
                        output.accept(ModBlocks.SPRUCE_DESK);
                        output.accept(ModBlocks.BIRCH_DESK);
                        output.accept(ModBlocks.JUNGLE_DESK);
                        output.accept(ModBlocks.ACACIA_DESK);
                        output.accept(ModBlocks.DARK_OAK_DESK);
                        output.accept(ModBlocks.MANGROVE_DESK);
                        output.accept(ModBlocks.CHERRY_DESK);
                        output.accept(ModBlocks.BAMBOO_DESK);
                        output.accept(ModBlocks.CRIMSON_DESK);
                        output.accept(ModBlocks.WARPED_DESK);

                        // Drawer
                        output.accept(ModBlocks.OAK_DRAWER);
                        output.accept(ModBlocks.SPRUCE_DRAWER);
                        output.accept(ModBlocks.BIRCH_DRAWER);
                        output.accept(ModBlocks.JUNGLE_DRAWER);
                        output.accept(ModBlocks.ACACIA_DRAWER);
                        output.accept(ModBlocks.DARK_OAK_DRAWER);
                        output.accept(ModBlocks.MANGROVE_DRAWER);
                        output.accept(ModBlocks.CHERRY_DRAWER);
                        output.accept(ModBlocks.BAMBOO_DRAWER);
                        output.accept(ModBlocks.CRIMSON_DRAWER);
                        output.accept(ModBlocks.WARPED_DRAWER);

                        // Wall Mirrors
                        output.accept(ModBlocks.OAK_WALL_MIRROR);
                        output.accept(ModBlocks.SPRUCE_WALL_MIRROR);
                        output.accept(ModBlocks.BIRCH_WALL_MIRROR);
                        output.accept(ModBlocks.JUNGLE_WALL_MIRROR);
                        output.accept(ModBlocks.ACACIA_WALL_MIRROR);
                        output.accept(ModBlocks.DARK_OAK_WALL_MIRROR);
                        output.accept(ModBlocks.MANGROVE_WALL_MIRROR);
                        output.accept(ModBlocks.CHERRY_WALL_MIRROR);
                        output.accept(ModBlocks.BAMBOO_WALL_MIRROR);
                        output.accept(ModBlocks.CRIMSON_WALL_MIRROR);
                        output.accept(ModBlocks.WARPED_WALL_MIRROR);

                        // SINKS
                        output.accept(ModBlocks.STONE_BRICK_SINK);
                        output.accept(ModBlocks.MOSSY_STONE_BRICK_SINK);
                        output.accept(ModBlocks.GRANITE_SINK);
                        output.accept(ModBlocks.DIORITE_SINK);
                        output.accept(ModBlocks.ANDESITE_SINK);
                        output.accept(ModBlocks.DEEPSLATE_SINK);
                        output.accept(ModBlocks.CALCITE_SINK);
                        output.accept(ModBlocks.TUFF_SINK);
                        output.accept(ModBlocks.BRICK_SINK);
                        output.accept(ModBlocks.MUD_SINK);
                        output.accept(ModBlocks.SANDSTONE_SINK);
                        output.accept(ModBlocks.RED_SANDSTONE_SINK);
                        output.accept(ModBlocks.PRISMARINE_SINK);
                        output.accept(ModBlocks.NETHER_BRICK_SINK);
                        output.accept(ModBlocks.RED_NETHER_BRICK_SINK);
                        output.accept(ModBlocks.BLACKSTONE_SINK);
                        output.accept(ModBlocks.ENDSTONE_SINK);
                        output.accept(ModBlocks.PURPUR_SINK);
                        output.accept(ModBlocks.IRON_SINK);
                        output.accept(ModBlocks.GOLD_SINK);

                        // BATHTUBS
                        output.accept(ModBlocks.STONE_BRICK_BATHTUB);
                        output.accept(ModBlocks.MOSSY_STONE_BRICK_BATHTUB);
                        output.accept(ModBlocks.GRANITE_BATHTUB);
                        output.accept(ModBlocks.DIORITE_BATHTUB);
                        output.accept(ModBlocks.ANDESITE_BATHTUB);
                        output.accept(ModBlocks.DEEPSLATE_BATHTUB);
                        output.accept(ModBlocks.CALCITE_BATHTUB);
                        output.accept(ModBlocks.TUFF_BATHTUB);
                        output.accept(ModBlocks.BRICK_BATHTUB);
                        output.accept(ModBlocks.MUD_BATHTUB);
                        output.accept(ModBlocks.SANDSTONE_BATHTUB);
                        output.accept(ModBlocks.RED_SANDSTONE_BATHTUB);
                        output.accept(ModBlocks.PRISMARINE_BATHTUB);
                        output.accept(ModBlocks.NETHER_BRICK_BATHTUB);
                        output.accept(ModBlocks.RED_NETHER_BRICK_BATHTUB);
                        output.accept(ModBlocks.BLACKSTONE_BATHTUB);
                        output.accept(ModBlocks.ENDSTONE_BATHTUB);
                        output.accept(ModBlocks.PURPUR_BATHTUB);
                        output.accept(ModBlocks.IRON_BATHTUB);
                        output.accept(ModBlocks.GOLD_BATHTUB);

                        // LARGE STUMPS
                        output.accept(ModBlocks.OAK_LARGE_STUMP);
                        output.accept(ModBlocks.SPRUCE_LARGE_STUMP);
                        output.accept(ModBlocks.BIRCH_LARGE_STUMP);
                        output.accept(ModBlocks.JUNGLE_LARGE_STUMP);
                        output.accept(ModBlocks.ACACIA_LARGE_STUMP);
                        output.accept(ModBlocks.DARK_OAK_LARGE_STUMP);
                        output.accept(ModBlocks.MANGROVE_LARGE_STUMP);
                        output.accept(ModBlocks.CHERRY_LARGE_STUMP);
                        output.accept(ModBlocks.BAMBOO_LARGE_STUMP);
                        output.accept(ModBlocks.CRIMSON_LARGE_STUMP);
                        output.accept(ModBlocks.WARPED_LARGE_STUMP);

                        // FOUNTAINS
                        output.accept(ModBlocks.STONE_BRICK_FOUNTAIN);
                        output.accept(ModBlocks.MOSSY_STONE_BRICK_FOUNTAIN);
                        output.accept(ModBlocks.GRANITE_FOUNTAIN);
                        output.accept(ModBlocks.DIORITE_FOUNTAIN);
                        output.accept(ModBlocks.ANDESITE_FOUNTAIN);
                        output.accept(ModBlocks.DEEPSLATE_FOUNTAIN);
                        output.accept(ModBlocks.CALCITE_FOUNTAIN);
                        output.accept(ModBlocks.TUFF_FOUNTAIN);
                        output.accept(ModBlocks.BRICK_FOUNTAIN);
                        output.accept(ModBlocks.MUD_FOUNTAIN);
                        output.accept(ModBlocks.SANDSTONE_FOUNTAIN);
                        output.accept(ModBlocks.RED_SANDSTONE_FOUNTAIN);
                        output.accept(ModBlocks.PRISMARINE_FOUNTAIN);
                        output.accept(ModBlocks.NETHER_BRICK_FOUNTAIN);
                        output.accept(ModBlocks.RED_NETHER_BRICK_FOUNTAIN);
                        output.accept(ModBlocks.BLACKSTONE_FOUNTAIN);
                        output.accept(ModBlocks.ENDSTONE_FOUNTAIN);
                        output.accept(ModBlocks.PURPUR_FOUNTAIN);

                        // FOUNTAIN SPROUTS
                        output.accept(ModBlocks.STONE_BRICK_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.MOSSY_STONE_BRICK_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.GRANITE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.DIORITE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.ANDESITE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.DEEPSLATE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.CALCITE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.TUFF_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.BRICK_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.MUD_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.SANDSTONE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.RED_SANDSTONE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.PRISMARINE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.NETHER_BRICK_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.RED_NETHER_BRICK_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.BLACKSTONE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.ENDSTONE_FOUNTAIN_SPOUT);
                        output.accept(ModBlocks.PURPUR_FOUNTAIN_SPOUT);
                        //
                        // CHIMNEYS
                        output.accept(ModBlocks.STONE_BRICK_CHIMNEY);
                        output.accept(ModBlocks.MOSSY_STONE_BRICK_CHIMNEY);
                        output.accept(ModBlocks.GRANITE_CHIMNEY);
                        output.accept(ModBlocks.DIORITE_CHIMNEY);
                        output.accept(ModBlocks.ANDESITE_CHIMNEY);
                        output.accept(ModBlocks.DEEPSLATE_CHIMNEY);
                        output.accept(ModBlocks.CALCITE_CHIMNEY);
                        output.accept(ModBlocks.TUFF_CHIMNEY);
                        output.accept(ModBlocks.BRICK_CHIMNEY);
                        output.accept(ModBlocks.MUD_CHIMNEY);
                        output.accept(ModBlocks.SANDSTONE_CHIMNEY);
                        output.accept(ModBlocks.RED_SANDSTONE_CHIMNEY);
                        output.accept(ModBlocks.PRISMARINE_CHIMNEY);
                        output.accept(ModBlocks.NETHER_BRICK_CHIMNEY);
                        output.accept(ModBlocks.RED_NETHER_BRICK_CHIMNEY);
                        output.accept(ModBlocks.BLACKSTONE_CHIMNEY);
                        output.accept(ModBlocks.ENDSTONE_CHIMNEY);
                        output.accept(ModBlocks.PURPUR_CHIMNEY);
                        output.accept(ModBlocks.IRON_CHIMNEY);
                        output.accept(ModBlocks.GOLD_CHIMNEY);

                        // Misc. Blocks/Items
                        output.accept(ModBlocks.TELESCOPE);
                        output.accept(ModItems.PAINT_BRUSH);
                        output.accept(ModItems.CUSHION);
                        output.accept(ModItems.HAY_CUSHION);
                        output.accept(ModItems.TRADER_CUSHION);


                    }).build());

    public static void registerModItemGroups() {
        CozyHome.LOGGER.info("Registering Item Groups for " + CozyHome.MOD_ID);
    }
}
