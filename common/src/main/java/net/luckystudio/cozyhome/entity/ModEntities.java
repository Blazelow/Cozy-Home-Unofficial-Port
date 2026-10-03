package net.luckystudio.cozyhome.entity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.luckystudio.cozyhome.entity.custom.SeatEntity;
public class ModEntities {
    public static final EntityType<SeatEntity> SEAT_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("cozyhome", "seat"),
            EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("cozyhome", "seat"))));

    public static void registerModEntities() {}
}
