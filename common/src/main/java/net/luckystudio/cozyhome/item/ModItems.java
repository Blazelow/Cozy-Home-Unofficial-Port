package net.luckystudio.cozyhome.item;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.item.custom.DyeableCushionItem;
import net.luckystudio.cozyhome.item.custom.PaintBrushItem;
public class ModItems {

    // Register Items Here
    public static final Item PAINT_BRUSH = registerItem("paint_brush", PaintBrushItem::new, new Item.Properties());
    public static final Item CUSHION = registerItem("cushion", DyeableCushionItem::new, new Item.Properties());
    public static final Item HAY_CUSHION = registerItem("hay_cushion", CushionItem::new, new Item.Properties());
    public static final Item TRADER_CUSHION = registerItem("trader_cushion", CushionItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    // Helper Method to register items (26.1 needs the registry key set on the properties before the item is created)
    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Identifier id = Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, name);
        properties.setId(ResourceKey.create(Registries.ITEM, id)).useItemDescriptionPrefix();
        return Registry.register(BuiltInRegistries.ITEM, id, factory.apply(properties));
    }

    public static void registerModItems() {
        CozyHome.LOGGER.info("Registering Mod Items for " + CozyHome.MOD_ID);
    }
}
