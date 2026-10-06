package net.luckystudio.cozyhome.block.custom.toolrack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.state.BlockState;

/** Draws the tool or weapon that rests on a tool rack. */
public class ToolRackBlockEntityRenderer implements BlockEntityRenderer<ItemRackBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ToolRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ItemRackBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = entity.getStack();
        if (stack.isEmpty() || entity.getLevel() == null) return;

        poseStack.pushPose();
        centerItem(entity.getBlockState(), poseStack); // Moves the item to the arms of the rack
        setItemPosition(stack, poseStack);
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, entity.getLevel(), (int) entity.getBlockPos().asLong());
        poseStack.popPose();
    }

    private void centerItem(BlockState state, PoseStack poseStack) {
        Direction facing = state.getValue(ToolRackBlock.FACING);
        float translation = 3.5F / 16.0F; // 3.5 pixels in block space
        switch (facing) {
            case NORTH -> {
                poseStack.translate(0.5F, 0.5F, 0.5F + translation + 0.0625F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            }
            case EAST -> {
                poseStack.translate(translation, 0.5F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            case SOUTH -> poseStack.translate(0.5F, 0.5F, translation);
            case WEST -> {
                poseStack.translate(0.5F + translation + 0.0625F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            }
            default -> {
            }
        }
    }

    private void setItemPosition(ItemStack stack, PoseStack poseStack) {
        Item item = stack.getItem();
        if (item instanceof ShovelItem || item instanceof PickaxeItem || item instanceof AxeItem || item instanceof HoeItem) {
            poseStack.translate(-0.04625F, -0.0625F, 0); // These item textures aren't centered when rotated vertically so we adjust
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
        } else if (item instanceof BowItem) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        } else if (item instanceof CrossbowItem) {
            poseStack.translate(0, -0.0625F, 0); // Move down to rest the hand guard on the rack arms
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        } else if (item instanceof SwordItem) {
            poseStack.translate(0, -0.0625F, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-135.0F));
        } else if (item instanceof MaceItem) {
            poseStack.translate(0, 0.0625F, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
        } else if (item instanceof SpyglassItem) {
            poseStack.translate(0, 0.125F, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        } else if (item instanceof InstrumentItem) {
            poseStack.translate(0, 0.5F, 0);
        } else if (item instanceof ShieldItem) {
            poseStack.translate(0.5F, 0.5F, 0.5F);
        } else if (item instanceof TridentItem) {
            poseStack.translate(0.5F, 0.6F, 0.5F);
        }
    }
}
