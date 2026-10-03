package net.luckystudio.cozyhome.item.custom;

import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockBehaviour;
public class CushionItem extends Item {
    public CushionItem(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.applied_when_interacted_with").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.chairs")));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.sofas")));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("blocks.cozyhome.couches")));
    }
}
