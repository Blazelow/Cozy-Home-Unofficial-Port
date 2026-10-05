package net.luckystudio.cozyhome.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;

/** Lifts the camera above the telescope stand while looking through it, so the stand is not in the picture. */
@Mixin(Camera.class)
public abstract class CameraMixin {
    private static final double LOOKING_THROUGH_LIFT = 0.8;

    @ModifyVariable(method = "setPosition(DDD)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double cozyhome$liftOverTelescopeStand(double y) {
        if (((Camera) (Object) this).getEntity() instanceof Player player && TelescopeBlock.isLookingThrough(player)) {
            return y + LOOKING_THROUGH_LIFT;
        }
        return y;
    }
}
