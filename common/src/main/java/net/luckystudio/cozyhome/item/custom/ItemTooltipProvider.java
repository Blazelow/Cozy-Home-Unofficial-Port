package net.luckystudio.cozyhome.item.custom;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/** Implemented by blocks that want to add lines to the tooltip of their block item (blocks no longer have tooltips in 26.1). */
public interface ItemTooltipProvider {
    void appendTooltip(ItemStack stack, Consumer<Component> tooltip);
}
