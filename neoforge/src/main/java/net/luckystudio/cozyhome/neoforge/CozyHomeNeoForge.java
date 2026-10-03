package net.luckystudio.cozyhome.neoforge;

import java.lang.reflect.Method;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.platform.Platform;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;

@Mod(CozyHome.MOD_ID)
public class CozyHomeNeoForge {

    public CozyHomeNeoForge(IEventBus modEventBus) {
        Platform.set(new NeoForgePlatform());

        modEventBus.addListener(CozyHomeNeoForge::onRegister);
        modEventBus.addListener(CozyHomeNeoForge::onCommonSetup);

        NeoForge.EVENT_BUS.addListener(CozyHomeNeoForge::onFurnaceFuel);
    }

    /**
     * The registries are only writable while RegisterEvent is firing. All Cozy Home content is created in static
     * initialisers that call Registry.register, so it is loaded here, once, on the first RegisterEvent.
     */
    private static void onRegister(RegisterEvent event) {
        CozyHome.registerContent();
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // FireBlock#setFlammable is private since 26.1, so it is called through reflection
            try {
                Method setFlammable = FireBlock.class.getDeclaredMethod("setFlammable", Block.class, int.class, int.class);
                setFlammable.setAccessible(true);
                FireBlock fire = (FireBlock) Blocks.FIRE;
                ModFlammableBlocks.registerFlammables((block, igniteOdds, burnOdds) -> {
                    try {
                        setFlammable.invoke(fire, block, igniteOdds, burnOdds);
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException(e);
                    }
                });
            } catch (ReflectiveOperationException e) {
                CozyHome.LOGGER.error("Could not register flammable blocks", e);
            }
        });
    }

    private static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
        int burnTime = ModFuels.getBurnTime(event.getItemStack().getItem());
        if (burnTime > 0) event.setBurnTime(burnTime);
    }
}
