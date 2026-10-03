package net.luckystudio.cozyhome.block.custom.wall_mirrors;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
public class WallMirrorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock{
    public static final MapCodec<WallMirrorBlock> CODEC = createCodec(WallMirrorBlock::new);
    public static final EnumProperty<VerticalLinearConnectionBlock> STACKABLE_BLOCK = ModProperties.VERTICAL_CONNECTION;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape WEST_SHAPE = Shapes.or(
            Block.box(14, 0, 0, 16, 16, 16));
    public static final VoxelShape NORTH_SHAPE = Shapes.or(
            Block.box(0, 0, 14, 16, 16, 16));
    public static final VoxelShape EAST_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 2, 16, 16));
    public static final VoxelShape SOUTH_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 2));

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public WallMirrorBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateManager.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(STACKABLE_BLOCK, VerticalLinearConnectionBlock.SINGLE)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> null;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STACKABLE_BLOCK, WATERLOGGED);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        LevelReader worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }
        return null;
    }
    @Override
    public BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos
    ) {
        // Check if the block can remain in place
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        // Schedule fluid tick if waterlogged
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        // Update stackable state
        updateStackableState(world, pos, state);

        // Return updated state
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    // Helper method to update the stackable state
    private void updateStackableState(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockPos blockPosAbove = pos.above();
        BlockPos blockPosBelow = pos.below();

        BlockState relativeHeadBlock = world.getBlockState(blockPosAbove);
        BlockState relativeTailBlock = world.getBlockState(blockPosBelow);

        VerticalLinearConnectionBlock linearConnectionBlockType = getLinearConnectionBlockType(state, relativeHeadBlock, relativeTailBlock);
        BlockState updatedState = state.setValue(STACKABLE_BLOCK, linearConnectionBlockType);

        world.setBlock(pos, updatedState, 3); // Use flags for block updates
    }

    // Determines the type of connection based on neighbors
    private VerticalLinearConnectionBlock getLinearConnectionBlockType(BlockState state, BlockState blockAbove, BlockState blockBelow) {
        boolean above = blockAbove.getBlock() == state.getBlock() && blockAbove.getValue(FACING) == state.getValue(FACING);
        boolean below = blockBelow.getBlock() == state.getBlock() && blockBelow.getValue(FACING) == state.getValue(FACING);

        if (above && below) return VerticalLinearConnectionBlock.MIDDLE;
        if (above) return VerticalLinearConnectionBlock.TAIL;
        if (below) return VerticalLinearConnectionBlock.HEAD;
        return VerticalLinearConnectionBlock.SINGLE;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
//        boolean type = state.getValue(STACKABLE_BLOCK) != VerticalLinearConnectionBlock.SINGLE;
//        // Check if the world is client-side
//        if (world.isClientSide()) {
//            // This is safe to call only on the client-side
//            Minecraft.getInstance().setScreen(new MirrorScreen(player, type));
//            return InteractionResult.CONSUME;
//        }
//        // If it's the server-side, we don't perform client actions
//        return InteractionResult.SUCCESS;
//    }
}
