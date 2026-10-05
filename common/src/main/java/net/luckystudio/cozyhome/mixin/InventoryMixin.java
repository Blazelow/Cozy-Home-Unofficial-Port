package net.luckystudio.cozyhome.mixin;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.client.TelescopeZoom;

/** While looking through a telescope the scroll wheel zooms instead of changing the selected hotbar slot. */
@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow
    public net.minecraft.world.entity.player.Player player;

    @Inject(method = "swapPaint", at = @At("HEAD"), cancellable = true)
    private void cozyhome$scrollZoomsTelescope(double direction, CallbackInfo ci) {
        if (this.player.level().isClientSide && TelescopeBlock.isLookingThrough(this.player)) {
            TelescopeZoom.scroll(direction > 0 ? 1 : direction < 0 ? -1 : 0);
            ci.cancel();
        }
    }
}
