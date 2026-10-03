package net.luckystudio.cozyhome.block.util.enums;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
public enum HorizontalLinearConnectionBlock implements StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right");

    private final String name;

    HorizontalLinearConnectionBlock(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static HorizontalLinearConnectionBlock setHorizontalConnection(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);

        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        BlockState stateLeft = world.getBlockState(pos.relative(left));
        BlockState stateRight = world.getBlockState(pos.relative(right));

        if (state.getBlock() instanceof ConnectingBlock connectingBlock) {
            if (connectingBlock.isMatchingBlock(stateLeft) && connectingBlock.isMatchingBlock(stateRight)) {
                return HorizontalLinearConnectionBlock.MIDDLE;
            } else if (connectingBlock.isMatchingBlock(stateLeft)) {
                return HorizontalLinearConnectionBlock.LEFT;
            } else if (connectingBlock.isMatchingBlock(stateRight)) {
                return HorizontalLinearConnectionBlock.RIGHT;
            }
        }
        return HorizontalLinearConnectionBlock.SINGLE;
    }
}
