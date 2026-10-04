package net.luckystudio.cozyhome.block.custom.drawers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.interfaces.ImplementedInventory;
public class DrawerBlockEntity extends RandomizableContainerBlockEntity implements MenuProvider, ImplementedInventory {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            DrawerBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            DrawerBlockEntity.this.setOpen(state, true);
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            DrawerBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            DrawerBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        public boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof DrawerScreenHandler) {
                Container inventory = ((DrawerScreenHandler)player.containerMenu).getInventory();
                return inventory == DrawerBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public DrawerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DRAWER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, this.inventory);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, this.inventory);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.cozyhome.drawer");
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new DrawerScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public void startOpen(ContainerUser user) {
        if (!this.remove && !user.getLivingEntity().isSpectator()) {
            this.stateManager.incrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), user.getContainerInteractionRange());
        }
    }

    @Override
    public void stopOpen(ContainerUser user) {
        if (!this.remove && !user.getLivingEntity().isSpectator()) {
            this.stateManager.decrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void tick() {
        if (!this.remove) {
            this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void setOpen(BlockState state, boolean open) {
        assert this.level != null;
        this.level.setBlock(this.getBlockPos(), state.setValue(DrawerBlock.OPEN, open), DrawerBlock.UPDATE_ALL);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(DrawerBlock.FACING).getUnitVec3i();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        assert this.level != null;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
