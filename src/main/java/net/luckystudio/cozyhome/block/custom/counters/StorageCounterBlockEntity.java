package net.luckystudio.cozyhome.block.custom.counters;

import net.minecraft.core.HolderLookup;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.interfaces.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
public class StorageCounterBlockEntity extends RandomizableContainerBlockEntity implements MenuProvider, ImplementedInventory {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(24, ItemStack.EMPTY);
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {
        @Override
        protected void onContainerOpen(Level world, BlockPos pos, BlockState state) {
            StorageCounterBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
            StorageCounterBlockEntity.this.setOpen(state, true);
        }

        @Override
        protected void onContainerClose(Level world, BlockPos pos, BlockState state) {
            StorageCounterBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
            StorageCounterBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void onViewerCountUpdate(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        protected boolean isPlayerViewing(Player player) {
            if (player.currentScreenHandler instanceof StorageCounterScreenHandler) {
                Container inventory = ((StorageCounterScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == StorageCounterBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public StorageCounterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.STORAGE_COUNTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.writeNbt(nbt, this.inventory, registryLookup);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.inventory = NonNullList.withSize(this.size(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.readNbt(nbt, this.inventory, registryLookup);
        }
    }

    @Override
    protected Component getContainerName() {
        return Component.translatable("container.cozyhome.storage_counter");
    }

    @Override
    protected NonNullList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected AbstractContainerMenu createScreenHandler(int syncId, Inventory playerInventory) {
        return new StorageCounterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int size() {
        return 24;
    }

    @Override
    public void onOpen(Player player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getLevel(), this.getPos(), this.getBlockState());
        }
    }

    @Override
    public void onClose(Player player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getLevel(), this.getPos(), this.getBlockState());
        }
    }

    public void tick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getLevel(), this.getPos(), this.getBlockState());
        }
    }

    void setOpen(BlockState state, boolean open) {
        assert this.world != null;
        this.world.setBlock(this.getPos(), state.setValue(StorageCounterBlock.OPEN, open), StorageCounterBlock.UPDATE_ALL);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = ((Direction)state.getValue(StorageCounterBlock.FACING)).getNormal();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        assert this.world != null;
        this.world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
}
