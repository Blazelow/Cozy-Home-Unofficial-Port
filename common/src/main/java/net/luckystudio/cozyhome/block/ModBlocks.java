package net.luckystudio.cozyhome.block;
import net.luckystudio.cozyhome.item.custom.CozyBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.bathtub.BathTubBlock;
import net.luckystudio.cozyhome.block.custom.chimneys.ChimneyBlock;
import net.luckystudio.cozyhome.block.custom.drawers.DeskBlock;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerBlock;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchBlock;
import net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks.LargeStumpBlock;
import net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks.ShelfTableBlock;
import net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks.TableBlock;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlock;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlock;
import net.luckystudio.cozyhome.block.custom.counters.CounterBlock;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink.SinkCounterBlock;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterBlock;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockBlock;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlock;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.block.custom.BeamBlock;
import net.luckystudio.cozyhome.block.custom.PlankedWallBlock;
import net.luckystudio.cozyhome.block.custom.tent.TentBlock;
import net.luckystudio.cozyhome.block.custom.seatable.footstool.FootstoolBlock;
import net.luckystudio.cozyhome.block.custom.mirror_stands.MirrorStandBlock;
import net.luckystudio.cozyhome.block.custom.seatable.StumpChairBlock;
import net.luckystudio.cozyhome.block.custom.seatable.benches.BenchBlock;
import net.luckystudio.cozyhome.block.custom.toolrack.ToolRackBlock;
import net.luckystudio.cozyhome.block.custom.FallingLiquidBlock;
import net.luckystudio.cozyhome.block.custom.fountains.FountainBlock;
import net.luckystudio.cozyhome.block.custom.fountains.FountainSpoutBlock;
import net.luckystudio.cozyhome.block.custom.lamps.*;
import net.luckystudio.cozyhome.block.custom.wall_mirrors.WallMirrorBlock;
import net.luckystudio.cozyhome.block.util.ModBlockUtilities;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink.SinkBlock;
import net.luckystudio.cozyhome.item.custom.DyedBlockItem;
public class ModBlocks {

    private static Block createCounterBlock(Block block, Boolean requiresTool, Boolean burnable) {
        BlockBehaviour.Properties settings = copyProps(block);
        if (requiresTool) settings.requiresCorrectToolForDrops();
        if (burnable) settings.ignitedByLava();
        return new CounterBlock(settings);
    }

    private static Block createStorageCounterBlock(Block block, Boolean requiresTool, Boolean burnable) {
        BlockBehaviour.Properties settings = copyProps(block);
        if (requiresTool) settings.requiresCorrectToolForDrops();
        if (burnable) settings.ignitedByLava();
        return new StorageCounterBlock(settings);
    }

    private static Block createSinkCounterBlock(Block block) {
        return new SinkCounterBlock(copyProps(block)
                .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15)));
    }

    private static Block createChair(ChairBlock.ChairType chairType, float hardness, float resistance, SoundType soundGroup, Boolean requiresTool, Boolean burnable) {
        BlockBehaviour.Properties settings = props();
        if (requiresTool) settings.requiresCorrectToolForDrops();
        if (burnable) settings.ignitedByLava();
        settings.destroyTime(hardness).explosionResistance(resistance).sound(soundGroup).dynamicShape();
        return new ChairBlock(chairType, settings);
    }

    private static Block createTable(Block block) {
        return new TableBlock(copyProps(block).dynamicShape().forceSolidOn());
    }

    private static Block createShelfTable(Block block) {
        return new ShelfTableBlock(copyProps(block).dynamicShape().forceSolidOn());
    }

    private static Block createWallClock(WallClockBlock.ClockType clockType, Block block) {
        return new WallClockBlock(clockType, copyProps(block)
                        .instabreak()
                        .dynamicShape());
    }

    private static Block createGrandfatherClock(GrandfatherClockBlock.GrandfatherClockType grandfatherClockType, SoundType soundGroup) {
        return new GrandfatherClockBlock(grandfatherClockType,
                props()
                        .destroyTime(2)
                        .strength(3)
                        .ignitedByLava()
                        .sound(soundGroup)
                        .dynamicShape());
    }

    private static Block createGenericLamp() {
        return new GenericLampBlock(props()
                .lightLevel(ModBlockUtilities.createLightLevelFromLitBlockState(10))
                .emissiveRendering(state -> state.getValue(BlockStateProperties.LIT))
                .instabreak()
                .dynamicShape()
                .sound(SoundType.LANTERN));
    }

    private static Block createSofa(SofaBlock.SofaType sofaType, Block block) {
        return new SofaBlock(sofaType, copyProps(block).dynamicShape().bounceRestitution(0.66F));
    }

    private static Block createCouch(Block block) {
        return new CouchBlock(copyProps(block).dynamicShape().bounceRestitution(0.66F));
    }

    private static Block createDesk(Block block) {
        return new DeskBlock(copyProps(block));
    }

    private static Block createDrawer(Block block) {
        return new DrawerBlock(block.defaultBlockState(), copyProps(block));
    }

    private static Block createSink(Block block) {
        return new SinkBlock(copyProps(block)
                .noOcclusion()
                .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15))
                .requiresCorrectToolForDrops());
    }

    private static Block createBathTub(Block block) {
        return new BathTubBlock(copyProps(block)
                .noOcclusion()
                .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15))
                .requiresCorrectToolForDrops());
    }

    private static Block createFountain(float hardness, float resistance, SoundType soundGroup) {
        return new FountainBlock(
                props()
                        .randomTicks()
                        .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15))
                        .forceSolidOn()
                        .requiresCorrectToolForDrops()
                        .destroyTime(hardness)
                        .strength(resistance)
                        .sound(soundGroup)
                        .dynamicShape());
    }

    private static Block createFountainSpout(float hardness, float resistance, SoundType soundGroup) {
        return new FountainSpoutBlock(
                props()
                        .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15))
                        .forceSolidOn()
                        .destroyTime(hardness)
                        .strength(resistance)
                        .sound(soundGroup)
                        .dynamicShape());
    }

    private static Block createChimney(float hardness, float resistance, SoundType soundGroup) {
        return new ChimneyBlock(
                props()
                        .forceSolidOn()
                        .requiresCorrectToolForDrops()
                        .destroyTime(hardness)
                        .strength(resistance)
                        .sound(soundGroup)
                        .dynamicShape());
    }

    private static Block createLargeStump(SoundType soundGroup) {
        return new LargeStumpBlock(
                props()
                        .forceSolidOn()
                        .requiresCorrectToolForDrops()
                        .destroyTime(2)
                        .strength(2)
                        .sound(soundGroup)
                        .dynamicShape());
    }

    // Counters
    public static final Block OAK_COUNTER = registerBlock("oak_counter", () -> createCounterBlock(Blocks.OAK_PLANKS, false, true));
    public static final Block SPRUCE_COUNTER = registerBlock("spruce_counter", () -> createCounterBlock(Blocks.SPRUCE_PLANKS, false, true));
    public static final Block BIRCH_COUNTER = registerBlock("birch_counter", () -> createCounterBlock(Blocks.BIRCH_PLANKS, false, true));
    public static final Block JUNGLE_COUNTER = registerBlock("jungle_counter", () -> createCounterBlock(Blocks.JUNGLE_PLANKS, false, true));
    public static final Block ACACIA_COUNTER = registerBlock("acacia_counter", () -> createCounterBlock(Blocks.ACACIA_PLANKS, false, true));
    public static final Block DARK_OAK_COUNTER = registerBlock("dark_oak_counter", () -> createCounterBlock(Blocks.DARK_OAK_PLANKS, false, true));
    public static final Block MANGROVE_COUNTER = registerBlock("mangrove_counter", () -> createCounterBlock(Blocks.MANGROVE_PLANKS, false, true));
    public static final Block CHERRY_COUNTER = registerBlock("cherry_counter", () -> createCounterBlock(Blocks.CHERRY_PLANKS, false, true));
    public static final Block BAMBOO_COUNTER = registerBlock("bamboo_counter", () -> createCounterBlock(Blocks.BAMBOO_PLANKS, false, true));
    public static final Block CRIMSON_COUNTER = registerBlock("crimson_counter", () -> createCounterBlock(Blocks.CRIMSON_PLANKS, false, false));
    public static final Block WARPED_COUNTER = registerBlock("warped_counter", () -> createCounterBlock(Blocks.WARPED_PLANKS, false, false));

    // Storage Counters
    public static final Block OAK_STORAGE_COUNTER = registerBlock("oak_storage_counter", () -> createStorageCounterBlock(Blocks.OAK_PLANKS, false, true));
    public static final Block SPRUCE_STORAGE_COUNTER = registerBlock("spruce_storage_counter", () -> createStorageCounterBlock(Blocks.SPRUCE_PLANKS, false, true));
    public static final Block BIRCH_STORAGE_COUNTER = registerBlock("birch_storage_counter", () -> createStorageCounterBlock(Blocks.BIRCH_PLANKS, false, true));
    public static final Block JUNGLE_STORAGE_COUNTER = registerBlock("jungle_storage_counter", () -> createStorageCounterBlock(Blocks.JUNGLE_PLANKS, false, true));
    public static final Block ACACIA_STORAGE_COUNTER = registerBlock("acacia_storage_counter", () -> createStorageCounterBlock(Blocks.ACACIA_PLANKS, false, true));
    public static final Block DARK_OAK_STORAGE_COUNTER = registerBlock("dark_oak_storage_counter", () -> createStorageCounterBlock(Blocks.DARK_OAK_PLANKS, false, true));
    public static final Block MANGROVE_STORAGE_COUNTER = registerBlock("mangrove_storage_counter", () -> createStorageCounterBlock(Blocks.MANGROVE_PLANKS, false, true));
    public static final Block CHERRY_STORAGE_COUNTER = registerBlock("cherry_storage_counter", () -> createStorageCounterBlock(Blocks.CHERRY_PLANKS, false, true));
    public static final Block BAMBOO_STORAGE_COUNTER = registerBlock("bamboo_storage_counter", () -> createStorageCounterBlock(Blocks.BAMBOO_PLANKS, false, true));
    public static final Block CRIMSON_STORAGE_COUNTER = registerBlock("crimson_storage_counter", () -> createStorageCounterBlock(Blocks.CRIMSON_PLANKS, false, false));
    public static final Block WARPED_STORAGE_COUNTER = registerBlock("warped_storage_counter", () -> createStorageCounterBlock(Blocks.WARPED_PLANKS, false, false));

    // Sink Counters
    public static final Block OAK_SINK_COUNTER = registerBlock("oak_sink_counter", () -> createSinkCounterBlock(Blocks.OAK_PLANKS));
    public static final Block SPRUCE_SINK_COUNTER = registerBlock("spruce_sink_counter", () -> createSinkCounterBlock(Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_SINK_COUNTER = registerBlock("birch_sink_counter", () -> createSinkCounterBlock(Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_SINK_COUNTER = registerBlock("jungle_sink_counter", () -> createSinkCounterBlock(Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_SINK_COUNTER = registerBlock("acacia_sink_counter", () -> createSinkCounterBlock(Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_SINK_COUNTER = registerBlock("dark_oak_sink_counter", () -> createSinkCounterBlock(Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_SINK_COUNTER = registerBlock("mangrove_sink_counter", () -> createSinkCounterBlock(Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_SINK_COUNTER = registerBlock("cherry_sink_counter", () -> createSinkCounterBlock(Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_SINK_COUNTER = registerBlock("bamboo_sink_counter", () -> createSinkCounterBlock(Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_SINK_COUNTER = registerBlock("crimson_sink_counter", () -> createSinkCounterBlock(Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_SINK_COUNTER = registerBlock("warped_sink_counter", () -> createSinkCounterBlock(Blocks.WARPED_PLANKS));

    // Tables
    public static final Block OAK_TABLE = registerBlock("oak_table", () -> createTable(Blocks.OAK_PLANKS));
    public static final Block SPRUCE_TABLE = registerBlock("spruce_table", () -> createTable(Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_TABLE = registerBlock("birch_table", () -> createTable(Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_TABLE = registerBlock("jungle_table", () -> createTable(Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_TABLE = registerBlock("acacia_table", () -> createTable(Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_TABLE = registerBlock("dark_oak_table", () -> createTable(Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_TABLE = registerBlock("mangrove_table", () -> createTable(Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_TABLE = registerBlock("cherry_table", () -> createTable(Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_TABLE = registerBlock("bamboo_table", () -> createTable(Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_TABLE = registerBlock("crimson_table", () -> createTable(Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_TABLE = registerBlock("warped_table", () -> createTable(Blocks.WARPED_PLANKS));
    public static final Block IRON_TABLE = registerBlock("iron_table", () -> createShelfTable(Blocks.IRON_BLOCK));
    public static final Block GLASS_TABLE = registerBlock("glass_table", () -> createShelfTable(Blocks.IRON_BLOCK));
    public static final Block UNDEAD_TABLE = registerBlock("undead_table", () -> new TableBlock(
            props()
                    .destroyTime(5)
                    .explosionResistance(5)
                    .sound(SoundType.VAULT)
                    .dynamicShape()
    ));

    public static final Block OMINOUS_TABLE = registerBlock("ominous_table", () -> new TableBlock(
            props()
                    .destroyTime(5)
                    .explosionResistance(5)
                    .sound(SoundType.TRIAL_SPAWNER)
                    .dynamicShape()
    ));

    // CHAIRS
    public static final Block OAK_CHAIR = registerBlock("oak_chair", () -> createChair(ChairBlock.Type.OAK, 2, 3, SoundType.WOOD, false, true));
    public static final Block SPRUCE_CHAIR = registerBlock("spruce_chair", () -> createChair(ChairBlock.Type.SPRUCE,  2, 3, SoundType.WOOD, false, true));
    public static final Block BIRCH_CHAIR = registerBlock("birch_chair", () -> createChair(ChairBlock.Type.BIRCH,  2, 3, SoundType.WOOD, false, true));
    public static final Block JUNGLE_CHAIR = registerBlock("jungle_chair", () -> createChair(ChairBlock.Type.JUNGLE,  2, 3, SoundType.WOOD, false, true));
    public static final Block ACACIA_CHAIR = registerBlock("acacia_chair", () -> createChair(ChairBlock.Type.ACACIA,  2, 3, SoundType.WOOD, false, true));
    public static final Block DARK_OAK_CHAIR = registerBlock("dark_oak_chair", () -> createChair(ChairBlock.Type.DARK_OAK,  2, 3, SoundType.WOOD, false, true));
    public static final Block MANGROVE_CHAIR = registerBlock("mangrove_chair", () -> createChair(ChairBlock.Type.MANGROVE,  2, 3, SoundType.WOOD, false, true));
    public static final Block CHERRY_CHAIR = registerBlock("cherry_chair", () -> createChair(ChairBlock.Type.CHERRY,  2, 3, SoundType.CHERRY_WOOD, false, true));
    public static final Block BAMBOO_CHAIR = registerBlock("bamboo_chair", () -> createChair(ChairBlock.Type.BAMBOO,  2, 3, SoundType.BAMBOO_WOOD, false, true));
    public static final Block CRIMSON_CHAIR = registerBlock("crimson_chair", () -> createChair(ChairBlock.Type.CRIMSON,  2, 3, SoundType.NETHER_WOOD, false, false));
    public static final Block WARPED_CHAIR = registerBlock("warped_chair", () -> createChair(ChairBlock.Type.WARPED,  2, 3, SoundType.NETHER_WOOD, false, false));
    public static final Block IRON_CHAIR = registerBlock("iron_chair", () -> createChair(ChairBlock.Type.IRON,  5, 6, SoundType.METAL, true, false));
    public static final Block GLASS_CHAIR = registerBlock("glass_chair", () -> createChair(ChairBlock.Type.GLASS,  5, 6, SoundType.GLASS, true, false));
    public static final Block UNDEAD_CHAIR = registerBlock("undead_chair", () -> createChair(ChairBlock.Type.UNDEAD,  5, 6, SoundType.VAULT, true, false));
    public static final Block OMINOUS_CHAIR = registerBlock("ominous_chair", () -> createChair(ChairBlock.Type.OMINOUS, 5, 6, SoundType.TRIAL_SPAWNER, true, false));

    // WALL CLOCKS
    public static final Block OAK_WALL_CLOCK = registerBlock("oak_wall_clock", () -> createWallClock(WallClockBlock.Type.OAK, Blocks.OAK_PLANKS));
    public static final Block SPRUCE_WALL_CLOCK = registerBlock("spruce_wall_clock", () -> createWallClock(WallClockBlock.Type.SPRUCE, Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_WALL_CLOCK = registerBlock("birch_wall_clock", () -> createWallClock(WallClockBlock.Type.BIRCH, Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_WALL_CLOCK = registerBlock("jungle_wall_clock", () -> createWallClock(WallClockBlock.Type.JUNGLE, Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_WALL_CLOCK = registerBlock("acacia_wall_clock", () -> createWallClock(WallClockBlock.Type.ACACIA, Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_WALL_CLOCK = registerBlock("dark_oak_wall_clock", () -> createWallClock(WallClockBlock.Type.DARK_OAK, Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_WALL_CLOCK = registerBlock("mangrove_wall_clock", () -> createWallClock(WallClockBlock.Type.MANGROVE, Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_WALL_CLOCK = registerBlock("cherry_wall_clock", () -> createWallClock(WallClockBlock.Type.CHERRY, Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_WALL_CLOCK = registerBlock("bamboo_wall_clock", () -> createWallClock(WallClockBlock.Type.BAMBOO, Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_WALL_CLOCK = registerBlock("crimson_wall_clock", () -> createWallClock(WallClockBlock.Type.CRIMSON, Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_WALL_CLOCK = registerBlock("warped_wall_clock", () -> createWallClock(WallClockBlock.Type.WARPED, Blocks.WARPED_PLANKS));
    public static final Block IRON_WALL_CLOCK = registerBlock("iron_wall_clock", () -> createWallClock(WallClockBlock.Type.IRON, Blocks.IRON_BLOCK));
    public static final Block GLASS_WALL_CLOCK = registerBlock("glass_wall_clock", () -> createWallClock(WallClockBlock.Type.GLASS, Blocks.GLASS));
    public static final Block UNDEAD_WALL_CLOCK = registerBlock("undead_wall_clock", () -> new WallClockBlock(WallClockBlock.Type.UNDEAD,
            props().instabreak().sound(SoundType.VAULT).destroyTime(5).explosionResistance(6).requiresCorrectToolForDrops()));
    public static final Block OMINOUS_WALL_CLOCK = registerBlock("ominous_wall_clock", () -> new WallClockBlock(WallClockBlock.Type.OMINOUS,
            props().sound(SoundType.TRIAL_SPAWNER).destroyTime(5).explosionResistance(6).requiresCorrectToolForDrops()));

    // GRANDFATHER CLOCKS
    public static final Block OAK_GRANDFATHER_CLOCK = registerBlock("oak_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.OAK, SoundType.WOOD));
    public static final Block SPRUCE_GRANDFATHER_CLOCK = registerBlock("spruce_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.SPRUCE, SoundType.WOOD));
    public static final Block BIRCH_GRANDFATHER_CLOCK = registerBlock("birch_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.BIRCH, SoundType.WOOD));
    public static final Block JUNGLE_GRANDFATHER_CLOCK = registerBlock("jungle_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.JUNGLE, SoundType.WOOD));
    public static final Block ACACIA_GRANDFATHER_CLOCK = registerBlock("acacia_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.ACACIA, SoundType.WOOD));
    public static final Block DARK_OAK_GRANDFATHER_CLOCK = registerBlock("dark_oak_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.DARK_OAK, SoundType.WOOD));
    public static final Block MANGROVE_GRANDFATHER_CLOCK = registerBlock("mangrove_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.MANGROVE, SoundType.WOOD));
    public static final Block CHERRY_GRANDFATHER_CLOCK = registerBlock("cherry_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.CHERRY, SoundType.CHERRY_WOOD));
    public static final Block BAMBOO_GRANDFATHER_CLOCK = registerBlock("bamboo_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.BAMBOO, SoundType.BAMBOO_WOOD));
    public static final Block CRIMSON_GRANDFATHER_CLOCK = registerBlock("crimson_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.CRIMSON, SoundType.NETHER_WOOD));
    public static final Block WARPED_GRANDFATHER_CLOCK = registerBlock("warped_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.WARPED, SoundType.NETHER_WOOD));
    public static final Block IRON_GRANDFATHER_CLOCK = registerBlock("iron_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.IRON, SoundType.METAL));
    public static final Block GLASS_GRANDFATHER_CLOCK = registerBlock("glass_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.GLASS, SoundType.GLASS));
    public static final Block UNDEAD_GRANDFATHER_CLOCK = registerBlock("undead_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.UNDEAD, SoundType.VAULT));
    public static final Block OMINOUS_GRANDFATHER_CLOCK = registerBlock("ominous_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.OMINOUS, SoundType.TRIAL_SPAWNER));

    // BENCHES
    public static final Block OAK_BENCH = registerBlock("oak_bench", () -> new BenchBlock(copyProps(Blocks.OAK_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block SPRUCE_BENCH = registerBlock("spruce_bench", () -> new BenchBlock(copyProps(Blocks.SPRUCE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block BIRCH_BENCH = registerBlock("birch_bench", () -> new BenchBlock(copyProps(Blocks.BIRCH_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block JUNGLE_BENCH = registerBlock("jungle_bench", () -> new BenchBlock(copyProps(Blocks.JUNGLE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block ACACIA_BENCH = registerBlock("acacia_bench", () -> new BenchBlock(copyProps(Blocks.ACACIA_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block DARK_OAK_BENCH = registerBlock("dark_oak_bench", () -> new BenchBlock(copyProps(Blocks.DARK_OAK_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block MANGROVE_BENCH = registerBlock("mangrove_bench", () -> new BenchBlock(copyProps(Blocks.MANGROVE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block CHERRY_BENCH = registerBlock("cherry_bench", () -> new BenchBlock(copyProps(Blocks.CHERRY_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block BAMBOO_BENCH = registerBlock("bamboo_bench", () -> new BenchBlock(copyProps(Blocks.BAMBOO_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block CRIMSON_BENCH = registerBlock("crimson_bench", () -> new BenchBlock(copyProps(Blocks.CRIMSON_PLANKS).noOcclusion().dynamicShape()));
    public static final Block WARPED_BENCH = registerBlock("warped_bench", () -> new BenchBlock(copyProps(Blocks.WARPED_PLANKS).noOcclusion().dynamicShape()));

    // MIRROR STANDS AND STUMP CHAIR
    public static final Block OAK_MIRROR_STAND = registerBlock("oak_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block SPRUCE_MIRROR_STAND = registerBlock("spruce_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block BIRCH_MIRROR_STAND = registerBlock("birch_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block JUNGLE_MIRROR_STAND = registerBlock("jungle_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block ACACIA_MIRROR_STAND = registerBlock("acacia_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block DARK_OAK_MIRROR_STAND = registerBlock("dark_oak_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block MANGROVE_MIRROR_STAND = registerBlock("mangrove_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block CHERRY_MIRROR_STAND = registerBlock("cherry_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block BAMBOO_MIRROR_STAND = registerBlock("bamboo_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block CRIMSON_MIRROR_STAND = registerBlock("crimson_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block WARPED_MIRROR_STAND = registerBlock("warped_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));
    public static final Block STUMP_CHAIR = registerBlock("stump_chair", () -> new StumpChairBlock(copyProps(Blocks.OAK_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));

    // FOOTSTOOLS
    public static final Block OAK_FOOTSTOOL = registerDyedBlock("oak_footstool", () -> new FootstoolBlock(copyProps(Blocks.OAK_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block SPRUCE_FOOTSTOOL = registerDyedBlock("spruce_footstool", () -> new FootstoolBlock(copyProps(Blocks.SPRUCE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block BIRCH_FOOTSTOOL = registerDyedBlock("birch_footstool", () -> new FootstoolBlock(copyProps(Blocks.BIRCH_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block JUNGLE_FOOTSTOOL = registerDyedBlock("jungle_footstool", () -> new FootstoolBlock(copyProps(Blocks.JUNGLE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block ACACIA_FOOTSTOOL = registerDyedBlock("acacia_footstool", () -> new FootstoolBlock(copyProps(Blocks.ACACIA_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block DARK_OAK_FOOTSTOOL = registerDyedBlock("dark_oak_footstool", () -> new FootstoolBlock(copyProps(Blocks.DARK_OAK_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block MANGROVE_FOOTSTOOL = registerDyedBlock("mangrove_footstool", () -> new FootstoolBlock(copyProps(Blocks.MANGROVE_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block CHERRY_FOOTSTOOL = registerDyedBlock("cherry_footstool", () -> new FootstoolBlock(copyProps(Blocks.CHERRY_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block BAMBOO_FOOTSTOOL = registerDyedBlock("bamboo_footstool", () -> new FootstoolBlock(copyProps(Blocks.BAMBOO_PLANKS).ignitedByLava().noOcclusion().dynamicShape()));
    public static final Block CRIMSON_FOOTSTOOL = registerDyedBlock("crimson_footstool", () -> new FootstoolBlock(copyProps(Blocks.CRIMSON_PLANKS).noOcclusion().dynamicShape()));
    public static final Block WARPED_FOOTSTOOL = registerDyedBlock("warped_footstool", () -> new FootstoolBlock(copyProps(Blocks.WARPED_PLANKS).noOcclusion().dynamicShape()));

    // QUARTZ SET
    public static final Block QUARTZ_COUNTER = registerBlock("quartz_counter", () -> createCounterBlock(Blocks.QUARTZ_BLOCK, true, false));
    public static final Block QUARTZ_STORAGE_COUNTER = registerBlock("quartz_storage_counter", () -> createStorageCounterBlock(Blocks.QUARTZ_BLOCK, true, false));
    public static final Block QUARTZ_SINK_COUNTER = registerBlock("quartz_sink_counter", () -> createSinkCounterBlock(Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_CHAIR = registerBlock("quartz_chair", () -> createChair(ChairBlock.Type.QUARTZ, 0.8f, 4, SoundType.STONE, true, false));
    public static final Block QUARTZ_BENCH = registerBlock("quartz_bench", () -> new BenchBlock(copyProps(Blocks.QUARTZ_BLOCK).requiresCorrectToolForDrops().noOcclusion().dynamicShape()));
    public static final Block QUARTZ_WALL_CLOCK = registerBlock("quartz_wall_clock", () -> createWallClock(WallClockBlock.Type.QUARTZ, Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_GRANDFATHER_CLOCK = registerBlock("quartz_grandfather_clock", () -> createGrandfatherClock(GrandfatherClockBlock.Type.QUARTZ, SoundType.STONE));
    public static final Block QUARTZ_SOFA = registerDyedBlock("quartz_sofa", () -> createSofa(SofaBlock.Type.QUARTZ, Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_COUCH = registerDyedBlock("quartz_couch", () -> createCouch(Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_DESK = registerBlock("quartz_desk", () -> createDesk(Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_DRAWER = registerBlock("quartz_drawer", () -> createDrawer(Blocks.QUARTZ_BLOCK));
    public static final Block QUARTZ_WALL_MIRROR = registerBlock("quartz_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block QUARTZ_MIRROR_STAND = registerBlock("quartz_mirror_stand", () -> new MirrorStandBlock(copyProps(Blocks.GLASS).noOcclusion().dynamicShape()));

    // FRAMED AND STAINED GLASS
    public static final Block GOLD_FRAMED_GLASS = registerBlock("gold_framed_glass", () -> new TransparentBlock(copyProps(Blocks.STAINED_GLASS.black())));
    public static final Block GOLD_FRAMED_GLASS_PANE = registerBlock("gold_framed_glass_pane", () -> new IronBarsBlock(copyProps(Blocks.STAINED_GLASS_PANE.black())));
    public static final Block AUTUMN_STAINED_GLASS = registerBlock("autumn_stained_glass", () -> new TransparentBlock(copyProps(Blocks.STAINED_GLASS.black())));
    public static final Block AUTUMN_STAINED_GLASS_PANE = registerBlock("autumn_stained_glass_pane", () -> new IronBarsBlock(copyProps(Blocks.STAINED_GLASS_PANE.black())));

    // PLANKED WALLS
    public static final Block OAK_PLANKED_WALL = registerBlock("oak_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.OAK_PLANKS)));
    public static final Block SPRUCE_PLANKED_WALL = registerBlock("spruce_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.SPRUCE_PLANKS)));
    public static final Block BIRCH_PLANKED_WALL = registerBlock("birch_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.BIRCH_PLANKS)));
    public static final Block JUNGLE_PLANKED_WALL = registerBlock("jungle_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.JUNGLE_PLANKS)));
    public static final Block ACACIA_PLANKED_WALL = registerBlock("acacia_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.ACACIA_PLANKS)));
    public static final Block DARK_OAK_PLANKED_WALL = registerBlock("dark_oak_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.DARK_OAK_PLANKS)));
    public static final Block MANGROVE_PLANKED_WALL = registerBlock("mangrove_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.MANGROVE_PLANKS)));
    public static final Block CHERRY_PLANKED_WALL = registerBlock("cherry_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.CHERRY_PLANKS)));
    public static final Block BAMBOO_PLANKED_WALL = registerBlock("bamboo_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.BAMBOO_PLANKS)));
    public static final Block CRIMSON_PLANKED_WALL = registerBlock("crimson_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.CRIMSON_PLANKS)));
    public static final Block WARPED_PLANKED_WALL = registerBlock("warped_planked_wall", () -> new PlankedWallBlock(copyProps(Blocks.WARPED_PLANKS)));

    // BEAMS
    public static final Block OAK_BEAM = registerBlock("oak_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.OAK_PLANKS)));
    public static final Block SPRUCE_BEAM = registerBlock("spruce_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.SPRUCE_PLANKS)));
    public static final Block BIRCH_BEAM = registerBlock("birch_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.BIRCH_PLANKS)));
    public static final Block JUNGLE_BEAM = registerBlock("jungle_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.JUNGLE_PLANKS)));
    public static final Block ACACIA_BEAM = registerBlock("acacia_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.ACACIA_PLANKS)));
    public static final Block DARK_OAK_BEAM = registerBlock("dark_oak_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.DARK_OAK_PLANKS)));
    public static final Block MANGROVE_BEAM = registerBlock("mangrove_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.MANGROVE_PLANKS)));
    public static final Block CHERRY_BEAM = registerBlock("cherry_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.CHERRY_PLANKS)));
    public static final Block BAMBOO_BEAM = registerBlock("bamboo_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.BAMBOO_PLANKS)));
    public static final Block CRIMSON_BEAM = registerBlock("crimson_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.CRIMSON_PLANKS)));
    public static final Block WARPED_BEAM = registerBlock("warped_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.WARPED_PLANKS)));
    public static final Block STRIPPED_OAK_BEAM = registerBlock("stripped_oak_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.OAK_PLANKS)));
    public static final Block STRIPPED_SPRUCE_BEAM = registerBlock("stripped_spruce_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.SPRUCE_PLANKS)));
    public static final Block STRIPPED_BIRCH_BEAM = registerBlock("stripped_birch_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.BIRCH_PLANKS)));
    public static final Block STRIPPED_JUNGLE_BEAM = registerBlock("stripped_jungle_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.JUNGLE_PLANKS)));
    public static final Block STRIPPED_ACACIA_BEAM = registerBlock("stripped_acacia_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.ACACIA_PLANKS)));
    public static final Block STRIPPED_DARK_OAK_BEAM = registerBlock("stripped_dark_oak_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.DARK_OAK_PLANKS)));
    public static final Block STRIPPED_MANGROVE_BEAM = registerBlock("stripped_mangrove_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.MANGROVE_PLANKS)));
    public static final Block STRIPPED_CHERRY_BEAM = registerBlock("stripped_cherry_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.CHERRY_PLANKS)));
    public static final Block STRIPPED_BAMBOO_BEAM = registerBlock("stripped_bamboo_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.BAMBOO_PLANKS)));
    public static final Block STRIPPED_CRIMSON_BEAM = registerBlock("stripped_crimson_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.CRIMSON_PLANKS)));
    public static final Block STRIPPED_WARPED_BEAM = registerBlock("stripped_warped_beam", () -> new BeamBlock(0.25F, copyProps(Blocks.WARPED_PLANKS)));

    // TENT
    public static final Block TENT = registerDyedBlock("tent", () -> new TentBlock(copyProps(Blocks.WOOL.white()).noOcclusion().dynamicShape()));

    // TOOL RACKS
    public static final Block OAK_TOOL_RACK = registerBlock("oak_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.OAK_PLANKS).noOcclusion()));
    public static final Block SPRUCE_TOOL_RACK = registerBlock("spruce_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.SPRUCE_PLANKS).noOcclusion()));
    public static final Block BIRCH_TOOL_RACK = registerBlock("birch_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.BIRCH_PLANKS).noOcclusion()));
    public static final Block JUNGLE_TOOL_RACK = registerBlock("jungle_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.JUNGLE_PLANKS).noOcclusion()));
    public static final Block ACACIA_TOOL_RACK = registerBlock("acacia_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.ACACIA_PLANKS).noOcclusion()));
    public static final Block DARK_OAK_TOOL_RACK = registerBlock("dark_oak_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.DARK_OAK_PLANKS).noOcclusion()));
    public static final Block MANGROVE_TOOL_RACK = registerBlock("mangrove_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.MANGROVE_PLANKS).noOcclusion()));
    public static final Block CHERRY_TOOL_RACK = registerBlock("cherry_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.CHERRY_PLANKS).noOcclusion()));
    public static final Block BAMBOO_TOOL_RACK = registerBlock("bamboo_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.BAMBOO_PLANKS).noOcclusion()));
    public static final Block CRIMSON_TOOL_RACK = registerBlock("crimson_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.CRIMSON_PLANKS).noOcclusion()));
    public static final Block WARPED_TOOL_RACK = registerBlock("warped_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.WARPED_PLANKS).noOcclusion()));
    public static final Block IRON_TOOL_RACK = registerBlock("iron_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Block GLASS_TOOL_RACK = registerBlock("glass_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.GLASS).noOcclusion()));
    public static final Block UNDEAD_TOOL_RACK = registerBlock("undead_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.IRON_BLOCK).sound(SoundType.VAULT).noOcclusion()));
    public static final Block OMINOUS_TOOL_RACK = registerBlock("ominous_tool_rack", () -> new ToolRackBlock(copyProps(Blocks.IRON_BLOCK).sound(SoundType.TRIAL_SPAWNER).noOcclusion()));

    // LAMPS
    public static final Block OAK_LAMP = registerDyedBlock("oak_lamp", () -> createGenericLamp());
    public static final Block SPRUCE_LAMP = registerDyedBlock("spruce_lamp", () -> new SpruceLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block BIRCH_LAMP = registerDyedBlock("birch_lamp", () -> createGenericLamp());
    public static final Block JUNGLE_LAMP = registerDyedBlock("jungle_lamp", () -> new JungleLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block ACACIA_LAMP = registerDyedBlock("acacia_lamp", () -> createGenericLamp());
    public static final Block DARK_OAK_LAMP = registerDyedBlock("dark_oak_lamp", () -> new DarkOakLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block MANGROVE_LAMP = registerDyedBlock("mangrove_lamp", () -> new MangroveLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block CHERRY_LAMP = registerDyedBlock("cherry_lamp", () -> createGenericLamp());
    public static final Block BAMBOO_LAMP = registerDyedBlock("bamboo_lamp", () -> new BambooLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block CRIMSON_LAMP = registerBlock("crimson_lamp", () -> new CrimsonLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block WARPED_LAMP = registerBlock("warped_lamp", () -> new WarpedLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block IRON_LAMP = registerDyedBlock("iron_lamp", () -> new IronLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block GLASS_LAMP = registerDyedBlock("glass_lamp", () -> new GlassLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block UNDEAD_LAMP = registerDyedBlock("undead_lamp", () -> new UndeadLampBlock(copyProps(ModBlocks.OAK_LAMP)));
    public static final Block OMINOUS_LAMP = registerBlock("ominous_lamp", () -> new OminousLampBlock(copyProps(ModBlocks.OAK_LAMP)));

    // SOFAS
    public static final Block OAK_SOFA = registerDyedBlock("oak_sofa", () -> createSofa(SofaBlock.Type.OAK, Blocks.OAK_PLANKS));
    public static final Block SPRUCE_SOFA = registerDyedBlock("spruce_sofa", () -> createSofa(SofaBlock.Type.SPRUCE, Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_SOFA = registerDyedBlock("birch_sofa", () -> createSofa(SofaBlock.Type.BIRCH, Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_SOFA = registerDyedBlock("jungle_sofa", () -> createSofa(SofaBlock.Type.JUNGLE, Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_SOFA = registerDyedBlock("acacia_sofa", () -> createSofa(SofaBlock.Type.ACACIA, Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_SOFA = registerDyedBlock("dark_oak_sofa", () -> createSofa(SofaBlock.Type.DARK_OAK, Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_SOFA = registerDyedBlock("mangrove_sofa", () -> createSofa(SofaBlock.Type.MANGROVE, Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_SOFA = registerDyedBlock("cherry_sofa", () -> createSofa(SofaBlock.Type.CHERRY, Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_SOFA = registerDyedBlock("bamboo_sofa", () -> createSofa(SofaBlock.Type.BAMBOO, Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_SOFA = registerDyedBlock("crimson_sofa", () -> createSofa(SofaBlock.Type.CRIMSON, Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_SOFA = registerDyedBlock("warped_sofa", () -> createSofa(SofaBlock.Type.WARPED, Blocks.WARPED_PLANKS));

    // COUCHES
    public static final Block OAK_COUCH = registerDyedBlock("oak_couch", () -> createCouch(Blocks.OAK_PLANKS));
    public static final Block SPRUCE_COUCH = registerDyedBlock("spruce_couch", () -> createCouch(Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_COUCH = registerDyedBlock("birch_couch", () -> createCouch(Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_COUCH = registerDyedBlock("jungle_couch", () -> createCouch(Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_COUCH = registerDyedBlock("acacia_couch", () -> createCouch(Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_COUCH = registerDyedBlock("dark_oak_couch", () -> createCouch(Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_COUCH = registerDyedBlock("mangrove_couch", () -> createCouch(Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_COUCH = registerDyedBlock("cherry_couch", () -> createCouch(Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_COUCH = registerDyedBlock("bamboo_couch", () -> createCouch(Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_COUCH = registerDyedBlock("crimson_couch", () -> createCouch(Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_COUCH = registerDyedBlock("warped_couch", () -> createCouch(Blocks.WARPED_PLANKS));

    // DESKS
    public static final Block OAK_DESK = registerBlock("oak_desk", () -> createDesk(Blocks.OAK_PLANKS));
    public static final Block SPRUCE_DESK = registerBlock("spruce_desk", () -> createDesk(Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_DESK = registerBlock("birch_desk", () -> createDesk(Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_DESK = registerBlock("jungle_desk", () -> createDesk(Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_DESK = registerBlock("acacia_desk", () -> createDesk(Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_DESK = registerBlock("dark_oak_desk", () -> createDesk(Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_DESK = registerBlock("mangrove_desk", () -> createDesk(Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_DESK = registerBlock("cherry_desk", () -> createDesk(Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_DESK = registerBlock("bamboo_desk", () -> createDesk(Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_DESK = registerBlock("crimson_desk", () -> createDesk(Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_DESK = registerBlock("warped_desk", () -> createDesk(Blocks.WARPED_PLANKS));

    // DRAWERS
    public static final Block OAK_DRAWER = registerBlock("oak_drawer", () -> createDrawer(Blocks.OAK_PLANKS));
    public static final Block SPRUCE_DRAWER = registerBlock("spruce_drawer", () -> createDrawer(Blocks.SPRUCE_PLANKS));
    public static final Block BIRCH_DRAWER = registerBlock("birch_drawer", () -> createDrawer(Blocks.BIRCH_PLANKS));
    public static final Block JUNGLE_DRAWER = registerBlock("jungle_drawer", () -> createDrawer(Blocks.JUNGLE_PLANKS));
    public static final Block ACACIA_DRAWER = registerBlock("acacia_drawer", () -> createDrawer(Blocks.ACACIA_PLANKS));
    public static final Block DARK_OAK_DRAWER = registerBlock("dark_oak_drawer", () -> createDrawer(Blocks.DARK_OAK_PLANKS));
    public static final Block MANGROVE_DRAWER = registerBlock("mangrove_drawer", () -> createDrawer(Blocks.MANGROVE_PLANKS));
    public static final Block CHERRY_DRAWER = registerBlock("cherry_drawer", () -> createDrawer(Blocks.CHERRY_PLANKS));
    public static final Block BAMBOO_DRAWER = registerBlock("bamboo_drawer", () -> createDrawer(Blocks.BAMBOO_PLANKS));
    public static final Block CRIMSON_DRAWER = registerBlock("crimson_drawer", () -> createDrawer(Blocks.CRIMSON_PLANKS));
    public static final Block WARPED_DRAWER = registerBlock("warped_drawer", () -> createDrawer(Blocks.WARPED_PLANKS));

    // WALL MIRRORS
    public static final Block OAK_WALL_MIRROR = registerBlock("oak_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block SPRUCE_WALL_MIRROR = registerBlock("spruce_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block BIRCH_WALL_MIRROR = registerBlock("birch_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block JUNGLE_WALL_MIRROR = registerBlock("jungle_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block ACACIA_WALL_MIRROR = registerBlock("acacia_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block DARK_OAK_WALL_MIRROR = registerBlock("dark_oak_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block MANGROVE_WALL_MIRROR = registerBlock("mangrove_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block CHERRY_WALL_MIRROR = registerBlock("cherry_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block BAMBOO_WALL_MIRROR = registerBlock("bamboo_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block CRIMSON_WALL_MIRROR = registerBlock("crimson_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));
    public static final Block WARPED_WALL_MIRROR = registerBlock("warped_wall_mirror", () -> new WallMirrorBlock(copyProps(Blocks.GLASS)));

    // SINKS
    public static final Block STONE_BRICK_SINK = registerBlock("stone_brick_sink", () -> createSink(Blocks.STONE_BRICKS));
    public static final Block MOSSY_STONE_BRICK_SINK = registerBlock("mossy_stone_brick_sink", () -> createSink(Blocks.MOSSY_STONE_BRICKS));
    public static final Block GRANITE_SINK = registerBlock("granite_sink", () -> createSink(Blocks.GRANITE));
    public static final Block DIORITE_SINK = registerBlock("diorite_sink", () -> createSink(Blocks.DIORITE));
    public static final Block ANDESITE_SINK = registerBlock("andesite_sink", () -> createSink(Blocks.ANDESITE));
    public static final Block DEEPSLATE_SINK = registerBlock("deepslate_sink", () -> createSink(Blocks.DEEPSLATE_BRICKS));
    public static final Block CALCITE_SINK = registerBlock("calcite_sink", () -> createSink(Blocks.CALCITE));
    public static final Block TUFF_SINK = registerBlock("tuff_sink", () -> createSink(Blocks.TUFF));
    public static final Block BRICK_SINK = registerBlock("brick_sink", () -> createSink(Blocks.BRICKS));
    public static final Block MUD_SINK = registerBlock("mud_sink", () -> createSink(Blocks.MUD_BRICKS));
    public static final Block SANDSTONE_SINK = registerBlock("sandstone_sink", () -> createSink(Blocks.SANDSTONE));
    public static final Block RED_SANDSTONE_SINK = registerBlock("red_sandstone_sink", () -> createSink(Blocks.RED_SANDSTONE));
    public static final Block PRISMARINE_SINK = registerBlock("prismarine_sink", () -> createSink(Blocks.PRISMARINE));
    public static final Block NETHER_BRICK_SINK = registerBlock("nether_brick_sink", () -> createSink(Blocks.NETHER_BRICKS));
    public static final Block RED_NETHER_BRICK_SINK = registerBlock("red_nether_brick_sink", () -> createSink(Blocks.RED_NETHER_BRICKS));
    public static final Block BLACKSTONE_SINK = registerBlock("blackstone_sink", () -> createSink(Blocks.BLACKSTONE));
    public static final Block ENDSTONE_SINK = registerBlock("endstone_sink", () -> createSink(Blocks.END_STONE));
    public static final Block PURPUR_SINK = registerBlock("purpur_sink", () -> createSink(Blocks.PURPUR_BLOCK));
    public static final Block IRON_SINK = registerBlock("iron_sink", () -> createSink(Blocks.IRON_BLOCK));
    public static final Block GOLD_SINK = registerBlock("gold_sink", () -> createSink(Blocks.GOLD_BLOCK));

    // BATHTUBS
    public static final Block STONE_BRICK_BATHTUB = registerBlock("stone_brick_bathtub", () -> createBathTub(Blocks.STONE_BRICKS));
    public static final Block MOSSY_STONE_BRICK_BATHTUB = registerBlock("mossy_stone_brick_bathtub", () -> createBathTub(Blocks.MOSSY_STONE_BRICKS));
    public static final Block GRANITE_BATHTUB = registerBlock("granite_bathtub", () -> createBathTub(Blocks.GRANITE));
    public static final Block DIORITE_BATHTUB = registerBlock("diorite_bathtub", () -> createBathTub(Blocks.DIORITE));
    public static final Block ANDESITE_BATHTUB = registerBlock("andesite_bathtub", () -> createBathTub(Blocks.ANDESITE));
    public static final Block DEEPSLATE_BATHTUB = registerBlock("deepslate_bathtub", () -> createBathTub(Blocks.DEEPSLATE_BRICKS));
    public static final Block CALCITE_BATHTUB = registerBlock("calcite_bathtub", () -> createBathTub(Blocks.CALCITE));
    public static final Block TUFF_BATHTUB = registerBlock("tuff_bathtub", () -> createBathTub(Blocks.TUFF));
    public static final Block BRICK_BATHTUB = registerBlock("brick_bathtub", () -> createBathTub(Blocks.BRICKS));
    public static final Block MUD_BATHTUB = registerBlock("mud_bathtub", () -> createBathTub(Blocks.MUD_BRICKS));
    public static final Block SANDSTONE_BATHTUB = registerBlock("sandstone_bathtub", () -> createBathTub(Blocks.SANDSTONE));
    public static final Block RED_SANDSTONE_BATHTUB = registerBlock("red_sandstone_bathtub", () -> createBathTub(Blocks.RED_SANDSTONE));
    public static final Block PRISMARINE_BATHTUB = registerBlock("prismarine_bathtub", () -> createBathTub(Blocks.PRISMARINE));
    public static final Block NETHER_BRICK_BATHTUB = registerBlock("nether_brick_bathtub", () -> createBathTub(Blocks.NETHER_BRICKS));
    public static final Block RED_NETHER_BRICK_BATHTUB = registerBlock("red_nether_brick_bathtub", () -> createBathTub(Blocks.RED_NETHER_BRICKS));
    public static final Block BLACKSTONE_BATHTUB = registerBlock("blackstone_bathtub", () -> createBathTub(Blocks.BLACKSTONE));
    public static final Block ENDSTONE_BATHTUB = registerBlock("endstone_bathtub", () -> createBathTub(Blocks.END_STONE));
    public static final Block PURPUR_BATHTUB = registerBlock("purpur_bathtub", () -> createBathTub(Blocks.PURPUR_BLOCK));
    public static final Block IRON_BATHTUB = registerBlock("iron_bathtub", () -> createBathTub(Blocks.IRON_BLOCK));
    public static final Block GOLD_BATHTUB = registerBlock("gold_bathtub", () -> createBathTub(Blocks.GOLD_BLOCK));

    // Fountains
    public static final Block STONE_BRICK_FOUNTAIN = registerBlock("stone_brick_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block MOSSY_STONE_BRICK_FOUNTAIN = registerBlock("mossy_stone_brick_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block GRANITE_FOUNTAIN = registerBlock("granite_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block DIORITE_FOUNTAIN = registerBlock("diorite_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block ANDESITE_FOUNTAIN = registerBlock("andesite_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block DEEPSLATE_FOUNTAIN = registerBlock("deepslate_fountain", () -> createFountain(3,6, SoundType.DEEPSLATE_BRICKS));
    public static final Block CALCITE_FOUNTAIN = registerBlock("calcite_fountain", () -> createFountain(0.75f,0.75f, SoundType.CALCITE));
    public static final Block TUFF_FOUNTAIN = registerBlock("tuff_fountain", () -> createFountain(1.5f,6, SoundType.POLISHED_TUFF));
    public static final Block BRICK_FOUNTAIN = registerBlock("brick_fountain", () -> createFountain(2,6, SoundType.STONE));
    public static final Block MUD_FOUNTAIN = registerBlock("mud_fountain", () -> createFountain(1.5f,3, SoundType.MUD_BRICKS));
    public static final Block SANDSTONE_FOUNTAIN = registerBlock("sandstone_fountain", () -> createFountain(2,6, SoundType.STONE));
    public static final Block RED_SANDSTONE_FOUNTAIN = registerBlock("red_sandstone_fountain", () -> createFountain(2,6, SoundType.STONE));
    public static final Block PRISMARINE_FOUNTAIN = registerBlock("prismarine_fountain", () -> createFountain(1.5f,6, SoundType.STONE));
    public static final Block NETHER_BRICK_FOUNTAIN = registerBlock("nether_brick_fountain", () -> createFountain(2,6, SoundType.NETHER_BRICKS));
    public static final Block RED_NETHER_BRICK_FOUNTAIN = registerBlock("red_nether_brick_fountain", () -> createFountain(2,6, SoundType.NETHER_BRICKS));
    public static final Block BLACKSTONE_FOUNTAIN = registerBlock("blackstone_fountain", () -> createFountain(1.5f,6, SoundType.GILDED_BLACKSTONE));
    public static final Block ENDSTONE_FOUNTAIN = registerBlock("endstone_fountain", () -> createFountain(3,9, SoundType.STONE));
    public static final Block PURPUR_FOUNTAIN = registerBlock("purpur_fountain", () -> createFountain(1.5f,6, SoundType.STONE));

    // Fountains spouts
    public static final Block STONE_BRICK_FOUNTAIN_SPOUT = registerBlock("stone_brick_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block MOSSY_STONE_BRICK_FOUNTAIN_SPOUT = registerBlock("mossy_stone_brick_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block GRANITE_FOUNTAIN_SPOUT = registerBlock("granite_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block DIORITE_FOUNTAIN_SPOUT = registerBlock("diorite_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block ANDESITE_FOUNTAIN_SPOUT = registerBlock("andesite_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block DEEPSLATE_FOUNTAIN_SPOUT = registerBlock("deepslate_fountain_spout", () -> createFountainSpout(3,6, SoundType.DEEPSLATE_BRICKS));
    public static final Block CALCITE_FOUNTAIN_SPOUT = registerBlock("calcite_fountain_spout", () -> createFountainSpout(0.75f,0.75f, SoundType.CALCITE));
    public static final Block TUFF_FOUNTAIN_SPOUT = registerBlock("tuff_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.POLISHED_TUFF));
    public static final Block BRICK_FOUNTAIN_SPOUT = registerBlock("brick_fountain_spout", () -> createFountainSpout(2,6, SoundType.STONE));
    public static final Block MUD_FOUNTAIN_SPOUT = registerBlock("mud_fountain_spout", () -> createFountainSpout(1.5f,3, SoundType.MUD_BRICKS));
    public static final Block SANDSTONE_FOUNTAIN_SPOUT = registerBlock("sandstone_fountain_spout", () -> createFountainSpout(2,6, SoundType.STONE));
    public static final Block RED_SANDSTONE_FOUNTAIN_SPOUT = registerBlock("red_sandstone_fountain_spout", () -> createFountainSpout(2,6, SoundType.STONE));
    public static final Block PRISMARINE_FOUNTAIN_SPOUT = registerBlock("prismarine_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));
    public static final Block NETHER_BRICK_FOUNTAIN_SPOUT = registerBlock("nether_brick_fountain_spout", () -> createFountainSpout(2,6, SoundType.NETHER_BRICKS));
    public static final Block RED_NETHER_BRICK_FOUNTAIN_SPOUT = registerBlock("red_nether_brick_fountain_spout", () -> createFountainSpout(2,6, SoundType.NETHER_BRICKS));
    public static final Block BLACKSTONE_FOUNTAIN_SPOUT = registerBlock("blackstone_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.GILDED_BLACKSTONE));
    public static final Block ENDSTONE_FOUNTAIN_SPOUT = registerBlock("endstone_fountain_spout", () -> createFountainSpout(3,9, SoundType.STONE));
    public static final Block PURPUR_FOUNTAIN_SPOUT = registerBlock("purpur_fountain_spout", () -> createFountainSpout(1.5f,6, SoundType.STONE));

    public static final Block FALLING_LIQUID = registerBlock("falling_liquid", () -> new FallingLiquidBlock(props()
            .replaceable()
            .lightLevel(ModBlockUtilities.createLightLevelFromContainsBlockState(15))));

    // Large Stumps
    public static final Block OAK_LARGE_STUMP = registerBlock("oak_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block SPRUCE_LARGE_STUMP = registerBlock("spruce_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block BIRCH_LARGE_STUMP = registerBlock("birch_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block JUNGLE_LARGE_STUMP = registerBlock("jungle_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block ACACIA_LARGE_STUMP = registerBlock("acacia_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block DARK_OAK_LARGE_STUMP = registerBlock("dark_oak_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block MANGROVE_LARGE_STUMP = registerBlock("mangrove_large_stump", () -> createLargeStump(SoundType.WOOD));
    public static final Block CHERRY_LARGE_STUMP = registerBlock("cherry_large_stump", () -> createLargeStump(SoundType.CHERRY_WOOD));
    public static final Block BAMBOO_LARGE_STUMP = registerBlock("bamboo_large_stump", () -> createLargeStump(SoundType.BAMBOO_WOOD));
    public static final Block CRIMSON_LARGE_STUMP = registerBlock("crimson_large_stump", () -> createLargeStump(SoundType.NETHER_WOOD));
    public static final Block WARPED_LARGE_STUMP = registerBlock("warped_large_stump", () -> createLargeStump(SoundType.NETHER_WOOD));

    // CHIMNEYS
    public static final Block STONE_BRICK_CHIMNEY = registerBlock("stone_brick_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block MOSSY_STONE_BRICK_CHIMNEY = registerBlock("mossy_stone_brick_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block GRANITE_CHIMNEY = registerBlock("granite_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block DIORITE_CHIMNEY = registerBlock("diorite_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block ANDESITE_CHIMNEY = registerBlock("andesite_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block DEEPSLATE_CHIMNEY = registerBlock("deepslate_chimney", () -> createChimney(3,6, SoundType.DEEPSLATE_BRICKS));
    public static final Block CALCITE_CHIMNEY = registerBlock("calcite_chimney", () -> createChimney(0.75f,0.75f, SoundType.CALCITE));
    public static final Block TUFF_CHIMNEY = registerBlock("tuff_chimney", () -> createChimney(1.5f,6, SoundType.POLISHED_TUFF));
    public static final Block BRICK_CHIMNEY = registerBlock("brick_chimney", () -> createChimney(2,6, SoundType.STONE));
    public static final Block MUD_CHIMNEY = registerBlock("mud_chimney", () -> createChimney(1.5f,3, SoundType.MUD_BRICKS));
    public static final Block SANDSTONE_CHIMNEY = registerBlock("sandstone_chimney", () -> createChimney(2,6, SoundType.STONE));
    public static final Block RED_SANDSTONE_CHIMNEY = registerBlock("red_sandstone_chimney", () -> createChimney(2,6, SoundType.STONE));
    public static final Block PRISMARINE_CHIMNEY = registerBlock("prismarine_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block NETHER_BRICK_CHIMNEY = registerBlock("nether_brick_chimney", () -> createChimney(2,6, SoundType.NETHER_BRICKS));
    public static final Block RED_NETHER_BRICK_CHIMNEY = registerBlock("red_nether_brick_chimney", () -> createChimney(2,6, SoundType.NETHER_BRICKS));
    public static final Block BLACKSTONE_CHIMNEY = registerBlock("blackstone_chimney", () -> createChimney(1.5f,6, SoundType.GILDED_BLACKSTONE));
    public static final Block ENDSTONE_CHIMNEY = registerBlock("endstone_chimney", () -> createChimney(3,9, SoundType.STONE));
    public static final Block PURPUR_CHIMNEY = registerBlock("purpur_chimney", () -> createChimney(1.5f,6, SoundType.STONE));
    public static final Block IRON_CHIMNEY = registerBlock("iron_chimney", () -> createChimney(1.5f,6, SoundType.METAL));
    public static final Block GOLD_CHIMNEY = registerBlock("gold_chimney", () -> createChimney(1.5f,6, SoundType.METAL));

    public static final Block TELESCOPE = registerBlock("telescope", () -> new TelescopeBlock(props()
            .instabreak()
            .mapColor(DyeColor.ORANGE)
            .sound(SoundType.COPPER)));

    private static Identifier currentId;

    /** Properties for the block that is currently being registered (26.1 requires the id to be set up front). */
    private static BlockBehaviour.Properties props() {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, currentId));
    }

    private static BlockBehaviour.Properties copyProps(BlockBehaviour block) {
        return BlockBehaviour.Properties.ofFullCopy(block).setId(ResourceKey.create(Registries.BLOCK, currentId));
    }

    private static <T extends Block> T registerBlock(String name, Supplier<T> factory) {
        return registerBlockAndItem(name, factory, false);
    }

    private static <T extends Block> T registerDyedBlock(String name, Supplier<T> factory) {
        return registerBlockAndItem(name, factory, true);
    }

    private static <T extends Block> T registerBlockAndItem(String name, Supplier<T> factory, boolean dyed) {
        Identifier id = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, name);
        currentId = id;
        T block = Registry.register(BuiltInRegistries.BLOCK, id, factory.get());

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
        Item.Properties itemProperties = new Item.Properties().setId(itemKey).useBlockDescriptionPrefix();
        Registry.register(BuiltInRegistries.ITEM, id, dyed ? new DyedBlockItem(block, itemProperties) : new CozyBlockItem(block, itemProperties));
        return block;
    }

    // Registering Blocks
    public static void registerModBlocks(){
        CozyHome.LOGGER.info("Registering ModBlocks for " + CozyHome.MOD_ID);
    }
}

