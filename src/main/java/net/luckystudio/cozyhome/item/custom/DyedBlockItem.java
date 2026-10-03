package net.luckystudio.cozyhome.item.custom;

import net.luckystudio.cozyhome.util.ModColorHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
public class DyedBlockItem extends BlockItem {

    public DyedBlockItem(Block block, BlockBehaviour.Properties settings) {
        super(block, settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        int color = DyedItemColor.getColor(stack, -393218); // Using 'this' as the BlockEntity
        Component colorName = ModColorHandler.getColorName(color);
        return colorName.copy().append(Component.literal(" ")).append(super.getName(stack));
    }
}
