package net.luckystudio.cozyhome.item.custom;

import net.minecraft.world.item.Item;

import net.luckystudio.cozyhome.util.ModColorHandler;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, type);
    }
}
