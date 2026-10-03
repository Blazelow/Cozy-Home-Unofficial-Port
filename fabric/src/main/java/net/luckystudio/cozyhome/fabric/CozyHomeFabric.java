package net.luckystudio.cozyhome.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.platform.Platform;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;

public class CozyHomeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Platform.set(new FabricPlatform());
        CozyHome.registerContent();

        ModFlammableBlocks.registerFlammables((block, igniteOdds, burnOdds) ->
                FlammableBlockRegistry.getDefaultInstance().add(block, igniteOdds, burnOdds));

        FuelValueEvents.BUILD.register((builder, context) -> ModFuels.forEach(builder::add));
    }
}
