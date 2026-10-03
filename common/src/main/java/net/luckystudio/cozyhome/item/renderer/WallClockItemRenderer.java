package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlock;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockBlockEntityRenderer;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;
public class WallClockItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final ModelPart wall_clock;

    public WallClockItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.wall_clock = WallClockModel.getTexturedModelData().bakeRoot();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();

        matrices.translate(0.5, 1.5, 0.5);
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        Block block = Block.byItem(stack.getItem());
        BlockState blockState = block.defaultBlockState();
        WallClockBlock.ClockType clockType = ((WallClockBlock)blockState.getBlock()).getClockType();

        RenderType clockRenderLayer = WallClockBlockEntityRenderer.getClockRenderLayer(clockType);
        VertexConsumer clockVertexConsumer = vertexConsumers.getBuffer(clockRenderLayer);
        wall_clock.render(matrices, clockVertexConsumer, light, overlay);

        matrices.popPose();
    }
}
