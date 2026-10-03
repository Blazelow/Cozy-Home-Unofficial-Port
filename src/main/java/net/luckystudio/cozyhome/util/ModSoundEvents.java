package net.luckystudio.cozyhome.util;

import net.luckystudio.cozyhome.CozyHome;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
public class ModSoundEvents {

    // Sounds
    public static final SoundEvent LAMP_TOGGLE = registerSoundEvent("lamp_toggle");
    public static final SoundEvent FIREPLACE_LOOP = registerSoundEvent("fireplace_loop");
    public static final SoundEvent GRANDFATHER_CLOCK_TICK = registerSoundEvent("grandfather_clock_tick");
    public static final SoundEvent GRANDFATHER_CLOCK_MIDNIGHT = registerSoundEvent("grandfather_clock_midnight");
    public static final SoundEvent LIGHT_WATER_FLOW = registerSoundEvent("light_water_flow");
    public static final SoundEvent SPLASH = registerSoundEvent("splash");
    public static final SoundEvent SINK_TOGGLE = registerSoundEvent("sink_toggle");
    public static final SoundEvent RUNNING_WATER = registerSoundEvent("running_water");

    // Block Sound Group
//    public static final SoundType EXAMPLE = new SoundType(1,1,
//            ModSounds.SOUND_BLOCK_BREAK,
//            ModSounds.SOUND_BLOCK_STEP,
//            ModSounds.SOUND_BLOCK_PLACE,
//            ModSounds.SOUND_BLOCK_HIT,
//            ModSounds.SOUND_BLOCK_FALL);

    // Helper Method
    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        CozyHome.LOGGER.info("Registering Sounds for " + CozyHome.MOD_ID);
    }
}
