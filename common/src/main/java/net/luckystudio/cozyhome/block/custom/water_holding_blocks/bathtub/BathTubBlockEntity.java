package net.luckystudio.cozyhome.block.custom.water_holding_blocks.bathtub;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.custom.water_holding_blocks.AbstractWaterHoldingBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
public class BathTubBlockEntity extends AbstractWaterHoldingBlockEntity {

    public BathTubBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BATHTUB_BLOCK_ENTITY, pos, state);
    }
}
