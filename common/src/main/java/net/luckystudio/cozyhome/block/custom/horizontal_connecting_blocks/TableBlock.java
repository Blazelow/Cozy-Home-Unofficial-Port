package net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.MapCodec;
public class TableBlock extends AbstractHorizontalConnectingBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<TableBlock> CODEC = simpleCodec(TableBlock::new);

    @Override
    public MapCodec<TableBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape TABLE_TOP = Block.box(0, 14, 0, 16, 16, 16);
    public static final VoxelShape NORTH_WEST_LEG = Block.box(1, 0, 1, 3, 14, 3);
    public static final VoxelShape NORTH_EAST_LEG = Block.box(13, 0, 1, 15, 14, 3);
    public static final VoxelShape SOUTH_WEST_LEG = Block.box(1, 0, 13, 3, 14, 15);
    public static final VoxelShape SOUTH_EAST_LEG = Block.box(13, 0, 13, 15, 14, 15);

    public TableBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = TABLE_TOP;
        // Added lower part of the table.
        shape = Shapes.or(shape, Block.box(
                state.getValue(WEST) ? 0 : 1,
                12,
                state.getValue(NORTH) ? 0 : 1,
                state.getValue(EAST) ? 16 : 15,
                14,
                state.getValue(SOUTH) ? 16 : 15));

        // Adding legs to the table.
        if (state.getValue(NORTH) && state.getValue(WEST) && !state.getValue(NORTH_WEST) || !state.getValue(NORTH) && !state.getValue(WEST)) shape = Shapes.or(shape, NORTH_WEST_LEG);
        if (state.getValue(NORTH) && state.getValue(EAST) && !state.getValue(NORTH_EAST) || !state.getValue(NORTH) && !state.getValue(EAST)) shape = Shapes.or(shape, NORTH_EAST_LEG);
        if (state.getValue(SOUTH) && state.getValue(WEST) && !state.getValue(SOUTH_WEST) || !state.getValue(SOUTH) && !state.getValue(WEST)) shape = Shapes.or(shape, SOUTH_WEST_LEG);
        if (state.getValue(SOUTH) && state.getValue(EAST) && !state.getValue(SOUTH_EAST) || !state.getValue(SOUTH) && !state.getValue(EAST)) shape = Shapes.or(shape, SOUTH_EAST_LEG);

        return shape;
    }
}