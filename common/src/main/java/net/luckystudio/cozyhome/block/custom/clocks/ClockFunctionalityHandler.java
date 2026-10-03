package net.luckystudio.cozyhome.block.custom.clocks;

import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.util.interfaces.ClockBlock;
import net.luckystudio.cozyhome.util.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/**
 * This class handles all the functionality of a typical clock block of any kind.
 * Inside of
 */
public class ClockFunctionalityHandler {
    public static void handleHandRotations(Level world, BlockPos pos, BlockState state, ClockBlock blockEntity) {
        boolean isNether = world.dimension() == Level.NETHER; // Check if we are in the nether
        if (isNether && !isNetherClock(state)) {
            RandomSource random = world.getRandom();

            if (blockEntity.getTicks() % 20 == 0) {
                blockEntity.setCurrentHourHandAngle(random.nextFloat() * 10.0f - 5.0f);
            }

            blockEntity.setLastHourHandAngle(blockEntity.getCurrentHourHandAngle());
            blockEntity.setCurrentHourHandAngle(
                    wrapAngle(blockEntity.getCurrentHourHandAngle() + blockEntity.getCurrentHourHandAngle())
            );

            blockEntity.setLastMinuteHandAngle(blockEntity.getCurrentMinuteHandAngle());
            float targetMinuteHandAngle = wrapAngle(blockEntity.getCurrentHourHandAngle() * 12.0f);
            blockEntity.setCurrentMinuteHandAngle(
                    lerpWrappedAngle(blockEntity.getCurrentMinuteHandAngle(), targetMinuteHandAngle)
            );
        } else {
            long worldTime = blockEntity.getLevel().getDayTime() % 24000;
            float hour = (worldTime / 1000.0f) % 12;
            float minute = (worldTime % 1000) / 16.6667f;

            blockEntity.setLastHourHandAngle(blockEntity.getCurrentHourHandAngle());
            blockEntity.setCurrentHourHandAngle(wrapAngle(hour * 30.0f + 180.0f));

            blockEntity.setLastMinuteHandAngle(blockEntity.getCurrentMinuteHandAngle());
            blockEntity.setCurrentMinuteHandAngle(wrapAngle(minute * 6.0f));
        }
    }

    private static boolean isNetherClock(BlockState state) {
        return state.is(ModBlocks.CRIMSON_GRANDFATHER_CLOCK) ||
                state.is(ModBlocks.WARPED_GRANDFATHER_CLOCK) ||
                state.is(ModBlocks.CRIMSON_WALL_CLOCK) ||
                state.is(ModBlocks.WARPED_WALL_CLOCK);
    }

    // Wrap angles to keep them in the 0–360 range
    private static float wrapAngle(float angle) {
        return (angle % 360.0f + 360.0f) % 360.0f; // Ensures positive values
    }

    // Interpolates between two angles, wrapping around the 360° boundary smoothly
    private static float lerpWrappedAngle(float from, float to) {
        float diff = wrapAngle(to - from); // Calculate difference, respecting wrap-around
        if (diff > 180.0f) diff -= 360.0f; // Shortest path correction
        if (diff < -180.0f) diff += 360.0f;

        float interpolated = from + diff * (float) 0.2; // Interpolate angle smoothly
        return wrapAngle(interpolated); // Normalize to 0–360 range
    }

    public static void handleGrandfatherClock(Level world, BlockPos pos, BlockState state, ClockBlock blockEntity, float pendulumAmplitude) {
        long worldTime = blockEntity.getLevel().getDayTime() % 24000;

        if (worldTime == 18000 && world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            world.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, true), Block.UPDATE_ALL);
            if (state.getBlock() == ModBlocks.OMINOUS_GRANDFATHER_CLOCK) {
                world.playSound(
                        null, // Null source means it won't be played from a specific entity
                        pos,
                        SoundEvents.VAULT_ACTIVATE,
                        SoundSource.BLOCKS,
                        0.25f, // Volume
                        1.0f  // Pitch
                );
            }
            world.playSound(
                    null, // Null source means it won't be played from a specific entity
                    pos,
                    ModSoundEvents.GRANDFATHER_CLOCK_MIDNIGHT,
                    SoundSource.BLOCKS,
                    1.0f, // Volume
                    1.0f  // Pitch
            );
        }

        if (worldTime == 18360) {
            world.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, false), Block.UPDATE_ALL);
            if (state.getBlock() == ModBlocks.OMINOUS_GRANDFATHER_CLOCK) {
                world.playSound(
                        null, // Null source means it won't be played from a specific entity
                        pos,
                        SoundEvents.VAULT_DEACTIVATE,
                        SoundSource.BLOCKS,
                        1.0f, // Volume
                        1.0f  // Pitch
                );
            }
        }

        // Duration of one full pendulum swing cycle in ticks
        float pendulumCycleDuration = 40.0f; // Adjust as necessary

        // Save the last pendulum angle for smooth transitions
        blockEntity.setLastPendulumAngle(blockEntity.getCurrentPendulumAngle());

        // Calculate the current pendulum angle using a sine wave
        float pendulumAngle = (float) (Math.sin((blockEntity.getTicks() % pendulumCycleDuration)
                / pendulumCycleDuration * Math.PI * 2) * pendulumAmplitude);
        blockEntity.setCurrentPendulumAngle(pendulumAngle);

        // Check if the pendulum passes through the center (0 angle)
        if ((blockEntity.getLastPendulumAngle() > 0 && blockEntity.getCurrentPendulumAngle() <= 0) ||
                (blockEntity.getLastPendulumAngle() < 0 && blockEntity.getCurrentPendulumAngle() >= 0)) {
            // The pendulum has crossed the center, play the ticking sound
            world.playSound(
                    null, // Null source means it won't be played from a specific entity
                    pos,
                    ModSoundEvents.GRANDFATHER_CLOCK_TICK,
                    SoundSource.BLOCKS,
                    0.25f, // Volume
                    1.0f  // Pitch
            );
        }
    }
}
