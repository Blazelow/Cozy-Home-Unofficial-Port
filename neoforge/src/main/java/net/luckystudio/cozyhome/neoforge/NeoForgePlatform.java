package net.luckystudio.cozyhome.neoforge;

import java.util.Set;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;

import net.luckystudio.cozyhome.platform.Platform;

public class NeoForgePlatform implements Platform.Impl {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(Platform.BlockEntityFactory<T> factory, Block... blocks) {
        return new BlockEntityType<>(factory::create, Set.of(blocks));
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(Platform.MenuFactory<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.VANILLA_SET);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
