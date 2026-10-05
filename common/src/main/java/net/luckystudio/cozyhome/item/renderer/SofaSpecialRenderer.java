package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;

import com.mojang.serialization.MapCodec;

import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlock;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;

public class SofaSpecialRenderer extends CozyItemModelRenderer {
    private final ModelPart sofa = SofaModel.getTexturedModelData().bakeRoot();

    @Override
    protected void render(Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        matrices.translate(0.5, 1.125, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.scale(.85f, .85f, .85f);
        SofaBlock.SofaType sofaType = ((SofaBlock) arg.block()).getSofaType();
        collector.submitModelPart(sofa, matrices, RenderTypes.entityCutoutZOffset(SofaBlockEntityRenderer.getSofaTexture(sofaType)), light, overlay, null, arg.color(), null, outlineColor);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Arg> {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Arg> bake(SpecialModelRenderer.BakingContext context) {
            return new SofaSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
