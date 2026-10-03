package net.luckystudio.cozyhome.block.util;

import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import java.util.List;
import java.util.function.ToIntFunction;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
public class ModBlockUtilities {

    // Returning a light level if the block contain LAVA
    public static ToIntFunction<BlockState> createLightLevelFromContainsBlockState(int litLevel) {
        return state -> state.getValue(ModProperties.CONTAINS) == ContainsBlock.LAVA ? litLevel : 0;
    }

    public static void tryMelt(BlockState state, Level world, BlockPos pos, BlockState getMeltedState) {
        if (world.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(world, pos)) {
            if (world.dimensionType().ultraWarm()) {
                world.removeBlock(pos, false);
            } else {
                world.setBlock(pos, getMeltedState, Block.UPDATE_ALL);
                world.neighborChanged(pos, getMeltedState.getBlock(), pos);
            }
        }
    }

    public static void tryFreezeWater(BlockState state, ServerLevel world, BlockPos pos, BlockState getFrozenState) {
        // Check if the block is water
        Biome biome = world.getBiome(pos).value();
        float temperature = biome.getBaseTemperature();
        if (world.getBrightness(LightLayer.BLOCK, pos) <= 11 - state.getLightBlock(world, pos) && temperature <= 0.15f) {
            if (world.dimensionType().ultraWarm()) {
                world.removeBlock(pos, false);
            } else {
                world.setBlock(pos, getFrozenState, Block.UPDATE_ALL);
                world.neighborChanged(pos, getFrozenState.getBlock(), pos);
            }
        }
    }


    public static boolean isEntityObstructing(Level world, BlockPos pos) {
        AABB box = new AABB(pos);
        List<Entity> entitiesBelow = world.getEntitiesOfClass(Entity.class, box, entity -> true);
        return !entitiesBelow.isEmpty();
    }

    public static boolean canPlaceBelow(Level world, BlockPos pos) {
        return pos.below().getY() > world.getMinBuildHeight() + 1 && world.getBlockState(pos.below()).canBeReplaced();
    }

    public static int getColorFromContainsState(BlockState state, BlockAndTintGetter world, BlockPos pos) {
        if (state.getValue(ModProperties.CONTAINS) == ContainsBlock.WATER){
            return BiomeColors.getWaterColor(world, pos);
        }
        return -17170434;
    }
}
