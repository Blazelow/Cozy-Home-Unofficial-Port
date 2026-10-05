package net.luckystudio.cozyhome.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;

/** Zooms the field of view the same way a spyglass does while the player looks through a telescope. */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    private static final float TELESCOPE_ZOOM = 0.1F;

    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void cozyhome$zoomThroughTelescope(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
        if (firstPerson && TelescopeBlock.isLookingThrough((AbstractClientPlayer) (Object) this)) {
            cir.setReturnValue(cir.getReturnValueF() * TELESCOPE_ZOOM);
        }
    }
}
