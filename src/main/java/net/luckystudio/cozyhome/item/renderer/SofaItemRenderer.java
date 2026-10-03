package net.luckystudio.cozyhome.item.renderer;

// FABRIC-IMPORT: net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlock;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
public class SofaItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private final ModelPart sofa;

    public SofaItemRenderer() {
        this.sofa = SofaModel.getTexturedModelData().createModel();
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        int color = DyedItemColor.getColor(stack, -17170434);
        matrices.translate(0.5, 1.125, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.scale(.85f, .85f, .85f);
        Block block = Block.getBlockFromItem(stack.getItem());
        BlockState blockState = block.defaultBlockState();
        SofaBlock.SofaType sofaType = ((SofaBlock)blockState.getBlock()).getSofaType();

        RenderType chairRenderLayer = SofaBlockEntityRenderer.getSofaRenderLayer(sofaType);
        VertexConsumer chairVertexConsumer = vertexConsumers.getBuffer(chairRenderLayer);
        sofa.render(matrices, chairVertexConsumer, light, overlay, color);

        matrices.popPose();
    }
}
