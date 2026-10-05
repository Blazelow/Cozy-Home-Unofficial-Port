package net.luckystudio.cozyhome.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;

/**
 * Looking through a telescope is the same as looking through a spyglass: the game already zooms the field of view,
 * hides the hand and draws the scope overlay whenever the player counts as scoping.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void cozyhome$scopeThroughTelescope(CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide && TelescopeBlock.isLookingThrough(self)) {
            cir.setReturnValue(true);
        }
    }
}
