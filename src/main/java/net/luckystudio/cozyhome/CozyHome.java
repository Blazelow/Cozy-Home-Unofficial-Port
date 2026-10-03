package net.luckystudio.cozyhome;

// FABRIC-IMPORT: net.fabricmc.api.ModInitializer;
// FABRIC-IMPORT: net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.components.ModDataComponents;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.item.ModItemGroups;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.util.ModFlammableBlocks;
import net.luckystudio.cozyhome.util.ModFuels;
import net.luckystudio.cozyhome.util.ModSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
public class CozyHome implements ModInitializer {
	public static final String MOD_ID = "cozyhome";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();
		ModBlockEntityTypes.registerBlockEntities();
		ModItemGroups.registerModItemGroups();
		ModFuels.registerFuels();
		ModFlammableBlocks.registerFlammables();
		ModSoundEvents.registerSounds();
		ModDataComponents.registerModDataComponents();

		// Registering Villager Trades
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 5, factories -> {
			factories.add((entity, random) -> new MerchantOffer(
					new ItemCost(Items.EMERALD, 26),
					new ItemStack(ModBlocks.TELESCOPE, 1),
					2,
					5,
					0.5f));
		});

		// Wandering trader now sells trader themed furniture
		TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
			factories.add((entity, random) -> new MerchantOffer(
					new ItemCost(Items.EMERALD, 6),
					new ItemStack(ModItems.TRADER_CUSHION, 1),
					8,
					5,
					0.5f));
		});
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}