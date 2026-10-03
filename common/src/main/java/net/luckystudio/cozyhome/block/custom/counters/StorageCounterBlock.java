package net.luckystudio.cozyhome.block.custom.counters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.List;
public class StorageCounterBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<StorageCounterBlock> CODEC = simpleCodec(StorageCounterBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    // BlockBehaviour.Properties Block general shape
    public static final VoxelShape COUNTER_TOP = Block.box(0, 12, 0, 16, 16, 16);

    // Setting Block shape based on direction
    public static final VoxelShape NORTH_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 0, 16, 12, 14));
    public static final VoxelShape EAST_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(2, 0, 0, 16, 12, 16));
    public static final VoxelShape SOUTH_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 2, 16, 12, 16));
    public static final VoxelShape WEST_STRAIGHT = Shapes.or(COUNTER_TOP, Block.box(0, 0, 0, 14, 12, 16));

    @Override
    public MapCodec<? extends StorageCounterBlock> codec() {
        return CODEC;
    }

    public StorageCounterBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition()
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, Boolean.FALSE));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StorageCounterBlockEntity) {
            player.openMenu((StorageCounterBlockEntity)blockEntity);
            player.awardStat(Stats.OPEN_BARREL);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection());
    }

    private VoxelShape getShape(BlockState state) {
        Direction direction = state.getValue(FACING);

        if (direction == Direction.NORTH) return NORTH_STRAIGHT;
        if (direction == Direction.SOUTH) return SOUTH_STRAIGHT;
        if (direction == Direction.WEST) return WEST_STRAIGHT;
        if (direction == Direction.EAST) return EAST_STRAIGHT;

        throw new IllegalStateException("Unexpected value: " + FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StorageCounterBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
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

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StorageCounterBlockEntity) {
            ((StorageCounterBlockEntity)blockEntity).tick();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
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
