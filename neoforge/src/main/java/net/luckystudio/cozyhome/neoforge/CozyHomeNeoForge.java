package net.luckystudio.cozyhome.neoforge;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.platform.Platform;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;

@Mod(CozyHome.MOD_ID)
public class CozyHomeNeoForge {

    public CozyHomeNeoForge(IEventBus modEventBus) {
        Platform.setModLoadedCheck(id -> ModList.get().isLoaded(id));

        modEventBus.addListener(CozyHomeNeoForge::onRegister);
        modEventBus.addListener(CozyHomeNeoForge::onCommonSetup);

        NeoForge.EVENT_BUS.addListener(CozyHomeNeoForge::onFurnaceFuel);
        NeoForge.EVENT_BUS.addListener(CozyHomeNeoForge::onVillagerTrades);
        NeoForge.EVENT_BUS.addListener(CozyHomeNeoForge::onWandererTrades);
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
            FireBlock fire = (FireBlock) Blocks.FIRE;
            ModFlammableBlocks.registerFlammables(fire::setFlammable);
        });
    }

    private static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
        int burnTime = ModFuels.getBurnTime(event.getItemStack().getItem());
        if (burnTime > 0) event.setBurnTime(burnTime);
    }

    // Cartographers sell telescopes at master level
    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            event.getTrades().get(5).add(CozyHome.telescopeTrade());
        }
    }

    // Wandering trader now sells trader themed furniture
    private static void onWandererTrades(WandererTradesEvent event) {
        event.getGenericTrades().add(CozyHome.traderCushionTrade());
    }
}
