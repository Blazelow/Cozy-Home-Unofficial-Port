package net.luckystudio.cozyhome.block.custom.seatable;

import net.minecraft.core.HolderLookup;

import net.luckystudio.cozyhome.item.custom.CushionItem;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
public class SeatWithCushionBlockEntity extends BlockEntity implements RandomizableContainer, ContainerSingleItem.BlockContainerSingleItem {
    protected ResourceKey<LootTable> lootTableId;
    protected long lootTableSeed;
    private ItemStack stack;
    public float currentOffset;

    public SeatWithCushionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.stack = ItemStack.EMPTY;
        this.currentOffset = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.trySaveLootTable(nbt) && !this.stack.isEmpty()) {
            nbt.put("item", this.stack.save(registryLookup));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        if (!this.tryLoadLootTable(nbt)) {
            if (nbt.contains("item", Tag.TAG_COMPOUND)) {
                this.stack = ItemStack.parse(registryLookup, nbt.getCompound("item")).orElse(ItemStack.EMPTY);
            } else {
                this.stack = ItemStack.EMPTY;
            }
        }
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
        nbt.remove("item");
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return this.lootTableId;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTableId = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public ItemStack getTheItem() {
        this.unpackLootTable(null);
        return this.stack;
    }

    @Override
    public void setTheItem(ItemStack stack) {
        this.unpackLootTable(null);
        this.stack = stack;
        updateListeners();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof CushionItem && stack.isEmpty();
    }

    private void updateListeners() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public BlockEntity asBlockEntity() {
        return this;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
