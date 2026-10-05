package net.luckystudio.cozyhome.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.npc.VillagerProfession;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.platform.Platform;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;

public class CozyHomeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Platform.setModLoadedCheck(id -> FabricLoader.getInstance().isModLoaded(id));
        CozyHome.registerContent();

        ModFlammableBlocks.registerFlammables((block, igniteOdds, burnOdds) ->
                FlammableBlockRegistry.getDefaultInstance().add(block, igniteOdds, burnOdds));

        ModFuels.forEach((item, ticks) -> FuelRegistry.INSTANCE.add(item, ticks));

        // Cartographers sell telescopes at master level
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 5,
                factories -> factories.add(CozyHome.telescopeTrade()));

        // Wandering trader now sells trader themed furniture
        TradeOfferHelper.registerWanderingTraderOffers(1,
                factories -> factories.add(CozyHome.traderCushionTrade()));
    }
}
