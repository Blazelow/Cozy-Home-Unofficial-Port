package net.luckystudio.cozyhome.entity;

import net.luckystudio.cozyhome.entity.custom.SeatEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
public class ModEntities {
    public static final EntityType<SeatEntity> SEAT_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath("cozyhome", "seat"),
            EntityType.Builder.create(SeatEntity::new, MobCategory.MISC).dimensions(1f, 1f).build());

    public static void registerModEntities() {}
}
