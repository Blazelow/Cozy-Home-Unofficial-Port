package net.luckystudio.cozyhome.util;

import net.luckystudio.cozyhome.CozyHome;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
public class ModTags {

// We can just add a folder in our workspace and input all the information into the json but the whole point of making TagKeys is so we can reference them in the code.

    public static class Blocks {

        public static final TagKey<Block> DYEABLE = createTag("dyeable");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return  TagKey.of(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, name));
        }
    }
}
