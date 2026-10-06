package net.luckystudio.cozyhome.block.custom;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * A stack of planks that looks like a wall of boards. It joins up with the planked walls next to it
 * along the axis it was placed on.
 */
public class PlankedWallBlock extends RotatedPillarBlock {
    public static final MapCodec<PlankedWallBlock> CODEC = simpleCodec(PlankedWallBlock::new);
    public static final EnumProperty<VerticalLinearConnectionBlock> STACKABLE_BLOCK = ModProperties.VERTICAL_CONNECTION;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PlankedWallBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(FACING, Direction.NORTH)
                .setValue(STACKABLE_BLOCK, VerticalLinearConnectionBlock.SINGLE));
    }

    @Override
    protected MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(AXIS, ctx.getClickedFace().getAxis());
        return state.setValue(STACKABLE_BLOCK, getConnection(state, ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STACKABLE_BLOCK, AXIS);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return state.setValue(STACKABLE_BLOCK, getConnection(state, level, pos));
    }

    private static BlockPos neighbor(Direction.Axis axis, BlockPos pos, boolean head) {
        return switch (axis) {
            case X -> head ? pos.east() : pos.west();
            case Y -> head ? pos.above() : pos.below();
            case Z -> head ? pos.north() : pos.south();
        };
    }

    private static VerticalLinearConnectionBlock getConnection(BlockState state, BlockGetter level, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);
        BlockState headState = level.getBlockState(neighbor(axis, pos, true));
        BlockState tailState = level.getBlockState(neighbor(axis, pos, false));
        boolean head = headState.is(state.getBlock()) && headState.getValue(AXIS) == axis;
        boolean tail = tailState.is(state.getBlock()) && tailState.getValue(AXIS) == axis;
        if (head && tail) return VerticalLinearConnectionBlock.MIDDLE;
        if (head) return VerticalLinearConnectionBlock.TAIL;
        if (tail) return VerticalLinearConnectionBlock.HEAD;
        return VerticalLinearConnectionBlock.SINGLE;
    }
}
