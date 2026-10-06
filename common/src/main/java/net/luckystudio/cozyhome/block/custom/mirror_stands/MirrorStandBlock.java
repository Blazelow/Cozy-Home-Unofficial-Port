package net.luckystudio.cozyhome.block.custom.mirror_stands;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.client.MirrorClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/** A standing mirror. Right-click it with an empty hand to see yourself in full. */
public class MirrorStandBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<MirrorStandBlock> CODEC = simpleCodec(MirrorStandBlock::new);

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 2, 16, 16, 15);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 1, 16, 16, 14);
    private static final VoxelShape EAST_SHAPE = Block.box(1, 0, 0, 14, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(2, 0, 0, 15, 16, 16);

    public MirrorStandBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        // Only with an empty hand, so held blocks can still be placed against the mirror
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (world.isClientSide()) {
            MirrorClientHooks.openMirror(player, true);
        }
        return InteractionResult.SUCCESS;
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
