package net.luckystudio.cozyhome.block.custom.tent;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Only stores the dye colour (as a data component), which the tinted tent model reads. */
public class TentBlockEntity extends BlockEntity {
    public TentBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TENT_BLOCK_ENTITY, pos, state);
    }

    // This syncs the colour to the client
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
