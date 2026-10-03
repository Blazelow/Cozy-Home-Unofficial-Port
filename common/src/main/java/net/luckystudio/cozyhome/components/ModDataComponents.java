package net.luckystudio.cozyhome.components;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;
import net.luckystudio.cozyhome.CozyHome;
import java.util.function.UnaryOperator;
public class ModDataComponents {

    public static final DataComponentType<Float> YAW = register("yaw", builder ->
            builder.persistent(Codec.FLOAT));
    public static final DataComponentType<Float> PITCH = register("pitch", builder ->
            builder.persistent(Codec.FLOAT));

    public static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(CozyHome.MOD_ID, path), builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerModDataComponents() {
        CozyHome.LOGGER.info("Registering Mod Data Components for " + CozyHome.MOD_ID);
    }
}

