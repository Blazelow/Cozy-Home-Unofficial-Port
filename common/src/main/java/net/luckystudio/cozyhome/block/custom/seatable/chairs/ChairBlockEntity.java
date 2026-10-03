package net.luckystudio.cozyhome.block.custom.seatable.chairs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.custom.seatable.SeatWithCushionBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
// Copied from the Decorated Pot
public class ChairBlockEntity extends SeatWithCushionBlockEntity {

    public ChairBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CHAIR_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
    }
}
