package net.luckystudio.cozyhome.block.custom.toolrack;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Holds the one tool or weapon that is displayed on a tool rack. */
public class ItemRackBlockEntity extends BlockEntity {
    private ItemStack stack = ItemStack.EMPTY;

    public ItemRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.ITEM_RACK_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.stack.isEmpty()) {
            nbt.put("item", this.stack.save(registryLookup));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.stack = nbt.contains("item", Tag.TAG_COMPOUND)
                ? ItemStack.parse(registryLookup, nbt.getCompound("item")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
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
