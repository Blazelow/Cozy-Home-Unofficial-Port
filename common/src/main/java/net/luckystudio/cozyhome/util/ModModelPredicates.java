package net.luckystudio.cozyhome.util;

import net.luckystudio.cozyhome.item.ModItems;
public class ModModelPredicates {

    public static void registerModelPredicates() {
//        ItemProperties.register(ModItems.CHISEL, Identifier.fromNamespaceAndPath(TutorialMod.MOD_ID, "used"),
//                (stack, world, entity, seed) -> stack.getValue(ModDataComponentTypes.COORDINATES) != null ? 1f : 0f);

//        registerCustomBow(ModItems.KAUPEN_BOW);
    }

//    private static void registerCustomBow(Item item) {
//        ItemProperties.register(item, Identifier.withDefaultNamespace("pull"), (stack, world, entity, seed) -> {
//            if (entity == null) {
//                return 0.0F;
//            } else {
//                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getItemUseTimeLeft()) / 20.0F;
//            }
//        });
//        ItemProperties.register(
//                item,
//                Identifier.withDefaultNamespace("pulling"),
//                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
//        );
//    }
}
