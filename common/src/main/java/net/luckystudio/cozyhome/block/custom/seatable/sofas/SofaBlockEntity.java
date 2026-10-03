package net.luckystudio.cozyhome.block.custom.seatable.sofas;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.custom.seatable.SeatWithCushionBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
public class SofaBlockEntity extends SeatWithCushionBlockEntity {

    public SofaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
    }
}
