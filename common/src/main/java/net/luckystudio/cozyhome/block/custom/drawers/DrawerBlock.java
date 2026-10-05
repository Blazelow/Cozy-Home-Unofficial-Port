package net.luckystudio.cozyhome.block.custom.drawers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.AdvancedHorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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
public class DrawerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, ConnectingBlock {
    public static final MapCodec<DrawerBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(BlockState.CODEC.fieldOf("base_state").forGetter(block -> block.baseBlockState), propertiesCodec())
                    .apply(instance, DrawerBlock::new)
    );

    public static final EnumProperty<AdvancedHorizontalLinearConnectionBlock> HORIZONTAL_CONNECTION = ModProperties.ADVANCED_HORIZONTAL_CONNECTION;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    // Setting the pieces of the block
    public static final VoxelShape TOP_PIECE = Block.box(0, 12, 0, 16, 16, 16);
    public static final VoxelShape NORTH_WEST_LEG_PIECE = Block.box(1, 0, 1, 4, 4, 4);
    public static final VoxelShape SOUTH_EAST_LEG_PIECE = Block.box(12, 0, 12, 15, 4, 15);
    public static final VoxelShape SOUTH_WEST_LEG_PIECE = Block.box(1, 0, 12, 4, 4, 15);
    public static final VoxelShape NORTH_EAST_LEG_PIECE = Block.box(12, 0, 1, 15, 4, 4);

    public static final VoxelShape NORTH_RIGHT_EXTENSION_PIECE = Block.box(15, 4, 1, 16, 12, 4);
    public static final VoxelShape EAST_RIGHT_EXTENSION_PIECE = Block.box(12, 4, 15, 15, 12, 16);
    public static final VoxelShape SOUTH_RIGHT_EXTENSION_PIECE = Block.box(0, 4, 12, 1, 12, 15);
    public static final VoxelShape WEST_RIGHT_EXTENSION_PIECE = Block.box(1, 4, 0, 4, 12, 1);

    public static final VoxelShape NORTH_LEFT_EXTENSION_PIECE = Block.box(0, 4, 1, 1, 12, 4);
    public static final VoxelShape EAST_LEFT_EXTENSION_PIECE = Block.box(12, 4, 0, 15, 12, 1);
    public static final VoxelShape SOUTH_LEFT_EXTENSION_PIECE = Block.box(15, 4, 12, 16, 12, 15);
    public static final VoxelShape WEST_LEFT_EXTENSION_PIECE = Block.box(1, 4, 15, 4, 12, 16);

    public static final VoxelShape SINGLE_SHAPE = Shapes.or(TOP_PIECE, NORTH_WEST_LEG_PIECE, NORTH_EAST_LEG_PIECE, SOUTH_WEST_LEG_PIECE, SOUTH_EAST_LEG_PIECE, Block.box(1, 4, 1, 15, 12, 15));

    public static final VoxelShape NORTH_LEFT_SHAPE = Shapes.or(TOP_PIECE, NORTH_WEST_LEG_PIECE, SOUTH_WEST_LEG_PIECE, Block.box(1, 4, 1, 16, 12, 15));
    public static final VoxelShape EAST_LEFT_SHAPE = Shapes.or(TOP_PIECE, NORTH_EAST_LEG_PIECE, NORTH_WEST_LEG_PIECE, Block.box(1, 4, 1, 15, 12, 16));
    public static final VoxelShape SOUTH_LEFT_SHAPE = Shapes.or(TOP_PIECE, SOUTH_EAST_LEG_PIECE, NORTH_EAST_LEG_PIECE, Block.box(0, 4, 1, 15, 12, 15));
    public static final VoxelShape WEST_LEFT_SHAPE = Shapes.or(TOP_PIECE, SOUTH_WEST_LEG_PIECE, SOUTH_EAST_LEG_PIECE, Block.box(1, 4, 0, 15, 12, 15));

    public static final VoxelShape NORTH_LEFT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, NORTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape EAST_LEFT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, EAST_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape SOUTH_LEFT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, SOUTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape WEST_LEFT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, WEST_RIGHT_EXTENSION_PIECE);

    public static final VoxelShape NORTH_LEFT_DIFF_LEFT_SHAPE = Shapes.or(NORTH_LEFT_SHAPE, NORTH_LEFT_EXTENSION_PIECE);
    public static final VoxelShape EAST_LEFT_DIFF_LEFT_SHAPE = Shapes.or(EAST_LEFT_SHAPE, EAST_LEFT_EXTENSION_PIECE);
    public static final VoxelShape SOUTH_LEFT_DIFF_LEFT_SHAPE = Shapes.or(SOUTH_LEFT_SHAPE, SOUTH_LEFT_EXTENSION_PIECE);
    public static final VoxelShape WEST_LEFT_DIFF_LEFT_SHAPE = Shapes.or(WEST_LEFT_SHAPE, WEST_LEFT_EXTENSION_PIECE);

    public static final VoxelShape NORTH_MIDDLE_SHAPE = Shapes.or(TOP_PIECE, Block.box(0, 4, 1, 16, 12, 15));
    public static final VoxelShape EAST_MIDDLE_SHAPE = Shapes.or(TOP_PIECE, Block.box(1, 4, 0, 15, 12, 16));
    public static final VoxelShape SOUTH_MIDDLE_SHAPE = Shapes.or(TOP_PIECE, Block.box(0, 4, 1, 16, 12, 15));
    public static final VoxelShape WEST_MIDDLE_SHAPE = Shapes.or(TOP_PIECE, Block.box(1, 4, 0, 15, 12, 16));

    public static final VoxelShape NORTH_MIDDLE_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, NORTH_LEFT_EXTENSION_PIECE, NORTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape EAST_MIDDLE_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, EAST_LEFT_EXTENSION_PIECE, EAST_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape SOUTH_MIDDLE_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, SOUTH_LEFT_EXTENSION_PIECE, SOUTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape WEST_MIDDLE_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, WEST_LEFT_EXTENSION_PIECE, WEST_RIGHT_EXTENSION_PIECE);

    public static final VoxelShape NORTH_RIGHT_SHAPE = Shapes.or(TOP_PIECE, SOUTH_EAST_LEG_PIECE, NORTH_EAST_LEG_PIECE, Block.box(0, 4, 1, 15, 12, 15));
    public static final VoxelShape EAST_RIGHT_SHAPE = Shapes.or(TOP_PIECE, SOUTH_WEST_LEG_PIECE, SOUTH_EAST_LEG_PIECE, Block.box(1, 4, 0, 15, 12, 15));
    public static final VoxelShape SOUTH_RIGHT_SHAPE = Shapes.or(TOP_PIECE, NORTH_WEST_LEG_PIECE, SOUTH_WEST_LEG_PIECE, Block.box(1, 4, 1, 16, 12, 15));
    public static final VoxelShape WEST_RIGHT_SHAPE = Shapes.or(TOP_PIECE, NORTH_EAST_LEG_PIECE, NORTH_WEST_LEG_PIECE, Block.box(1, 4, 1, 15, 12, 16));

    public static final VoxelShape NORTH_RIGHT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, NORTH_LEFT_EXTENSION_PIECE);
    public static final VoxelShape EAST_RIGHT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, EAST_LEFT_EXTENSION_PIECE);
    public static final VoxelShape SOUTH_RIGHT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, SOUTH_LEFT_EXTENSION_PIECE);
    public static final VoxelShape WEST_RIGHT_DIFF_SHAPE = Shapes.or(SINGLE_SHAPE, WEST_LEFT_EXTENSION_PIECE);

    public static final VoxelShape NORTH_RIGHT_DIFF_RIGHT_SHAPE = Shapes.or(NORTH_LEFT_SHAPE, NORTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape EAST_RIGHT_DIFF_RIGHT_SHAPE = Shapes.or(EAST_LEFT_SHAPE, EAST_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape SOUTH_RIGHT_DIFF_RIGHT_SHAPE = Shapes.or(SOUTH_LEFT_SHAPE, SOUTH_RIGHT_EXTENSION_PIECE);
    public static final VoxelShape WEST_RIGHT_DIFF_RIGHT_SHAPE = Shapes.or(WEST_LEFT_SHAPE, WEST_RIGHT_EXTENSION_PIECE);

    protected final BlockState baseBlockState;

    public DrawerBlock(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HORIZONTAL_CONNECTION, AdvancedHorizontalLinearConnectionBlock.SINGLE)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, Boolean.FALSE));
        this.baseBlockState = baseBlockState;
    }

    @Override
    public MapCodec<? extends DrawerBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrawerBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_CONNECTION, WATERLOGGED, FACING, OPEN);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        AdvancedHorizontalLinearConnectionBlock connectionBlock = state.getValue(HORIZONTAL_CONNECTION);
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case NORTH -> switch (connectionBlock) {
                case LEFT -> NORTH_LEFT_SHAPE;
                case LEFT_DIFF -> NORTH_LEFT_DIFF_SHAPE;
                case LEFT_DIFF_LEFT -> NORTH_LEFT_DIFF_LEFT_SHAPE;
                case MIDDLE -> NORTH_MIDDLE_SHAPE;
                case MIDDLE_DIFF -> NORTH_MIDDLE_DIFF_SHAPE;
                case RIGHT -> NORTH_RIGHT_SHAPE;
                case RIGHT_DIFF -> NORTH_RIGHT_DIFF_SHAPE;
                case RIGHT_DIFF_RIGHT -> NORTH_RIGHT_DIFF_RIGHT_SHAPE;
                default -> SINGLE_SHAPE;
            };
            case EAST -> switch (connectionBlock) {
                case LEFT -> EAST_LEFT_SHAPE;
                case LEFT_DIFF -> EAST_LEFT_DIFF_SHAPE;
                case LEFT_DIFF_LEFT -> EAST_LEFT_DIFF_LEFT_SHAPE;
                case MIDDLE -> EAST_MIDDLE_SHAPE;
                case MIDDLE_DIFF -> EAST_MIDDLE_DIFF_SHAPE;
                case RIGHT -> EAST_RIGHT_SHAPE;
                case RIGHT_DIFF -> EAST_RIGHT_DIFF_SHAPE;
                case RIGHT_DIFF_RIGHT -> EAST_RIGHT_DIFF_RIGHT_SHAPE;
                default -> SINGLE_SHAPE;
            };
            case SOUTH -> switch (connectionBlock) {
                case LEFT -> SOUTH_LEFT_SHAPE;
                case LEFT_DIFF -> SOUTH_LEFT_DIFF_SHAPE;
                case LEFT_DIFF_LEFT -> SOUTH_LEFT_DIFF_LEFT_SHAPE;
                case MIDDLE -> SOUTH_MIDDLE_SHAPE;
                case MIDDLE_DIFF -> SOUTH_MIDDLE_DIFF_SHAPE;
                case RIGHT -> SOUTH_RIGHT_SHAPE;
                case RIGHT_DIFF -> SOUTH_RIGHT_DIFF_SHAPE;
                case RIGHT_DIFF_RIGHT -> SOUTH_RIGHT_DIFF_RIGHT_SHAPE;
                default -> SINGLE_SHAPE;
            };
            case WEST -> switch (connectionBlock) {
                case LEFT -> WEST_LEFT_SHAPE;
                case LEFT_DIFF -> WEST_LEFT_DIFF_SHAPE;
                case LEFT_DIFF_LEFT -> WEST_LEFT_DIFF_LEFT_SHAPE;
                case MIDDLE -> WEST_MIDDLE_SHAPE;
                case MIDDLE_DIFF -> WEST_MIDDLE_DIFF_SHAPE;
                case RIGHT -> WEST_RIGHT_SHAPE;
                case RIGHT_DIFF -> WEST_RIGHT_DIFF_SHAPE;
                case RIGHT_DIFF_RIGHT -> WEST_RIGHT_DIFF_RIGHT_SHAPE;
                default -> SINGLE_SHAPE;
            };
            default -> SINGLE_SHAPE;
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        BlockState defaultState = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection()) // Face the player by default
                .setValue(WATERLOGGED, bl);
        return defaultState;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return state.setValue(HORIZONTAL_CONNECTION, AdvancedHorizontalLinearConnectionBlock.setAdvancedHorizontalConnections(state, world, pos));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity) {
            player.openMenu((DrawerBlockEntity)blockEntity);
            player.awardStat(Stats.OPEN_BARREL);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() == newState.getBlock()) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity boxBlockEntity) {
            Containers.dropContents(world, pos, boxBlockEntity);
            // update comparators
            world.updateNeighbourForOutputSignal(pos,this);
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity) {
            ((DrawerBlockEntity)blockEntity).tick();
        }
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
