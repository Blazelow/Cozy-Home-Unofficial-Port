package net.luckystudio.cozyhome.block.util;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.custom.counters.StorageCounterScreenHandler;
import net.luckystudio.cozyhome.block.custom.drawers.DrawerScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final MenuType<StorageCounterScreenHandler> STORAGE_COUNTER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "storage_counter"),
            new MenuType<>(StorageCounterScreenHandler::new, FeatureFlags.VANILLA_SET));

    public static final MenuType<DrawerScreenHandler> DRAWER_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, "drawer"),
            new MenuType<>(DrawerScreenHandler::new, FeatureFlags.VANILLA_SET));

    public static void registerMenuTypes() {
        CozyHome.LOGGER.info("Registering menu types for " + CozyHome.MOD_ID);
    }
}
