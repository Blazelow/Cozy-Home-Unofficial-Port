package net.luckystudio.cozyhome.block.util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

import net.luckystudio.cozyhome.platform.Platform;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreenHandler;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreenHandler;

public class ModMenuTypes {

    public static final MenuType<StorageCounterScreenHandler> STORAGE_COUNTER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "storage_counter"),
            Platform.get().menuType(StorageCounterScreenHandler::new));

    public static final MenuType<DrawerScreenHandler> DRAWER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, "drawer"),
            Platform.get().menuType(DrawerScreenHandler::new));

    public static void registerMenuTypes() {
        CozyHome.LOGGER.info("Registering menu types for " + CozyHome.MOD_ID);
    }
}
