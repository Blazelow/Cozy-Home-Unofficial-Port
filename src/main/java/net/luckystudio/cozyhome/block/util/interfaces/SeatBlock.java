package net.luckystudio.cozyhome.block.util.interfaces;

import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.entity.custom.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
public interface SeatBlock {

        // This method is used to get the rotation of the player when sitting on the seat
        float getSeatRotation(BlockState state, Level world, BlockPos pos);

        // This method is used to get the height of the seat they are sitting on
        float getSeatHeight(BlockState state);

        // SIT DOWN BOI
        static ItemInteractionResult sitDown(BlockState state, Level world, BlockPos pos, Player player) {
                if (world.isClientSide) return ItemInteractionResult.SUCCESS;
                if (state.getBlock() instanceof SeatBlock seatBlock) {
                        if (!state.getValue(BlockStateProperties.TRIGGERED)) {
                                world.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, true), Block.UPDATE_ALL);
                                // Creates a new entity
                                SeatEntity seat = new SeatEntity(ModEntities.SEAT_ENTITY, world);
                                // Sets it's location
                                seat.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);

                                seat.setYRot(seatBlock.getSeatRotation(state, world, pos));
                                seat.setRot(seatBlock.getSeatRotation(state, world, pos), 0);

                                world.addFreshEntity(seat);

                                player.startRiding(seat);
                                return ItemInteractionResult.SUCCESS;
                        }
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
}
