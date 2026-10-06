package net.luckystudio.cozyhome.block.custom.toolrack;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/** Holds the one tool or weapon that is displayed on a tool rack. */
public class ItemRackBlockEntity extends BlockEntity {
    private ItemStack stack = ItemStack.EMPTY;

    public ItemRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.ITEM_RACK_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.stack.isEmpty()) {
            output.store("item", ItemStack.CODEC, this.stack);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.stack = input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    // This syncs the held item to the client
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    // Drops the displayed item when the rack is broken
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.stack.isEmpty()) {
            Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), this.stack);
        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }
}
