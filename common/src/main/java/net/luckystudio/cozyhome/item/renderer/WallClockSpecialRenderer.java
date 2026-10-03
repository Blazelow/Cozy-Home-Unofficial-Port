package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;

import com.mojang.serialization.MapCodec;

import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlock;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;

public class WallClockSpecialRenderer extends CozyItemModelRenderer {
    private final ModelPart wallClock = WallClockModel.getTexturedModelData().bakeRoot();

    @Override
    protected void render(Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        WallClockBlock.ClockType clockType = ((WallClockBlock) arg.block()).getClockType();
        collector.submitModelPart(wallClock, matrices, RenderTypes.entityCutoutZOffset(WallClockBlockEntityRenderer.getClockTexture(clockType)), light, overlay, null, false, hasFoil, -1, null, outlineColor);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Arg> {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Arg> bake(SpecialModelRenderer.BakingContext context) {
            return new WallClockSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
