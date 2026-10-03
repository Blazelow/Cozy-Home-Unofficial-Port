package net.luckystudio.cozyhome.block.util.enums;

import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
public enum VerticalLinearConnectionBlock implements StringRepresentable {
    SINGLE("single"),
    HEAD("head"),
    MIDDLE("middle"),
    TAIL("tail");

    private final String name;

    private VerticalLinearConnectionBlock(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static VerticalLinearConnectionBlock setVerticalConnection(BlockState state, LevelAccessor world, BlockPos pos) {
        if (state.getBlock() instanceof ConnectingBlock connectingBlock) {
            boolean isMatchingBlockAbove = connectingBlock.isMatchingBlock(world.getBlockState(pos.above()));
            boolean isMatchingBlockBelow = connectingBlock.isMatchingBlock(world.getBlockState(pos.below()));

            if (isMatchingBlockAbove && isMatchingBlockBelow) return VerticalLinearConnectionBlock.MIDDLE;
            if (isMatchingBlockAbove) return VerticalLinearConnectionBlock.TAIL;
            if (isMatchingBlockBelow) return VerticalLinearConnectionBlock.HEAD;
        }
        return VerticalLinearConnectionBlock.SINGLE;
    }
}
