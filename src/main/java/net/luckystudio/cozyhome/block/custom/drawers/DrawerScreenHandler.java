package net.luckystudio.cozyhome.block.custom.drawers;

import net.luckystudio.cozyhome.block.util.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
public class DrawerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public DrawerScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(9)); // Change size to 9
    }

    public DrawerScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(ModMenuTypes.DRAWER_SCREEN_HANDLER, syncId);
        checkContainerSize(inventory, 9); // Ensure inventory size matches
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        int left_padding = 8;
        int vertical_offset = 36; // Offset to move the custom inventory down by 36 pixels

        // Our inventory: Single row (9 slots)
        for (int l = 0; l < 9; ++l) {
            int x = left_padding + l * 18;
            int y = 17 + vertical_offset; // Add the offset here
            this.addSlot(new Slot(inventory, l, x, y));
        }

        // The player inventory
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (int m = 0; m < 9; ++m) {
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

    public Container getInventory() {
        return this.inventory;
    }
}
