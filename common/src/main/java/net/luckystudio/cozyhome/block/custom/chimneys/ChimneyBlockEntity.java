package net.luckystudio.cozyhome.block.custom.chimneys;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
public class ChimneyBlockEntity extends BlockEntity {
    public ChimneyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CHIMNEY_BLOCK_ENTITY, pos, state);
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, ChimneyBlockEntity campfire) {
        RandomSource random = world.random;
        if (random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                world.addAlwaysVisibleParticle(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        true,
                        (double)pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
                        (double)pos.getY() + random.nextDouble() + random.nextDouble(),
                        (double)pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
                        0.0,
                        0.07,
                        0.0
                );
            }
        }
    }
}
