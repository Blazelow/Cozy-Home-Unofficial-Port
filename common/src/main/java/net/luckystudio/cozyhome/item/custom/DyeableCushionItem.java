package net.luckystudio.cozyhome.item.custom;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

import net.luckystudio.cozyhome.util.ModColorHandler;
import java.util.List;
public class DyeableCushionItem extends CushionItem {
    public DyeableCushionItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        int color = DyedItemColor.getOrDefault(stack, -393218); // Using 'this' as the BlockEntity
        Component colorName = ModColorHandler.getColorName(color);
        return colorName.copy().append(Component.literal(" ")).append(super.getName(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        tooltip.accept(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, tooltip, type);
    }
}
