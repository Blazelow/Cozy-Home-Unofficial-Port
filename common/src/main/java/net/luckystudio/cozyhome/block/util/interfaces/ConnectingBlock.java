package net.luckystudio.cozyhome.block.util.interfaces;
import net.minecraft.world.level.block.state.BlockState;

public interface ConnectingBlock {
    boolean isMatchingBlock(BlockState targetState);
}
