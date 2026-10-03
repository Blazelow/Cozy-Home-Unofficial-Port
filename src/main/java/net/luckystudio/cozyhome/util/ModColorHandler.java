package net.luckystudio.cozyhome.util;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.entity.BlockEntity;
public class ModColorHandler {
    public static final Map<Integer, Component> COLOR_MAP = new HashMap<>();

    static {
        // Basic colors
        COLOR_MAP.put(-393218, Component.translatable("cozyhome.color.white"));
        COLOR_MAP.put(-6447721, Component.translatable("cozyhome.color.gray"));
        COLOR_MAP.put(-14869215, Component.translatable("cozyhome.color.black"));
        COLOR_MAP.put(-8170446, Component.translatable("cozyhome.color.brown"));
        COLOR_MAP.put(-5231066, Component.translatable("cozyhome.color.red"));
        COLOR_MAP.put(-425955, Component.translatable("cozyhome.color.orange"));
        COLOR_MAP.put(-75715, Component.translatable("cozyhome.color.yellow"));
        COLOR_MAP.put(-8337633, Component.translatable("cozyhome.color.lime"));
        COLOR_MAP.put(-10585066, Component.translatable("cozyhome.color.green"));
        COLOR_MAP.put(-15295332, Component.translatable("cozyhome.color.cyan"));
        COLOR_MAP.put(-12930086, Component.translatable("cozyhome.color.light_blue"));
        COLOR_MAP.put(-12827478, Component.translatable("cozyhome.color.blue"));
        COLOR_MAP.put(-7785800, Component.translatable("cozyhome.color.purple"));
        COLOR_MAP.put(-3715395, Component.translatable("cozyhome.color.magenta"));
        COLOR_MAP.put(-816214, Component.translatable("cozyhome.color.pink"));
    }

    public static Component getColorName(int color) {
        return COLOR_MAP.getOrDefault(color, Component.translatable("cozyhome.custom_colored"));
    }

    public static int getBlockColor(@Nullable BlockEntity entity, int defaultColor) {
        if (entity == null) {
            return defaultColor;
        }
        DyedItemColor dyedColorComponent = entity.getComponents().get(DataComponents.DYED_COLOR);
        return dyedColorComponent != null ? FastColor.ARGB32.opaque(dyedColorComponent.rgb()) : defaultColor;
    }
}
