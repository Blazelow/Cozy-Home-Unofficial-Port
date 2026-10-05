package net.luckystudio.cozyhome.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.client.TelescopeZoom;

/** Zooms the field of view while the player looks through a telescope. */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void cozyhome$zoomThroughTelescope(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
        if (firstPerson && TelescopeBlock.isLookingThrough((AbstractClientPlayer) (Object) this)) {
            cir.setReturnValue(TelescopeZoom.fovModifier());
        }
    }
}
