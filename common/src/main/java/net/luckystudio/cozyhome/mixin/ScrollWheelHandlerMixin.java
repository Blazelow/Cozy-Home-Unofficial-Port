package net.luckystudio.cozyhome.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.client.TelescopeZoom;

/** While looking through a telescope the scroll wheel zooms instead of changing the selected hotbar slot. */
@Mixin(ScrollWheelHandler.class)
public abstract class ScrollWheelHandlerMixin {

    @Inject(method = "onMouseScroll", at = @At("RETURN"), cancellable = true)
    private void cozyhome$scrollZoomsTelescope(double xOffset, double yOffset, CallbackInfoReturnable<Vector2i> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !TelescopeBlock.isLookingThrough(minecraft.player)) return;

        Vector2i steps = cir.getReturnValue();
        TelescopeZoom.scroll(steps.y == 0 ? -steps.x : steps.y);
        cir.setReturnValue(new Vector2i(0, 0));
    }
}
