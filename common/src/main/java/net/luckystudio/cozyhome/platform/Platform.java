package net.luckystudio.cozyhome.platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The few things that cannot be done from shared code because the constructors are not public in vanilla.
 * Each loader module installs its implementation before any content is registered.
 */
public final class Platform {

    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int containerId, Inventory inventory);
    }

    public interface Impl {
        <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntityFactory<T> factory, Block... blocks);

        <T extends AbstractContainerMenu> MenuType<T> menuType(MenuFactory<T> factory);

        boolean isModLoaded(String modId);
    }

    private static Impl impl;

    private Platform() {
    }

    public static void set(Impl implementation) {
        impl = implementation;
    }

    public static Impl get() {
        if (impl == null) throw new IllegalStateException("Platform has not been initialised by the loader");
        return impl;
    }
}
