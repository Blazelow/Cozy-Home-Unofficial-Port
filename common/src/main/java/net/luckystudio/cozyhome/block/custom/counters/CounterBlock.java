package net.luckystudio.cozyhome.block.custom.counters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink.SinkCounterBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
public class CounterBlock extends Block implements ConnectingBlock {
    public static final MapCodec<CounterBlock> CODEC = simpleCodec(CounterBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    // Setting the pieces of the block
    public static final VoxelShape COUNTER_TOP = Block.box(0, 12, 0, 16, 16, 16);
    public static final VoxelShape NORTH_EAST_INNER = Shapes.or(Block.box(0, 0, 0, 14, 12, 16));
    public static final VoxelShape NORTH_WEST_INNER = Shapes.or(Block.box(2, 0, 0, 16, 12, 16));
    public static final VoxelShape SOUTH_EAST_INNER = Shapes.or(Block.box(2, 0, 0, 16, 12, 16));
    public static final VoxelShape SOUTH_WEST_INNER = Shapes.or(Block.box(0, 0, 0, 14, 12, 16));
    public static final VoxelShape NORTH_EAST_OUTER = Shapes.or(Block.box(2, 0, 0, 16, 12, 14));
    public static final VoxelShape NORTH_WEST_OUTER = Shapes.or(Block.box(0, 0, 0, 14, 12, 14));
    public static final VoxelShape SOUTH_EAST_OUTER = Shapes.or(Block.box(2, 0, 2, 16, 12, 16));
    public static final VoxelShape SOUTH_WEST_OUTER = Shapes.or(Block.box(0, 0, 2, 14, 12, 16));

    // Final Shapes
    public static final VoxelShape NORTH_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 0, 16, 12, 14));
    public static final VoxelShape NORTH_INNER_LEFT = Shapes.or(COUNTER_TOP, NORTH_EAST_INNER);
    public static final VoxelShape NORTH_INNER_RIGHT = Shapes.or(COUNTER_TOP, NORTH_WEST_INNER);
    public static final VoxelShape NORTH_OUTER_LEFT = Shapes.or(COUNTER_TOP, NORTH_WEST_OUTER);
    public static final VoxelShape NORTH_OUTER_RIGHT = Shapes.or(COUNTER_TOP, NORTH_EAST_OUTER);
    public static final VoxelShape EAST_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(2, 0, 0, 16, 12, 16));
    public static final VoxelShape EAST_INNER_LEFT = Shapes.or(COUNTER_TOP, NORTH_WEST_INNER);
    public static final VoxelShape EAST_INNER_RIGHT = Shapes.or(COUNTER_TOP, SOUTH_EAST_INNER);
    public static final VoxelShape EAST_OUTER_LEFT = Shapes.or(COUNTER_TOP, NORTH_EAST_OUTER);
    public static final VoxelShape EAST_OUTER_RIGHT = Shapes.or(COUNTER_TOP, SOUTH_EAST_OUTER);
    public static final VoxelShape SOUTH_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 2, 16, 12, 16));
    public static final VoxelShape SOUTH_INNER_LEFT = Shapes.or(COUNTER_TOP, SOUTH_EAST_INNER);
    public static final VoxelShape SOUTH_INNER_RIGHT = Shapes.or(COUNTER_TOP, SOUTH_WEST_INNER);
    public static final VoxelShape SOUTH_OUTER_LEFT = Shapes.or(COUNTER_TOP, SOUTH_EAST_OUTER);
    public static final VoxelShape SOUTH_OUTER_RIGHT = Shapes.or(COUNTER_TOP, SOUTH_WEST_OUTER);
    public static final VoxelShape WEST_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 0, 14, 12, 16));
    public static final VoxelShape WEST_INNER_LEFT = Shapes.or(COUNTER_TOP, SOUTH_WEST_INNER);
    public static final VoxelShape WEST_INNER_RIGHT = Shapes.or(COUNTER_TOP, NORTH_EAST_INNER);
    public static final VoxelShape WEST_OUTER_LEFT = Shapes.or(COUNTER_TOP, SOUTH_WEST_OUTER);
    public static final VoxelShape WEST_OUTER_RIGHT = Shapes.or(COUNTER_TOP, NORTH_WEST_OUTER);

    public CounterBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, StairsShape.STRAIGHT));
    }

    @Override
    public MapCodec<? extends CounterBlock> codec() {
        return CODEC;
    }

    @Override
    protected int getLightDampening(BlockState state) {
        return super.getLightDampening(state);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return super.getShadeBrightness(state, world, pos);
    }

    private VoxelShape getShape(BlockState state) {
        Direction direction = state.getValue(FACING);
        StairsShape shape = state.getValue(SHAPE);
        return switch (direction) {
            case NORTH -> switch (shape) {
                case STRAIGHT -> NORTH_STRAIGHT;
                case INNER_LEFT -> NORTH_INNER_LEFT;
                case INNER_RIGHT -> NORTH_INNER_RIGHT;
                case OUTER_LEFT -> NORTH_OUTER_LEFT;
                case OUTER_RIGHT -> NORTH_OUTER_RIGHT;
            };
            case EAST -> switch (shape) {
                case STRAIGHT -> EAST_STRAIGHT;
                case INNER_LEFT -> EAST_INNER_LEFT;
                case INNER_RIGHT -> EAST_INNER_RIGHT;
                case OUTER_LEFT -> EAST_OUTER_LEFT;
                case OUTER_RIGHT -> EAST_OUTER_RIGHT;
            };
            case SOUTH -> switch (shape) {
                case STRAIGHT -> SOUTH_STRAIGHT;
                case INNER_LEFT -> SOUTH_INNER_LEFT;
                case INNER_RIGHT -> SOUTH_INNER_RIGHT;
                case OUTER_LEFT -> SOUTH_OUTER_LEFT;
                case OUTER_RIGHT -> SOUTH_OUTER_RIGHT;
            };
            case WEST -> switch (shape) {
                case STRAIGHT -> WEST_STRAIGHT;
                case INNER_LEFT -> WEST_INNER_LEFT;
                case INNER_RIGHT -> WEST_INNER_RIGHT;
                case OUTER_LEFT -> WEST_OUTER_LEFT;
                case OUTER_RIGHT -> WEST_OUTER_RIGHT;
            };
            default -> throw new IllegalStateException("Unexpected value: " + FACING);
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection());
        return blockState.setValue(SHAPE, ModProperties.setStairShapeNoFlip(blockState, ctx.getLevel(), blockPos));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction.getAxis().isHorizontal()
                ? state.setValue(SHAPE, ModProperties.setStairShapeNoFlip(state, world, pos))
                : super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() instanceof CounterBlock ||
                targetState.getBlock() instanceof StorageCounterBlock ||
                targetState.getBlock() instanceof SinkCounterBlock;
    }
}
