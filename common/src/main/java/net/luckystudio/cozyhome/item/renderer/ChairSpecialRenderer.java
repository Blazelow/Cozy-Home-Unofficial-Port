package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;

import com.mojang.serialization.MapCodec;

import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlock;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;

public class ChairSpecialRenderer extends CozyItemModelRenderer {
    private final ModelPart chair = ChairModel.getTexturedModelData().bakeRoot();

    @Override
    protected void render(Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        matrices.translate(0.5, 1.125, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.scale(.85f, .85f, .85f);
        ChairBlock.ChairType chairType = ((ChairBlock) arg.block()).getChairType();
        collector.submitModelPart(chair, matrices, RenderTypes.entityCutoutZOffset(ChairBlockEntityRenderer.getChairTexture(chairType, arg.block().defaultBlockState())), light, overlay, null, -1, null, outlineColor);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Arg> {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Arg> bake(SpecialModelRenderer.BakingContext context) {
            return new ChairSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
