package net.luckystudio.cozyhome.util;
import net.minecraft.world.item.Item;

import net.luckystudio.cozyhome.block.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class ModFuels {
    private static final Map<Item, Integer> FUELS = new HashMap<>();

    /** Burn time in ticks for an item, or -1 if the item is not a Cozy Home fuel. */
    public static int getBurnTime(Item item) {
        if (FUELS.isEmpty()) registerFuels();
        return FUELS.getOrDefault(item, -1);
    }

    /** Calls the consumer with every Cozy Home fuel and its burn time in ticks. */
    public static void forEach(java.util.function.ObjIntConsumer<Item> consumer) {
        if (FUELS.isEmpty()) registerFuels();
        FUELS.forEach(consumer::accept);
    }

    public static void registerFuels() {
        FUELS.put(ModBlocks.OAK_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.SPRUCE_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.BIRCH_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.JUNGLE_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.ACACIA_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.DARK_OAK_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.MANGROVE_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.CHERRY_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.BAMBOO_TABLE.asItem(), 300);
        FUELS.put(ModBlocks.OAK_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.SPRUCE_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.BIRCH_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.JUNGLE_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.ACACIA_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.DARK_OAK_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.MANGROVE_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.CHERRY_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.BAMBOO_BENCH.asItem(), 150);
        FUELS.put(ModBlocks.OAK_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.SPRUCE_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.BIRCH_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.JUNGLE_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.ACACIA_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.DARK_OAK_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.MANGROVE_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.CHERRY_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.BAMBOO_CHAIR.asItem(), 150);
        FUELS.put(ModBlocks.OAK_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.SPRUCE_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.BIRCH_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.JUNGLE_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.ACACIA_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.DARK_OAK_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.MANGROVE_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.CHERRY_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.BAMBOO_WALL_CLOCK.asItem(), 60);
        FUELS.put(ModBlocks.OAK_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.SPRUCE_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.BIRCH_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.JUNGLE_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.ACACIA_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.DARK_OAK_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.MANGROVE_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.CHERRY_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.BAMBOO_GRANDFATHER_CLOCK.asItem(), 900);
        FUELS.put(ModBlocks.OAK_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.SPRUCE_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.BIRCH_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.JUNGLE_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.ACACIA_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.DARK_OAK_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.MANGROVE_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.CHERRY_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.BAMBOO_SOFA.asItem(), 600);
        FUELS.put(ModBlocks.OAK_DESK.asItem(), 300);
        FUELS.put(ModBlocks.SPRUCE_DESK.asItem(), 300);
        FUELS.put(ModBlocks.BIRCH_DESK.asItem(), 300);
        FUELS.put(ModBlocks.JUNGLE_DESK.asItem(), 300);
        FUELS.put(ModBlocks.ACACIA_DESK.asItem(), 300);
        FUELS.put(ModBlocks.DARK_OAK_DESK.asItem(), 300);
        FUELS.put(ModBlocks.MANGROVE_DESK.asItem(), 300);
        FUELS.put(ModBlocks.CHERRY_DESK.asItem(), 300);
        FUELS.put(ModBlocks.BAMBOO_DESK.asItem(), 300);
        FUELS.put(ModBlocks.OAK_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.SPRUCE_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.BIRCH_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.JUNGLE_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.ACACIA_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.DARK_OAK_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.MANGROVE_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.CHERRY_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.BAMBOO_DRAWER.asItem(), 300);
        FUELS.put(ModBlocks.OAK_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.SPRUCE_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.BIRCH_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.JUNGLE_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.ACACIA_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.DARK_OAK_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.MANGROVE_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.CHERRY_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.BAMBOO_WALL_MIRROR.asItem(), 100);
        FUELS.put(ModBlocks.OAK_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.SPRUCE_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.BIRCH_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.JUNGLE_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.ACACIA_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.DARK_OAK_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.MANGROVE_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.CHERRY_LARGE_STUMP.asItem(), 100);
        FUELS.put(ModBlocks.BAMBOO_LARGE_STUMP.asItem(), 100);
    }
}
