package net.luckystudio.cozyhome.block.custom.lamps;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class GlassLampBlock extends AbstractLampBlock {
    public static final MapCodec<GlassLampBlock> CODEC = simpleCodec(GlassLampBlock::new);
    public static final VoxelShape SINGLE_SHAPE = Shapes.or(
            Block.box(2, 2, 2, 14, 14, 14),
            Block.box(4, 0, 4, 12, 2, 12));
    public static final VoxelShape MIDDLE_SHAPE = Block.box(6, 0, 6, 10, 16, 10);
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(MIDDLE_SHAPE, Block.box(4, 0, 4, 12, 4, 12));
    public GlassLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CONNECTION)) {
            case MIDDLE -> MIDDLE_SHAPE;
            case TAIL -> BOTTOM_SHAPE;
            default -> SINGLE_SHAPE;
        };
    }

    // Add flame particles to the lamp when on
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (state.getValue(CONNECTION) == VerticalLinearConnectionBlock.HEAD || state.getValue(CONNECTION) == VerticalLinearConnectionBlock.SINGLE) {
            if (state.getValue(LIT)) {
                double x = pos.getX() + 0.5D;
                double y = pos.getY() + 0.425D;
                double z = pos.getZ() + 0.5D;
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
