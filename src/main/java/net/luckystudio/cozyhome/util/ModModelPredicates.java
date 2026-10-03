package net.luckystudio.cozyhome.util;

import net.luckystudio.cozyhome.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
public class ModModelPredicates {

    public static void registerModelPredicates() {
//        ItemProperties.register(ModItems.CHISEL, ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "used"),
//                (stack, world, entity, seed) -> stack.getValue(ModDataComponentTypes.COORDINATES) != null ? 1f : 0f);

//        registerCustomBow(ModItems.KAUPEN_BOW);
    }

//    private static void registerCustomBow(Item item) {
//        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (stack, world, entity, seed) -> {
//            if (entity == null) {
//                return 0.0F;
//            } else {
//                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 20.0F;
//            }
//        });
//        ItemProperties.register(
//                item,
//                ResourceLocation.withDefaultNamespace("pulling"),
//                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
//        );
//    }
}
