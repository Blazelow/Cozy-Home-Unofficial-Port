package net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks;

import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.AllSidesConnectingBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
public abstract class AbstractHorizontalConnectingBlock extends Block implements SimpleWaterloggedBlock, AllSidesConnectingBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty NORTH_EAST = ModProperties.NORTH_EAST;
    public static final BooleanProperty NORTH_WEST = ModProperties.NORTH_WEST;
    public static final BooleanProperty SOUTH_EAST = ModProperties.SOUTH_EAST;
    public static final BooleanProperty SOUTH_WEST = ModProperties.SOUTH_WEST;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public AbstractHorizontalConnectingBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(NORTH, false)
                        .setValue(EAST, false)
                        .setValue(SOUTH, false)
                        .setValue(WEST, false)
                        .setValue(NORTH_EAST, false)
                        .setValue(NORTH_WEST, false)
                        .setValue(SOUTH_EAST, false)
                        .setValue(SOUTH_WEST, false)
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = this.defaultBlockState();
        BlockPos pos = ctx.getClickedPos();
        LevelAccessor world = ctx.getLevel();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        return state
                .setValue(NORTH, checkDirectionalNeighbor(state, Direction.NORTH, world, pos))
                .setValue(EAST, checkDirectionalNeighbor(state, Direction.EAST, world, pos))
                .setValue(SOUTH, checkDirectionalNeighbor(state, Direction.SOUTH, world, pos))
                .setValue(WEST, checkDirectionalNeighbor(state, Direction.WEST, world, pos))
                .setValue(NORTH_EAST, checkDiagonalNeighbor(state, Direction.NORTH, Direction.EAST, world, pos))
                .setValue(NORTH_WEST, checkDiagonalNeighbor(state, Direction.NORTH, Direction.WEST, world, pos))
                .setValue(SOUTH_EAST, checkDiagonalNeighbor(state, Direction.SOUTH, Direction.EAST, world, pos))
                .setValue(SOUTH_WEST, checkDiagonalNeighbor(state, Direction.SOUTH, Direction.WEST, world, pos))
                .setValue(WATERLOGGED, bl);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return state
                .setValue(NORTH, checkDirectionalNeighbor(state, Direction.NORTH, world, pos))
                .setValue(EAST, checkDirectionalNeighbor(state, Direction.EAST, world, pos))
                .setValue(SOUTH, checkDirectionalNeighbor(state, Direction.SOUTH, world, pos))
                .setValue(WEST, checkDirectionalNeighbor(state, Direction.WEST, world, pos))
                .setValue(NORTH_EAST, checkDiagonalNeighbor(state, Direction.NORTH, Direction.EAST, world, pos))
                .setValue(NORTH_WEST, checkDiagonalNeighbor(state, Direction.NORTH, Direction.WEST, world, pos))
                .setValue(SOUTH_EAST, checkDiagonalNeighbor(state, Direction.SOUTH, Direction.EAST, world, pos))
                .setValue(SOUTH_WEST, checkDiagonalNeighbor(state, Direction.SOUTH, Direction.WEST, world, pos));
    }

    private boolean checkDirectionalNeighbor(BlockState state, Direction direction, LevelAccessor world, BlockPos pos) {
        BlockPos targetPos = pos.relative(direction);
        return isMatchingBlock(state, world.getBlockState(targetPos));
    }

    private boolean checkDiagonalNeighbor(BlockState state, Direction direction1, Direction direction2, LevelAccessor world, BlockPos pos) {
        // Ensure both adjacent directions (e.g., NORTH and EAST) are set to true in the state
        BooleanProperty property1 = getDirectionalProperty(direction1);
        BooleanProperty property2 = getDirectionalProperty(direction2);

        if (!state.getValue(property1) || !state.getValue(property2)) return false;

        // Check the diagonal position offset by direction1 and direction2
        BlockPos targetPos = pos.relative(direction1).relative(direction2);
        return isMatchingBlock(state, world.getBlockState(targetPos));
    }

    private BooleanProperty getDirectionalProperty(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> throw new IllegalArgumentException("Invalid direction for diagonal neighbor check");
        };
    }

    @Override
    public boolean isMatchingBlock(BlockState state, BlockState targetState) {
        return targetState.getBlock() == this;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state
                        .setValue(NORTH, state.getValue(SOUTH))
                        .setValue(SOUTH, state.getValue(NORTH))
                        .setValue(EAST, state.getValue(WEST))
                        .setValue(WEST, state.getValue(EAST))
                        .setValue(NORTH_EAST, state.getValue(SOUTH_WEST))
                        .setValue(NORTH_WEST, state.getValue(SOUTH_EAST))
                        .setValue(SOUTH_EAST, state.getValue(NORTH_WEST))
                        .setValue(SOUTH_WEST, state.getValue(NORTH_EAST));
            case COUNTERCLOCKWISE_90:
                return state
                        .setValue(NORTH, state.getValue(EAST))
                        .setValue(SOUTH, state.getValue(WEST))
                        .setValue(EAST, state.getValue(SOUTH))
                        .setValue(WEST, state.getValue(NORTH))
                        .setValue(NORTH_EAST, state.getValue(SOUTH_EAST))
                        .setValue(SOUTH_EAST, state.getValue(SOUTH_WEST))
                        .setValue(SOUTH_WEST, state.getValue(NORTH_WEST))
                        .setValue(NORTH_WEST, state.getValue(NORTH_EAST));
            case CLOCKWISE_90:
                return state
                        .setValue(NORTH, state.getValue(WEST))
                        .setValue(SOUTH, state.getValue(EAST))
                        .setValue(EAST, state.getValue(NORTH))
                        .setValue(WEST, state.getValue(SOUTH))
                        .setValue(NORTH_EAST, state.getValue(NORTH_WEST))
                        .setValue(NORTH_WEST, state.getValue(SOUTH_WEST))
                        .setValue(SOUTH_WEST, state.getValue(SOUTH_EAST))
                        .setValue(SOUTH_EAST, state.getValue(NORTH_EAST));
            default:
                return state;
        }
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state
                        .setValue(NORTH, state.getValue(SOUTH))
                        .setValue(SOUTH, state.getValue(NORTH))
                        .setValue(NORTH_EAST, state.getValue(SOUTH_EAST))
                        .setValue(SOUTH_EAST, state.getValue(NORTH_EAST))
                        .setValue(NORTH_WEST, state.getValue(SOUTH_WEST))
                        .setValue(SOUTH_WEST, state.getValue(NORTH_WEST));
            case FRONT_BACK:
                return state
                        .setValue(EAST, state.getValue(WEST))
                        .setValue(WEST, state.getValue(EAST))
                        .setValue(NORTH_EAST, state.getValue(NORTH_WEST))
                        .setValue(NORTH_WEST, state.getValue(NORTH_EAST))
                        .setValue(SOUTH_EAST, state.getValue(SOUTH_WEST))
                        .setValue(SOUTH_WEST, state.getValue(SOUTH_EAST));
            default:
                return super.mirror(state, mirror);
        }
    }
}
