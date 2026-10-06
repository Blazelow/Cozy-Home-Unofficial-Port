package net.luckystudio.cozyhome.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.interfaces.Strippable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

/**
 * A wooden beam that points along its facing and reaches out to solid blocks and other beams.
 * Strip it with an axe to get the stripped variant.
 */
public class BeamBlock extends PipeBlock implements SimpleWaterloggedBlock, Strippable {
    public static final MapCodec<BeamBlock> CODEC = simpleCodec(settings -> new BeamBlock(0.25F, settings));
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ImmutableMap.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST,
            Direction.UP, UP,
            Direction.DOWN, DOWN
    );

    public BeamBlock(float radius, Properties settings) {
        super(radius, settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        // The base shape is an 8x8x8 cube against the face the beam was placed on, then each connection adds another half
        VoxelShape shape = createFaceCenteredShape(state.getValue(FACING).getOpposite(), 4.0);
        for (Direction direction : DIRECTIONS) {
            if (state.getValue(FACING_PROPERTIES.get(direction))) {
                shape = Shapes.or(shape, createFaceCenteredShape(direction, 4.0));
            }
        }
        return shape;
    }

    private static VoxelShape createFaceCenteredShape(Direction direction, double radius) {
        double min = 8.0 - radius;
        double max = 8.0 + radius;
        return switch (direction) {
            case UP -> Block.box(min, 8.0, min, max, 16.0, max);
            case DOWN -> Block.box(min, 0.0, min, max, 8.0, max);
            case NORTH -> Block.box(min, min, 0.0, max, max, 8.0);
            case SOUTH -> Block.box(min, min, 8.0, max, max, 16.0);
            case WEST -> Block.box(0.0, min, min, 8.0, max, max);
            case EAST -> Block.box(8.0, min, min, 16.0, max, max);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return Strippable.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        BlockState state = this.defaultBlockState()
                .setValue(FACING, ctx.getClickedFace())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return updateConnectionProperties(ctx.getLevel(), ctx.getClickedPos(), state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return updateConnectionProperties(world, pos, state);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public static BlockState updateConnectionProperties(BlockGetter world, BlockPos pos, BlockState state) {
        for (Direction direction : DIRECTIONS) {
            BlockState target = world.getBlockState(pos.relative(direction));
            state = state.setValue(FACING_PROPERTIES.get(direction), isConnectableFace(state, target, world, pos, direction));
        }
        return state;
    }

    private static boolean isConnectableFace(BlockState origin, BlockState target, BlockGetter world, BlockPos pos, Direction direction) {
        Direction facing = origin.getValue(FACING);
        if (facing == direction.getOpposite()) return false;
        if (target.isAir()) return false;

        BlockPos targetPos = pos.relative(direction);
        if (facing == direction) {
            if (target.isFaceSturdy(world, targetPos, direction.getOpposite(), SupportType.CENTER)) return true;
            if (target.isFaceSturdy(world, targetPos, direction.getOpposite())) return true;
            return isLongBeam(target) || isSameAxis(target, direction);
        }

        if (!isLongBeam(origin, world, pos)) return false;
        if (target.isFaceSturdy(world, targetPos, direction.getOpposite(), SupportType.CENTER)) return true;
        return isBeam(target) && isSameAxis(target, direction);
    }

    private static boolean isLongBeam(BlockState origin, BlockGetter world, BlockPos pos) {
        Direction front = origin.getValue(FACING);
        Direction back = front.getOpposite();
        BlockState frontState = world.getBlockState(pos.relative(front));
        BlockState backState = world.getBlockState(pos.relative(back));
        if (frontState.isAir()) return false;

        boolean frontConnected = frontState.isFaceSturdy(world, pos.relative(front), back);
        boolean backConnected = backState.isFaceSturdy(world, pos.relative(back), front) || backState.isAir();

        if (isBeam(frontState) && (isSameAxis(frontState, front) || isLongBeam(frontState))) frontConnected = true;
        if (isBeam(backState) && (isSameAxis(backState, front) || isLongBeam(backState))) backConnected = true;
        return frontConnected && backConnected;
    }

    private static boolean isSameAxis(BlockState target, Direction direction) {
        if (!isBeam(target)) return false;
        Direction targetFacing = target.getValue(FACING);
        return direction == targetFacing || direction == targetFacing.getOpposite();
    }

    /** A beam is "long" when it reaches out of the front of its own facing. */
    private static boolean isLongBeam(BlockState target) {
        return isBeam(target) && target.getValue(FACING_PROPERTIES.get(target.getValue(FACING)));
    }

    private static boolean isBeam(BlockState state) {
        return state.getBlock() instanceof BeamBlock;
    }
}
