package net.luckystudio.cozyhome.item.renderer;

// FABRIC-IMPORT: net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.DoubleLongPart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
public class BathtubItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();

        matrices.translate(0,0,0);

        Block block = Block.getBlockFromItem(stack.getItem());

        // Front part (FOOT)
        BlockState frontState = block.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .setValue(ModProperties.DOUBLE_LONG_PART, DoubleLongPart.FRONT);

        // Back part (HEAD)
        BlockState backState = block.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .setValue(ModProperties.DOUBLE_LONG_PART, DoubleLongPart.BACK);

        matrices.translate(0, 0, 0);

        // Render front part
        Minecraft.getInstance().getBlockRenderManager().renderBlockAsEntity(
                backState, matrices, vertexConsumers, light, overlay
        );

        // Move backward one block length to render front part
        matrices.translate(0, 0, -1);

        // Render back part (at origin)
        Minecraft.getInstance().getBlockRenderManager().renderBlockAsEntity(
                frontState, matrices, vertexConsumers, light, overlay
        );

        matrices.popPose();
    }

}
