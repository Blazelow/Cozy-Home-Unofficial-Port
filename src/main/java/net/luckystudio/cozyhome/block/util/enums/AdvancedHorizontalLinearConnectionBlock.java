package net.luckystudio.cozyhome.block.util.enums;

import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
public enum AdvancedHorizontalLinearConnectionBlock implements StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right"),
    LEFT_DIFF("left_diff"),
    MIDDLE_DIFF("middle_diff"),
    RIGHT_DIFF("right_diff"),
    LEFT_DIFF_LEFT("left_diff_left"),
    RIGHT_DIFF_RIGHT("right_diff_right");

    private final String name;

    AdvancedHorizontalLinearConnectionBlock(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static AdvancedHorizontalLinearConnectionBlock setAdvancedHorizontalConnections(BlockState state, LevelAccessor world, BlockPos pos) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        // Determine left and right directions based on the block's facing
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        BlockState stateLeft = world.getBlockState(pos.offset(left));
        BlockState stateRight = world.getBlockState(pos.offset(right));

        if (state.getBlock() instanceof ConnectingBlock connectingBlock) {
            // Check existing connection validity
            if (isLeft(state)) {
                if (connectingBlock.isMatchingBlock(stateLeft) && isMiddle(stateLeft)) {
                    return state.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
                }
            } else if (isMiddle(state)) {
                if (connectingBlock.isMatchingBlock(stateLeft) && connectingBlock.isMatchingBlock(stateRight)) {
                    if (isLeft(stateLeft) && isRight(stateRight)) {
                        return state.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
                    }
                }
            } else if (isRight(state)) {
                if (connectingBlock.isMatchingBlock(stateRight) && isMiddle(stateRight)) {
                    return state.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
                }
            }
        }
        // Determine new connection
        return setConnections(state, world, pos, left, right);
    }

    private static AdvancedHorizontalLinearConnectionBlock setConnections(BlockState state, LevelAccessor world, BlockPos pos, Direction left, Direction right) {
        boolean canConnectLeft = canConnect(state, world, pos, left);
        boolean canConnectRight = canConnect(state, world, pos, right);
        BlockState stateLeft = world.getBlockState(pos.offset(left));
        BlockState stateRight = world.getBlockState(pos.offset(right));
        if (canConnectLeft && canConnectRight) {
            if (isMiddle(stateLeft)) {
                return stateLeft.is(state.getBlock()) ? AdvancedHorizontalLinearConnectionBlock.LEFT : AdvancedHorizontalLinearConnectionBlock.LEFT_DIFF;
            } else if (isMiddle(stateRight)) {
                return stateRight.is(state.getBlock()) ? AdvancedHorizontalLinearConnectionBlock.RIGHT : AdvancedHorizontalLinearConnectionBlock.RIGHT_DIFF;
            } else if (stateLeft.is(state.getBlock()) && stateRight.is(state.getBlock())) {
                return AdvancedHorizontalLinearConnectionBlock.MIDDLE;
            } else if (stateLeft.is(state.getBlock())) {
                return AdvancedHorizontalLinearConnectionBlock.LEFT_DIFF_LEFT;
            } else if (stateRight.is(state.getBlock())) {
                return AdvancedHorizontalLinearConnectionBlock.RIGHT_DIFF_RIGHT;
            } else {
                return AdvancedHorizontalLinearConnectionBlock.MIDDLE_DIFF;
            }
        } else if (canConnectLeft) {
            return stateLeft.is(state.getBlock()) ? AdvancedHorizontalLinearConnectionBlock.LEFT : AdvancedHorizontalLinearConnectionBlock.LEFT_DIFF;
        } else if (canConnectRight) {
            return stateRight.is(state.getBlock()) ? AdvancedHorizontalLinearConnectionBlock.RIGHT : AdvancedHorizontalLinearConnectionBlock.RIGHT_DIFF;
        }
        return AdvancedHorizontalLinearConnectionBlock.SINGLE;
    }

    private static boolean canConnect(BlockState state, LevelAccessor world, BlockPos pos, Direction direction) {
        BlockState neighborState = world.getBlockState(pos.offset(direction));
        BlockState neighborState2 = world.getBlockState(pos.offset(direction,2));
        // Test if the block next to it is already connected to a block
        if (state.getBlock() instanceof ConnectingBlock connectingBlock) {
            if (connectingBlock.isMatchingBlock(neighborState2) && neighborState2.getValue(HorizontalDirectionalBlock.FACING) == state.getValue(HorizontalDirectionalBlock.FACING) && isMiddle(neighborState2)) return false;
            return connectingBlock.isMatchingBlock(neighborState) && neighborState.getValue(HorizontalDirectionalBlock.FACING) == state.getValue(HorizontalDirectionalBlock.FACING);
        }
        return false;
    }

    private static boolean isLeft(BlockState targetState) {
        if (targetState.hasProperty(ModProperties.ADVANCED_HORIZONTAL_CONNECTION)) {
            AdvancedHorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
            return connectionBlock == AdvancedHorizontalLinearConnectionBlock.LEFT || connectionBlock == AdvancedHorizontalLinearConnectionBlock.LEFT_DIFF;
        } else {
            HorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.HORIZONTAL_CONNECTION);
            return connectionBlock == HorizontalLinearConnectionBlock.LEFT;
        }
    }

    private static boolean isRight(BlockState targetState) {
        if (targetState.hasProperty(ModProperties.ADVANCED_HORIZONTAL_CONNECTION)) {
            AdvancedHorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
            return connectionBlock == AdvancedHorizontalLinearConnectionBlock.RIGHT || connectionBlock == AdvancedHorizontalLinearConnectionBlock.RIGHT_DIFF;
        } else {
            HorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.HORIZONTAL_CONNECTION);
            return connectionBlock == HorizontalLinearConnectionBlock.RIGHT;
        }
    }

    private static boolean isMiddle(BlockState targetState) {
        if (targetState.hasProperty(ModProperties.ADVANCED_HORIZONTAL_CONNECTION)) {
            AdvancedHorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.ADVANCED_HORIZONTAL_CONNECTION);
            return connectionBlock == AdvancedHorizontalLinearConnectionBlock.MIDDLE ||
                    connectionBlock == AdvancedHorizontalLinearConnectionBlock.MIDDLE_DIFF ||
                    connectionBlock == AdvancedHorizontalLinearConnectionBlock.LEFT_DIFF_LEFT ||
                    connectionBlock == AdvancedHorizontalLinearConnectionBlock.RIGHT_DIFF_RIGHT;
        } else {
            HorizontalLinearConnectionBlock connectionBlock = targetState.getValue(ModProperties.HORIZONTAL_CONNECTION);
            return connectionBlock == HorizontalLinearConnectionBlock.MIDDLE;
        }
    }
}
