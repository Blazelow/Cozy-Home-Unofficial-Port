package net.luckystudio.cozyhome.block.custom.counters;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.luckystudio.cozyhome.block.util.ModMenuTypes;
public class StorageCounterScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public StorageCounterScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(24));
    }

    public StorageCounterScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(ModMenuTypes.STORAGE_COUNTER_SCREEN_HANDLER, syncId);
        checkContainerSize(inventory, 24);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        int m;
        int l;
        int left_padding = 8;

        // Our inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 8; ++l) {
                // after the 4th slot the slots are shifted to the right by one slot
                int x = l <= 3 ? left_padding + l * 18 : (left_padding + 18) + l * 18;
                int y = 17 + m * 18;

                // x is left and right, y is up and down
                this.addSlot(new Slot(inventory, l + m * 8, x, y));
            }
        }
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

    public Container getInventory() { return this.inventory; }
}