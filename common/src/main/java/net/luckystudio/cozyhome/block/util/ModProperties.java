package net.luckystudio.cozyhome.block.util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.StairsShape;

import net.luckystudio.cozyhome.block.util.enums.*;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
public class ModProperties {

    public static final EnumProperty<HorizontalLinearConnectionBlock> HORIZONTAL_CONNECTION = EnumProperty.create("horizontal_connection", HorizontalLinearConnectionBlock.class);
    public static final EnumProperty<AdvancedHorizontalLinearConnectionBlock> ADVANCED_HORIZONTAL_CONNECTION = EnumProperty.create("advanced_horizontal_connection", AdvancedHorizontalLinearConnectionBlock.class);
    public static final EnumProperty<VerticalLinearConnectionBlock> VERTICAL_CONNECTION = EnumProperty.create("vertical_connection", VerticalLinearConnectionBlock.class);
    public static final EnumProperty<VerticalWithExtraConnectionBlock> VERTICAL_WITH_EXTRA_CONNECTION = EnumProperty.create("vertical_with_extra_connection", VerticalWithExtraConnectionBlock.class);
    public static final EnumProperty<TripleTallBlock> TRIPLE_TALL_BLOCK = EnumProperty.create("part", TripleTallBlock.class);
    public static final EnumProperty<ContainsBlock> CONTAINS = EnumProperty.create("contains", ContainsBlock.class);

    public static final BooleanProperty HAS_UNDER = BooleanProperty.create("has_under");
    public static final BooleanProperty NORTH_EAST = BooleanProperty.create("north_east");
    public static final BooleanProperty NORTH_WEST = BooleanProperty.create("north_west");
    public static final BooleanProperty SOUTH_EAST = BooleanProperty.create("south_east");
    public static final BooleanProperty SOUTH_WEST = BooleanProperty.create("south_west");
    public static final BooleanProperty TUCKED = BooleanProperty.create("tucked");

    public static final IntegerProperty FILLED_LEVEL_0_2 = IntegerProperty.create("level", 0, 2);
    public static final IntegerProperty FILLED_LEVEL_0_3 = IntegerProperty.create("level", 0, 3);
    public static final EnumProperty<DoubleLongPart> DOUBLE_LONG_PART = EnumProperty.create("part", DoubleLongPart.class);

    public static float setSeatRotationFromFacing(BlockState state) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        return switch (facing) {
            case NORTH -> 180;
            case EAST -> 270;
            case WEST -> 90;
            default -> 0;
        };
    }

    public static float setSeatRotationFromShape(BlockState state) {
        StairsShape stairShape = state.getValue(BlockStateProperties.STAIRS_SHAPE);
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        return switch (stairShape) {
            case INNER_LEFT, OUTER_LEFT -> switch (facing) {
                case NORTH -> 135;
                case EAST -> 225;
                case WEST -> 45;
                default -> 315;
            };
            case INNER_RIGHT, OUTER_RIGHT -> switch (facing) {
                case NORTH -> 225;
                case EAST -> 315;
                case WEST -> 135;
                default -> 45;
            };
            default -> setSeatRotationFromFacing(state);
        };
    }

    public static float setSeatRotationFromRotation(BlockState state) {
        int rotation = state.getValue(BlockStateProperties.ROTATION_16);
        return RotationSegment.convertToDegrees(rotation) + 180;
    }

    public static StairsShape setStairShapeNoFlip(BlockState state, BlockGetter world, BlockPos pos) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockState blockState = world.getBlockState(pos.relative(direction));
        if (state.getBlock() instanceof ConnectingBlock connectingBlock)
        {
            if (connectingBlock.isMatchingBlock(blockState)) {
                Direction direction2 = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()) {
                    if (direction2 == direction.getCounterClockWise()) {
                        return StairsShape.OUTER_LEFT;
                    }

                    return StairsShape.OUTER_RIGHT;
                }
            }
            BlockState blockState2 = world.getBlockState(pos.relative(direction.getOpposite()));
            if (connectingBlock.isMatchingBlock(blockState2)) {
                Direction direction3 = blockState2.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()) {
                    if (direction3 == direction.getCounterClockWise()) {
                        return StairsShape.INNER_LEFT;
                    }
                    return StairsShape.INNER_RIGHT;
                }
            }
        }
        return StairsShape.STRAIGHT;
    }
}
