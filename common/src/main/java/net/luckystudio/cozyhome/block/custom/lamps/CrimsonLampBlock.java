package net.luckystudio.cozyhome.block.custom.lamps;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
// Copied from net.minecraft.block.TorchBlock and SporeBlossomBlock
public class CrimsonLampBlock extends AbstractLampBlock {
    public static final MapCodec<CrimsonLampBlock> CODEC = simpleCodec(CrimsonLampBlock::new);
    public static final VoxelShape TOP_PIECE = Block.box(2, 8, 2, 14, 14, 14);
    public static final VoxelShape POT = Block.box(4, 0, 4, 12, 6, 12);

    public static final VoxelShape SINGLE_SHAPE = Shapes.or(TOP_PIECE, Block.box(5, 6, 5, 11, 8, 11), POT);
    public static final VoxelShape TOP_SHAPE = Shapes.or(TOP_PIECE, Block.box(5, 0, 5, 11, 8, 11));
    public static final VoxelShape MIDDLE_SHAPE = Block.box(5, 0, 5, 11, 16, 11);
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(POT, Block.box(5, 2, 5, 11, 16, 11));

    public CrimsonLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public boolean isDyeable() {
        return false;
    }

    @Override
    protected MapCodec<? extends CrimsonLampBlock> codec() {
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

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            double d = (double) i + random.nextDouble();
            double e = (double) j + 0.7;
            double f = (double) k + random.nextDouble();
            world.addParticle(ParticleTypes.CRIMSON_SPORE, d, e, f, 0.0, 0.0, 0.0);
        }
    }
}
