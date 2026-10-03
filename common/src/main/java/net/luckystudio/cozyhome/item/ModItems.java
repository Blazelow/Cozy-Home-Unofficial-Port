package net.luckystudio.cozyhome.item;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.item.custom.DyeableCushionItem;
import net.luckystudio.cozyhome.item.custom.PaintBrushItem;
public class ModItems {

    // Register Items Here
    public static final Item PAINT_BRUSH = registerItem("paint_brush", new PaintBrushItem(new Item.Properties()));
    public static final Item CUSHION = registerItem("cushion", new DyeableCushionItem(new Item.Properties()));
    public static final Item HAY_CUSHION = registerItem("hay_cushion", new CushionItem(new Item.Properties()));
    public static final Item TRADER_CUSHION = registerItem("trader_cushion", new CushionItem(new Item.Properties().rarity(Rarity.UNCOMMON)));

    // Helper Method to register items
    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CozyHome.LOGGER.info("Registering Mod Items for " + CozyHome.MOD_ID);
    }
}
