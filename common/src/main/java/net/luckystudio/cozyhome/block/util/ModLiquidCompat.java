package net.luckystudio.cozyhome.block.util;

import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;

/**
 * What liquid a bucket, a neighbouring block or a fluid stands for. Honey and chocolate come from the Create mod and
 * are only matched by their registry names, so Create is not needed to run Cozy Home.
 */
public final class ModLiquidCompat {
    private ModLiquidCompat() {}

    private static String idOf(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    /** The liquid inside a filled bucket, or NONE for any other item. */
    public static ContainsBlock getLiquidFromBucket(Item item) {
        if (item == Items.WATER_BUCKET) return ContainsBlock.WATER;
        if (item == Items.LAVA_BUCKET) return ContainsBlock.LAVA;
        String id = idOf(item);
        if (id.equals("create:honey_bucket")) return ContainsBlock.HONEY;
        if (id.equals("create:chocolate_bucket")) return ContainsBlock.CHOCOLATE;
        return ContainsBlock.NONE;
    }

    public static ItemStack getFilledBucket(ContainsBlock contents) {
        return switch (contents) {
            case WATER -> new ItemStack(Items.WATER_BUCKET);
            case LAVA -> new ItemStack(Items.LAVA_BUCKET);
            case HONEY -> createStack("honey_bucket");
            case CHOCOLATE -> createStack("chocolate_bucket");
            default -> ItemStack.EMPTY;
        };
    }

    private static ItemStack createStack(String path) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", path));
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }

    public static SoundEvent getFillSound(ContainsBlock contents) {
        return contents == ContainsBlock.LAVA ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
    }

    public static SoundEvent getEmptySound(ContainsBlock contents) {
        return contents == ContainsBlock.LAVA ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
    }

    /**
     * The liquid a sink or bathtub can pull out of the given neighbouring block, or NONE if it cannot pull from it.
     * Water can come from flowing water since it renews itself. Lava, honey and chocolate are finite, so they have
     * to be real source blocks (or a cauldron).
     */
    public static ContainsBlock getLiquidFromSource(BlockState state) {
        FluidState fluid = state.getFluidState();
        if (fluid.is(FluidTags.WATER)
                || state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)
                || state.getBlock() == Blocks.WATER_CAULDRON) {
            return ContainsBlock.WATER;
        }
        if (state.getBlock() == Blocks.LAVA_CAULDRON) return ContainsBlock.LAVA;
        if (fluid.is(FluidTags.LAVA)) return fluid.isSource() ? ContainsBlock.LAVA : ContainsBlock.NONE;
        if (!fluid.isEmpty() && fluid.isSource()) {
            String id = BuiltInRegistries.FLUID.getKey(fluid.getType()).toString();
            if (id.equals("create:honey")) return ContainsBlock.HONEY;
            if (id.equals("create:chocolate")) return ContainsBlock.CHOCOLATE;
        }
        return ContainsBlock.NONE;
    }

    /** Liquids that are used up when pulled in (the source block is removed) and fill the block at once. */
    public static boolean isFinite(ContainsBlock contents) {
        return contents == ContainsBlock.LAVA || contents == ContainsBlock.HONEY || contents == ContainsBlock.CHOCOLATE;
    }
}
