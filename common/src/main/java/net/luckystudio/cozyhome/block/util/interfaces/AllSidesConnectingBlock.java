package net.luckystudio.cozyhome.block.util.interfaces;


import net.minecraft.world.level.block.state.BlockState;
/**
 * Implement into any block that will connect to other blocks
 */
public interface AllSidesConnectingBlock {
    boolean isMatchingBlock(BlockState state, BlockState targetState);
}
