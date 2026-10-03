package net.luckystudio.cozyhome.block.custom.lamps;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class SpruceLampBlock extends AbstractLampBlock {
    public static final MapCodec<SpruceLampBlock> CODEC = createCodec(SpruceLampBlock::new);
    public static final VoxelShape TOP_PIECE = Block.box(2, 4, 2, 14, 14, 14);
    public static final VoxelShape BOTTOM_PIECE = Block.box(4, 0, 4, 12, 2, 12);

    public static final VoxelShape SINGLE_SHAPE = Shapes.or(TOP_PIECE, BOTTOM_PIECE);
    public static final VoxelShape TOP_SHAPE = Shapes.or(TOP_PIECE, Block.box(6, 0, 6, 10, 4, 10));
    public static final VoxelShape MIDDLE_SHAPE = Block.box(6, 0, 6, 10, 16, 10);
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(BOTTOM_PIECE, Block.box(6, 2, 6, 10, 16, 10));

    public SpruceLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CONNECTION)) {
            case HEAD -> TOP_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case TAIL -> BOTTOM_SHAPE;
            default -> SINGLE_SHAPE;
        };
    }
}
