package net.luckystudio.cozyhome.block.custom.seatable.sofas;

import net.luckystudio.cozyhome.block.custom.seatable.SeatWithCushionBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
public class SofaBlockEntity extends SeatWithCushionBlockEntity {

    public SofaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SOFA_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
    }
}
