package net.luckystudio.cozyhome.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;

/** A telescope only looks at the sky: the view cannot go below the horizon, so the stand under the camera stays out of sight. */
@Mixin(Entity.class)
public abstract class EntityMixin {
    private static final float LOWEST_PITCH = 5.0F;

    @Inject(method = "turn", at = @At("TAIL"))
    private void cozyhome$keepTelescopeLookingUp(double yRot, double xRot, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof Player player && player.level().isClientSide() && TelescopeBlock.isLookingThrough(player) && player.getXRot() > LOWEST_PITCH) {
            player.setXRot(LOWEST_PITCH);
        }
    }
}
