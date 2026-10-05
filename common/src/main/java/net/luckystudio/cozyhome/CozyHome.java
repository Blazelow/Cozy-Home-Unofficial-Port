package net.luckystudio.cozyhome;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
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
import net.luckystudio.cozyhome.util.ModSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CozyHome {
	public static final String MOD_ID = "cozyhome";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static boolean registered = false;

	/**
	 * Creates and registers all Cozy Home content. The content lives in static initialisers that call
	 * Registry.register, so this has to run while the registries are still writable. The loader module calls it
	 * at the right moment, and it only ever does its work once.
	 */
	public static void registerContent() {
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

	/** Cartographers sell telescopes at master level. */
	public static VillagerTrades.ItemListing telescopeTrade() {
		return (entity, random) -> new MerchantOffer(
				new ItemCost(Items.EMERALD, 26),
				new ItemStack(ModBlocks.TELESCOPE, 1),
				2,
				5,
				0.5f);
	}

	/** The wandering trader sells trader themed furniture. */
	public static VillagerTrades.ItemListing traderCushionTrade() {
		return (entity, random) -> new MerchantOffer(
				new ItemCost(Items.EMERALD, 6),
				new ItemStack(ModItems.TRADER_CUSHION, 1),
				8,
				5,
				0.5f);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
