package net.luckystudio.cozyhome.item.renderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.Minecraft;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlock;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
public class ChairItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final ModelPart chair;

    public ChairItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.chair = ChairModel.getTexturedModelData().bakeRoot();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();

        matrices.translate(0.5, 1.125, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.scale(.85f, .85f, .85f);
        Block block = Block.byItem(stack.getItem());
        BlockState blockState = block.defaultBlockState();
        ChairBlock.ChairType chairType = ((ChairBlock)blockState.getBlock()).getChairType();

        RenderType chairRenderLayer = ChairBlockEntityRenderer.getChairRenderLayer(chairType, blockState);
        VertexConsumer chairVertexConsumer = vertexConsumers.getBuffer(chairRenderLayer);
        chair.render(matrices, chairVertexConsumer, light, overlay);

        matrices.popPose();
    }
}
