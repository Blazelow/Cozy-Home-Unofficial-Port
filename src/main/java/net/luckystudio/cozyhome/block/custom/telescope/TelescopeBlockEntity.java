package net.luckystudio.cozyhome.block.custom.telescope;

import net.minecraft.core.HolderLookup;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
public class TelescopeBlockEntity extends BlockEntity {
    public float yaw;
    public float pitch;
    float maxPitchAngle = 60;

    public TelescopeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TELESCOPE_BLOCK_ENTITY, pos, state); // Pass the correct BlockEntityType here
        this.yaw = 0;
        this.pitch = 45;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        updateListeners();
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = Math.clamp(pitch, -maxPitchAngle, maxPitchAngle);
        updateListeners();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putFloat("yaw", this.yaw);
        nbt.putFloat("pitch", this.pitch);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.yaw = nbt.getFloat("yaw");
        this.pitch = nbt.getFloat("pitch");
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
        return createNbt(registryLookup);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag nbt) {
        super.removeComponentsFromTag(nbt);
        nbt.remove("yaw");
        nbt.remove("pitch");
    }

    private void updateListeners() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
}
