package net.luckystudio.cozyhome.block.custom.seatable.footstool;

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

/** Only stores the dye colour of the cushion (as a data component), which the tinted model reads. */
public class FootstoolBlockEntity extends BlockEntity {
    public FootstoolBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.FOOTSTOOL_BLOCK_ENTITY, pos, state);
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
