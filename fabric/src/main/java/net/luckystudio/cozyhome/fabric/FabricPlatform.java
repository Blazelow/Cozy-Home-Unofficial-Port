package net.luckystudio.cozyhome.fabric;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.luckystudio.cozyhome.platform.Platform;

public class FabricPlatform implements Platform.Impl {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(Platform.BlockEntityFactory<T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.<T>create(factory::create, blocks).build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(Platform.MenuFactory<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.VANILLA_SET);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
