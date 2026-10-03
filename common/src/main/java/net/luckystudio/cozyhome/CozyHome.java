package net.luckystudio.cozyhome;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModMenuTypes;
import net.luckystudio.cozyhome.components.ModDataComponents;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.item.ModItemGroups;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;
import net.luckystudio.cozyhome.util.ModSoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CozyHome.MOD_ID)
public class CozyHome {
	public static final String MOD_ID = "cozyhome";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static boolean registered = false;

	public CozyHome(IEventBus modEventBus) {
		modEventBus.addListener(CozyHome::onRegister);
		modEventBus.addListener(CozyHome::onCommonSetup);

		NeoForge.EVENT_BUS.addListener(CozyHome::onFurnaceFuel);
		NeoForge.EVENT_BUS.addListener(CozyHome::onVillagerTrades);
		NeoForge.EVENT_BUS.addListener(CozyHome::onWandererTrades);
	}

	/**
	 * The registries are only writable while RegisterEvent is firing. All Cozy Home content is created in static
	 * initialisers that call Registry.register, so it is loaded here, once, on the first RegisterEvent.
	 */
	private static void onRegister(RegisterEvent event) {
		if (registered) return;
		registered = true;

		ModSoundEvents.registerSounds();
		ModDataComponents.registerModDataComponents();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();
		ModBlockEntityTypes.registerBlockEntities();
		ModMenuTypes.registerMenuTypes();
		ModItemGroups.registerModItemGroups();
	}

	private static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(ModFlammableBlocks::registerFlammables);
	}

	private static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
		int burnTime = ModFuels.getBurnTime(event.getItemStack().getItem());
		if (burnTime > 0) event.setBurnTime(burnTime);
	}

	// Cartographers sell telescopes at master level
	private static void onVillagerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.CARTOGRAPHER) {
			event.getTrades().get(5).add((entity, random) -> new MerchantOffer(
					new ItemCost(Items.EMERALD, 26),
					new ItemStack(ModBlocks.TELESCOPE, 1),
					2,
					5,
					0.5f));
		}
	}

	// Wandering trader now sells trader themed furniture
	private static void onWandererTrades(WandererTradesEvent event) {
		event.getGenericTrades().add((entity, random) -> new MerchantOffer(
				new ItemCost(Items.EMERALD, 6),
				new ItemStack(ModItems.TRADER_CUSHION, 1),
				8,
				5,
				0.5f));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
