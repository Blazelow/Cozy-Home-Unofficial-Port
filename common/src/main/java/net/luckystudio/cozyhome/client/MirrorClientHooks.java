package net.luckystudio.cozyhome.client;

import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

/**
 * Lets the wall mirror block open its screen without the block class touching any client only class (which would
 * crash a dedicated server). The client sets the opener on start up, on the server it stays empty.
 */
public class MirrorClientHooks {
    /** Opens the mirror screen for the player. The boolean is true for a full body mirror (stacked mirrors). */
    public static BiConsumer<Player, Boolean> opener = (player, fullBody) -> {};

    public static void openMirror(Player player, boolean fullBody) {
        opener.accept(player, fullBody);
    }
}
