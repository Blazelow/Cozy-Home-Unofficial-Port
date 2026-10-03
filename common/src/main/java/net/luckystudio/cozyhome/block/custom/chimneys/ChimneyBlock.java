package net.luckystudio.cozyhome.block.custom.chimneys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.VerticalWithExtraConnectionBlock;
import org.jetbrains.annotations.Nullable;
public class ChimneyBlock extends BaseEntityBlock {
    public static final MapCodec<ChimneyBlock> CODEC = simpleCodec(ChimneyBlock::new);
    public static final EnumProperty<VerticalWithExtraConnectionBlock> STACKABLE_BLOCK = ModProperties.VERTICAL_WITH_EXTRA_CONNECTION;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final VoxelShape SINGLE = Block.box(0, 0, 0, 16, 16, 16);
    public static final VoxelShape TOP = Shapes.or(
            Block.box(0,4,0,16,16,16),
            Block.box(2, 0, 2, 14, 4, 14));
    public static final VoxelShape MIDDLE = Block.box(2, 0, 2, 14, 16, 14);
    public static final VoxelShape EXTRA = Block.box(0, 0, 0, 16, 16, 16);
    public static final VoxelShape BOTTOM = Shapes.or(
            Block.box(2,4,2,14,16,14),
            Block.box(0, 0, 0, 16, 4, 16));

    @Override
    public MapCodec<? extends ChimneyBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChimneyBlockEntity(pos, state);
    }

    public ChimneyBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STACKABLE_BLOCK, VerticalWithExtraConnectionBlock.HEAD)
                .setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STACKABLE_BLOCK, LIT);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(STACKABLE_BLOCK)) {
            case SINGLE -> SINGLE;
            case HEAD -> TOP;
            case MIDDLE -> MIDDLE;
            case EXTENDED -> EXTRA;
            case TAIL -> BOTTOM;
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(STACKABLE_BLOCK, VerticalWithExtraConnectionBlock.SINGLE)
                .setValue(LIT, isLIT(ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockPos relativeHeadBlockPos = pos.above();
        BlockPos relativeTailBlockPos = pos.below();

        BlockState relativeHeadBlock = world.getBlockState(relativeHeadBlockPos);
        BlockState relativeTailBlock = world.getBlockState(relativeTailBlockPos);

        // Count how many horizontal sides are solid
        int horizontalSides = 0;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockState sideState = world.getBlockState(pos.relative(dir));
            if (sideState.getBlock() != Blocks.AIR) {
                horizontalSides++;
            }
        }

        VerticalWithExtraConnectionBlock LinearConnectionBlockType = getLinearConnectionBlockType(state, relativeHeadBlock, relativeTailBlock, horizontalSides);

        return state.setValue(STACKABLE_BLOCK, LinearConnectionBlockType).setValue(LIT, isLIT(world, pos));
    }

    private boolean isLIT(LevelReader world, BlockPos pos) {
        for (int i = 1; i < 2; i++) {
            BlockPos blockPosBelow = pos.below(i);
            BlockState blockStateBelow = world.getBlockState(blockPosBelow);
            Block blockBelow = blockStateBelow.getBlock();
            if (blockBelow == this) {
                return blockStateBelow.getValue(LIT);
            }
            if (blockBelow instanceof AbstractFurnaceBlock || blockBelow instanceof CampfireBlock) {
                return blockStateBelow.getValue(BlockStateProperties.LIT);
            }
        }
        return false;
    }

    private VerticalWithExtraConnectionBlock getLinearConnectionBlockType(BlockState state, BlockState relativeHeadBlock, BlockState relativeBlockTail , int sides) {
        boolean isHeadBlockConnected = relativeHeadBlock.is(state.getBlock());
        boolean isTailBlockConnected = relativeBlockTail.is(state.getBlock());

        if (sides >= 3 && isTailBlockConnected) return VerticalWithExtraConnectionBlock.EXTENDED;
        if (isHeadBlockConnected && isTailBlockConnected) return VerticalWithExtraConnectionBlock.MIDDLE;
        if (isHeadBlockConnected) return VerticalWithExtraConnectionBlock.TAIL;
        if (isTailBlockConnected) return VerticalWithExtraConnectionBlock.HEAD;
        return VerticalWithExtraConnectionBlock.SINGLE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide() && state.getValue(LIT) && state.getValue(STACKABLE_BLOCK) == VerticalWithExtraConnectionBlock.HEAD ? createTickerHelper(type, ModBlockEntityTypes.CHIMNEY_BLOCK_ENTITY, ChimneyBlockEntity::clientTick) : null;
    }
}
