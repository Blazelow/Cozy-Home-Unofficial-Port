package net.luckystudio.cozyhome.item.custom;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.util.ModColorHandler;
import java.util.function.Consumer;
public class DyedBlockItem extends BlockItem {

    public DyedBlockItem(Block block, Item.Properties settings) {
        super(block, settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        int color = DyedItemColor.getOrDefault(stack, -393218); // Using 'this' as the BlockEntity
        Component colorName = ModColorHandler.getColorName(color);
        return colorName.copy().append(Component.literal(" ")).append(super.getName(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (getBlock() instanceof ItemTooltipProvider provider) provider.appendTooltip(stack, tooltip);
    }
}
