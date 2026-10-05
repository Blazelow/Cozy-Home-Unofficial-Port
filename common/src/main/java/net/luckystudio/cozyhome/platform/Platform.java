package net.luckystudio.cozyhome.platform;

import java.util.function.Predicate;

/** The few things shared code needs to ask the loader. Each loader module installs its answer before any content loads. */
public final class Platform {
    private static Predicate<String> modLoaded = id -> false;

    private Platform() {
    }

    public static void setModLoadedCheck(Predicate<String> check) {
        modLoaded = check;
    }

    public static boolean isModLoaded(String modId) {
        return modLoaded.test(modId);
    }
}
