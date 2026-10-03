package net.luckystudio.cozyhome;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

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
	 * (after installing the Platform) at the right moment, and it only ever does its work once.
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

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
