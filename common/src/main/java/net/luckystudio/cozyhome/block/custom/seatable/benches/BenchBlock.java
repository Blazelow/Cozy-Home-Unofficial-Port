package net.luckystudio.cozyhome.block.custom.seatable.benches;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.HorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/** A bench with a back rest. Benches placed side by side (facing the same way) join up. */
public class BenchBlock extends Block implements SeatBlock, SimpleWaterloggedBlock {
    public static final MapCodec<BenchBlock> CODEC = simpleCodec(BenchBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<HorizontalLinearConnectionBlock> HORIZONTAL_CONNECTION = ModProperties.HORIZONTAL_CONNECTION;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape NORTH_SHAPE = Shapes.or(
            Block.box(0, 6, 1, 16, 9, 15),
            Block.box(0, 9, 13, 16, 19, 15),
            Block.box(1, 0, 2, 4, 6, 5),
            Block.box(12, 0, 2, 15, 6, 5),
            Block.box(1, 0, 11, 4, 6, 14),
            Block.box(12, 0, 11, 15, 6, 14));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(
            Block.box(0, 6, 1, 16, 9, 15),
            Block.box(0, 9, 1, 16, 19, 3),
            Block.box(1, 0, 2, 4, 6, 5),
            Block.box(12, 0, 2, 15, 6, 5),
            Block.box(1, 0, 11, 4, 6, 14),
            Block.box(12, 0, 11, 15, 6, 14));
    private static final VoxelShape EAST_SHAPE = Shapes.or(
            Block.box(1, 6, 0, 15, 9, 16),
            Block.box(1, 9, 0, 3, 19, 16),
            Block.box(2, 0, 1, 5, 6, 4),
            Block.box(11, 0, 1, 14, 6, 4),
            Block.box(2, 0, 12, 5, 6, 15),
            Block.box(11, 0, 12, 14, 6, 15));
    private static final VoxelShape WEST_SHAPE = Shapes.or(
            Block.box(1, 6, 0, 15, 9, 16),
            Block.box(13, 9, 0, 15, 19, 16),
            Block.box(2, 0, 1, 5, 6, 4),
            Block.box(11, 0, 1, 14, 6, 4),
            Block.box(2, 0, 12, 5, 6, 15),
            Block.box(11, 0, 12, 14, 6, 15));

    public BenchBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HORIZONTAL_CONNECTION, HorizontalLinearConnectionBlock.SINGLE)
                .setValue(TRIGGERED, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HORIZONTAL_CONNECTION, TRIGGERED, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        BlockState state = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(TRIGGERED, false)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        return state.setValue(HORIZONTAL_CONNECTION, getConnection(state, ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return state.setValue(HORIZONTAL_CONNECTION, getConnection(state, world, pos));
    }

    private static HorizontalLinearConnectionBlock getConnection(BlockState state, LevelAccessor world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean left = isMatchingBench(world.getBlockState(pos.relative(facing.getClockWise())), facing);
        boolean right = isMatchingBench(world.getBlockState(pos.relative(facing.getCounterClockWise())), facing);
        if (left && right) return HorizontalLinearConnectionBlock.MIDDLE;
        if (left) return HorizontalLinearConnectionBlock.LEFT;
        if (right) return HorizontalLinearConnectionBlock.RIGHT;
        return HorizontalLinearConnectionBlock.SINGLE;
    }

    private static boolean isMatchingBench(BlockState state, Direction facing) {
        return state.getBlock() instanceof BenchBlock && state.getValue(FACING) == facing;
    }

    private static VoxelShape shapeFor(BlockState state) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapeFor(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapeFor(state);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return SeatBlock.sitDown(state, world, pos, player);
    }

    @Override
    public float getSeatRotation(BlockState state, Level world, BlockPos pos) {
        return state.getValue(FACING).toYRot();
    }

    @Override
    public float getSeatHeight(BlockState state) {
        return 0.55F;
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
