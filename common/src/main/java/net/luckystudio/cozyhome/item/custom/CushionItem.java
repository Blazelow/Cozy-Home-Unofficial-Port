package net.luckystudio.cozyhome.item.custom;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
public class CushionItem extends Item {
    public CushionItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, display, tooltip, type);
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("tooltip.cozyhome.applied_when_interacted_with").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.chairs")));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.sofas")));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.couches")));
    }
}
