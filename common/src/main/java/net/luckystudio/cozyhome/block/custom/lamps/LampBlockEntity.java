package net.luckystudio.cozyhome.block.custom.lamps;

import net.minecraft.core.HolderLookup;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
public class LampBlockEntity extends BlockEntity {
    public float currentOffset;

    public LampBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LAMP_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
        this.currentOffset = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput components) {
        super.applyImplicitComponents(components);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
    }

    // This Syncs the Client and Server
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // This Syncs the Client and Server
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag nbt) {
        super.removeComponentsFromTag(nbt);
    }

}
