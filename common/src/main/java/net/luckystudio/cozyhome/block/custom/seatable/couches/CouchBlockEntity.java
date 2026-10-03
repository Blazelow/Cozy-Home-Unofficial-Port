package net.luckystudio.cozyhome.block.custom.seatable.couches;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.custom.seatable.SeatWithCushionBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;
public class CouchBlockEntity extends SeatWithCushionBlockEntity {

    public CouchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
    }

}
