package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Base for the items that are drawn from code (chairs, sofas, wall clocks, bathtubs) instead of from a json model.
 * The argument holds everything needed to draw the item, read from the stack once per frame.
 */
public abstract class CozyItemModelRenderer implements SpecialModelRenderer<CozyItemModelRenderer.Arg> {

    public record Arg(Block block, int color) {
    }

    @Override
    public @Nullable Arg extractArgument(ItemStack stack) {
        return new Arg(Block.byItem(stack.getItem()), DyedItemColor.getOrDefault(stack, -17170434));
    }

    @Override
    public void submit(@Nullable Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (arg == null) return;
        matrices.pushPose();
        render(arg, matrices, collector, light, overlay, hasFoil, outlineColor);
        matrices.popPose();
    }

    protected abstract void render(Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor);

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(0.0F, 0.0F, 0.0F));
        output.accept(new Vector3f(1.0F, 1.0F, 1.0F));
    }
}
