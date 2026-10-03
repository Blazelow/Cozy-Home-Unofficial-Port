package net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
public class LargeStumpBlock extends AbstractHorizontalConnectingBlock implements ConnectingBlock {

    public static final MapCodec<LargeStumpBlock> CODEC = simpleCodec(LargeStumpBlock::new);

    @Override
    public MapCodec<LargeStumpBlock> codec() {
        return CODEC;
    }

    public static final VoxelShape TOP = Block.box(0, 10, 0, 16, 16, 16);

    public LargeStumpBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = TOP;
        shape = Shapes.or(shape, Block.box(
                state.getValue(WEST) ? 0 : 4,
                0,
                state.getValue(NORTH) ? 0 : 4,
                state.getValue(EAST) ? 16 : 12,
                10,
                state.getValue(SOUTH) ? 16 : 12));

        // Chip away corners based on diagonal connections
        if (!state.getValue(NORTH_EAST)) shape = Shapes.join(shape, Block.box(12, 0, 0, 16, 10, 4), BooleanOp.ONLY_FIRST);
        if (!state.getValue(NORTH_WEST)) shape = Shapes.join(shape, Block.box(0, 0, 0, 4, 10, 4), BooleanOp.ONLY_FIRST);
        if (!state.getValue(SOUTH_EAST)) shape = Shapes.join(shape, Block.box(12, 0, 12, 16, 10, 16), BooleanOp.ONLY_FIRST);
        if (!state.getValue(SOUTH_WEST)) shape = Shapes.join(shape, Block.box(0, 0, 12, 4, 10, 16), BooleanOp.ONLY_FIRST);
        return shape;
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() instanceof LargeStumpBlock;
    }
}
