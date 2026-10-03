package net.luckystudio.cozyhome.block.util.interfaces;

import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.block.custom.drawers.DeskBlock;
import net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks.TableBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.AdvancedHorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.enums.HorizontalLinearConnectionBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.RotationSegment;
public interface TuckableBlock {
    BooleanProperty TUCKED = ModProperties.TUCKED;

    // This is where we try and tuck the block in.
    static ItemInteractionResult toggleTuck(BlockState state, Level world, BlockPos pos, Player player) {
        if (isFacingDirection(state)) { // Make sure the block is facing a direction.
            // If the block is already tucked, untuck it.
            if (state.getValue(TUCKED)) {
                world.setBlock(pos, state.setValue(TUCKED, false), 3);
                playMoveSound(player, world, pos, state);
                return ItemInteractionResult.SUCCESS;
            }

            boolean isTuckable = !isAnotherTuckedBlockInTheWay(state, world, pos) && canTuckUnderBlockInFront(state, world, pos);

            if (isTuckable) {
                world.setBlock(pos, state.setValue(TUCKED, true), Block.UPDATE_ALL);
                playMoveSound(player, world, pos, state);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    static boolean isFacingDirection(BlockState state) {
        int rotation = state.getValue(BlockStateProperties.ROTATION_16);
        return RotationSegment.convertToDirection(rotation).isPresent();
    }

    static boolean canTuckUnderBlockInFront(BlockState state, Level world, BlockPos pos) {
        BlockState targetState = world.getBlockState(pos.relative(direction(state)));
        // Allow trapdoors to be tucked under if they are the top half and closed.
        if (targetState.getBlock() instanceof TrapDoorBlock && targetState.getValue(BlockStateProperties.HALF) == Half.TOP && !targetState.getValue(BlockStateProperties.OPEN)) return true;
        // Allow desks to be tucked under if they are facing the same direction.
        if (targetState.getBlock() instanceof DeskBlock && targetState.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction(state) && targetState.getValue(ModProperties.HORIZONTAL_CONNECTION) == HorizontalLinearConnectionBlock.MIDDLE) return true;
        // Allow tables to be tucked under.
        if (targetState.getBlock() instanceof TableBlock) return true;
        // Allow blocks that are replaceable or air to be tucked under.
        return targetState.canBeReplaced() || targetState.is(Blocks.AIR);
    }

    // This method will prevent two chairs from tucking into the same block.
    static boolean isAnotherTuckedBlockInTheWay(BlockState state, Level world, BlockPos pos) {
        Direction facing = direction(state);
        BlockPos leftPos = pos.relative(facing).relative(facing.getCounterClockWise());
        BlockPos rightPos = pos.relative(facing).relative(facing.getClockWise());
        BlockState left = world.getBlockState(leftPos);
        BlockState right = world.getBlockState(rightPos);
        if (left.hasProperty(TUCKED)) {
            Direction leftDir = direction(left);
            return left.getValue(TUCKED) && leftDir == facing.getClockWise();
        }
        if (right.hasProperty(TUCKED)) {
            Direction rightDir = direction(right);
            return right.getValue(TUCKED) && rightDir == facing.getCounterClockWise();
        }
        return false;
    }

    static void playMoveSound(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
        // Just alters the pitch when the lamp is being turned on and off.
        float f = state.getValue(TUCKED) ? 1.4F : 1.2F;
        world.playSound(player, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1F, f);
    }

    static Direction direction(BlockState state) {
        int rotation = state.getValue(BlockStateProperties.ROTATION_16);
        return RotationSegment.convertToDirection(rotation).orElse(null);
    }
}
