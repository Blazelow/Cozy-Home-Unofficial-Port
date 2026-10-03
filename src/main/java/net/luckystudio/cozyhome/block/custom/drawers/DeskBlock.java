package net.luckystudio.cozyhome.block.custom.drawers;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.HorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class DeskBlock extends Block implements SimpleWaterloggedBlock, ConnectingBlock {
    public static final MapCodec<DeskBlock> CODEC = createCodec(DeskBlock::new);

    public static final EnumProperty<HorizontalLinearConnectionBlock> HORIZONTAL_CONNECTION = ModProperties.HORIZONTAL_CONNECTION;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape DESK_TOP = Block.box(0, 12, 0, 16, 16, 16);
    public static final VoxelShape DESK_BODY = Block.box(1, 4, 1, 15, 12, 15);
    public static final VoxelShape NORTH_WEST_LEG_PIECE = Block.box(1, 0, 1, 4, 4, 4);
    public static final VoxelShape SOUTH_EAST_LEG_PIECE = Block.box(12, 0, 12, 15, 4, 15);
    public static final VoxelShape SOUTH_WEST_LEG_PIECE = Block.box(1, 0, 12, 4, 4, 15);
    public static final VoxelShape NORTH_EAST_LEG_PIECE = Block.box(12, 0, 1, 15, 4, 4);

    public DeskBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateManager.defaultBlockState()
                .setValue(HORIZONTAL_CONNECTION, HorizontalLinearConnectionBlock.SINGLE)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_CONNECTION, FACING, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    private VoxelShape getShape(BlockState state) {
        Direction direction = state.getValue(FACING);
        HorizontalLinearConnectionBlock horz = state.getValue(HORIZONTAL_CONNECTION);
        VoxelShape shape = Shapes.or(DESK_TOP, DESK_BODY);

        // Add the inner cutout based on the direction
        shape = Shapes.combineAndSimplify(shape, Block.box(
                        x1(direction, horz),
                        0,
                        z1(direction, horz),
                        x2(direction, horz),
                        12,
                        z2(direction, horz)),
                BooleanOp.ONLY_FIRST);

        // Adding legs and returning the shape.
        return switch (horz) {
            case SINGLE -> Shapes.or(shape, NORTH_EAST_LEG_PIECE, NORTH_WEST_LEG_PIECE, SOUTH_EAST_LEG_PIECE, SOUTH_WEST_LEG_PIECE);
            case LEFT -> switch (direction) {
                case NORTH -> Shapes.or(shape, NORTH_WEST_LEG_PIECE, SOUTH_WEST_LEG_PIECE);
                case SOUTH -> Shapes.or(shape, NORTH_EAST_LEG_PIECE, SOUTH_EAST_LEG_PIECE);
                case WEST -> Shapes.or(shape, SOUTH_EAST_LEG_PIECE, SOUTH_WEST_LEG_PIECE);
                default -> Shapes.or(shape, NORTH_EAST_LEG_PIECE, NORTH_WEST_LEG_PIECE);
            };
            case RIGHT -> switch (direction) {
                case NORTH -> Shapes.or(shape, NORTH_EAST_LEG_PIECE, SOUTH_EAST_LEG_PIECE);
                case SOUTH -> Shapes.or(shape, NORTH_WEST_LEG_PIECE, SOUTH_WEST_LEG_PIECE);
                case WEST -> Shapes.or(shape, NORTH_WEST_LEG_PIECE, NORTH_EAST_LEG_PIECE);
                default -> Shapes.or(shape, SOUTH_WEST_LEG_PIECE, SOUTH_EAST_LEG_PIECE);
            };
            case MIDDLE -> shape;
        };
    }

    private static int x1(Direction direction, HorizontalLinearConnectionBlock horz) {
        return switch (direction) {
            case NORTH -> switch (horz) {
                case RIGHT, MIDDLE -> 0;
                default -> 4;
            };
            case SOUTH -> switch (horz) {
                case LEFT, MIDDLE -> 0;
                default -> 4;
            };
            case WEST -> 4;
            default -> 0;
        };
    }

    private static int z1(Direction direction, HorizontalLinearConnectionBlock horz) {
        return switch (direction) {
            case EAST -> switch (horz) {
                case RIGHT, MIDDLE -> 0;
                default -> 4;
            };
            case WEST -> switch (horz) {
                case LEFT, MIDDLE -> 0;
                default -> 4;
            };
            case NORTH -> 4;
            default -> 0;
        };
    }

    private static int x2(Direction direction, HorizontalLinearConnectionBlock horz) {
        return switch (direction) {
            case NORTH -> switch (horz) {
                case LEFT, MIDDLE -> 16;
                default -> 12;
            };
            case SOUTH -> switch (horz) {
                case RIGHT, MIDDLE -> 16;
                default -> 12;
            };
            case EAST -> 12;
            default -> 16;
        };
    }

    private static int z2(Direction direction, HorizontalLinearConnectionBlock horz) {
        return switch (direction) {
            case EAST -> switch (horz) {
                case LEFT, MIDDLE -> 16;
                default -> 12;
            };
            case WEST -> switch (horz) {
                case RIGHT, MIDDLE -> 16;
                default -> 12;
            };
            case SOUTH -> 12;
            default -> 16;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        BlockState defaultState = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection()) // Face the player by default
                .setValue(WATERLOGGED, bl);
        return defaultState.setValue(HORIZONTAL_CONNECTION, HorizontalLinearConnectionBlock.setHorizontalConnection(defaultState, ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return state.setValue(HORIZONTAL_CONNECTION, HorizontalLinearConnectionBlock.setHorizontalConnection(state, world, pos));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() instanceof DrawerBlock || targetState.getBlock() instanceof DeskBlock;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}