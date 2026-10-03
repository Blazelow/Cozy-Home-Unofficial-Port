package net.luckystudio.cozyhome.block.custom.seatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;

import net.luckystudio.cozyhome.item.custom.CushionItem;
import org.jetbrains.annotations.Nullable;
public class SeatWithCushionBlockEntity extends BlockEntity implements RandomizableContainer, Container {
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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output) && !this.stack.isEmpty()) {
            output.store("item", ItemStack.CODEC, this.stack);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        if (!this.tryLoadLootTable(input)) {
            this.stack = input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
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
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("item");
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

    public ItemStack getTheItem() {
        this.unpackLootTable(null);
        return this.stack;
    }

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
    public int getMaxStackSize() {
        return 1;
    }

    // One-slot container implementation (the cushion)
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getTheItem().isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? this.getTheItem() : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != 0 || amount <= 0) return ItemStack.EMPTY;
        ItemStack taken = this.getTheItem().split(amount);
        if (!taken.isEmpty()) this.updateListeners();
        return taken;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) return ItemStack.EMPTY;
        ItemStack previous = this.getTheItem();
        this.stack = ItemStack.EMPTY;
        return previous;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) this.setTheItem(stack);
    }

    @Override
    public void clearContent() {
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }
}
