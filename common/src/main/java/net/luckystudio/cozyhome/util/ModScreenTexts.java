package net.luckystudio.cozyhome.util;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
public class ModScreenTexts {

    private static final ChatFormatting CAPTION = ChatFormatting.GRAY;
    private static final ChatFormatting ENTRIES = ChatFormatting.BLUE;

    public static final Component ENTRY = entry();

    public static MutableComponent entry() {
        return Component.literal(" ").withStyle(ENTRIES);
    }
}
