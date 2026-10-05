package net.luckystudio.cozyhome.block.custom.seatable.couches;

import net.luckystudio.cozyhome.block.custom.seatable.SeatWithCushionBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
public class CouchBlockEntity extends SeatWithCushionBlockEntity {

    public CouchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COUCH_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
    }

}
