package net.luckystudio.cozyhome.client;

import net.minecraft.util.Mth;

/** How far the telescope zooms in, adjusted with the scroll wheel while looking through it. */
public final class TelescopeZoom {
    /** The field of view is multiplied by this: 1 is no zoom, 0.1 is the same zoom as a spyglass. */
    private static final float MIN_FOV = 0.08F;
    private static final float MAX_FOV = 0.8F;
    private static final float STEP = 0.85F;

    private static float fov = 0.45F;

    private TelescopeZoom() {
    }

    public static float fovModifier() {
        return fov;
    }

    /** Positive steps (scrolling up) zoom in, negative steps zoom out. */
    public static void scroll(int steps) {
        if (steps == 0) return;
        fov = Mth.clamp(fov * (float) Math.pow(STEP, steps), MIN_FOV, MAX_FOV);
    }
}
