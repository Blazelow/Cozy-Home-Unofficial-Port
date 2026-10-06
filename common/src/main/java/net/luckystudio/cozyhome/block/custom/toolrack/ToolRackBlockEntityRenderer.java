package net.luckystudio.cozyhome.block.custom.toolrack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.Vec3;

/** Draws the tool or weapon that rests on a tool rack. */
public class ToolRackBlockEntityRenderer implements BlockEntityRenderer<ItemRackBlockEntity, ToolRackRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ToolRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public ToolRackRenderState createRenderState() {
        return new ToolRackRenderState();
    }

    @Override
    public void extractRenderState(ItemRackBlockEntity entity, ToolRackRenderState state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
        state.stack = entity.getStack();
        state.facing = entity.getBlockState().getValue(ToolRackBlock.FACING);
        state.item.clear();
        if (!state.stack.isEmpty() && entity.getLevel() != null) {
            this.itemModelResolver.updateForTopItem(state.item, state.stack, ItemDisplayContext.NONE, entity.getLevel(), null, (int) entity.getBlockPos().asLong());
        }
    }

    @Override
    public void submit(ToolRackRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.stack.isEmpty() || state.item.isEmpty()) return;

        poseStack.pushPose();
        centerItem(state, poseStack); // Moves the item to the arms of the rack
        setItemPosition(state.stack, poseStack);
        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private void centerItem(ToolRackRenderState state, PoseStack poseStack) {
        float translation = 3.5F / 16.0F; // 3.5 pixels in block space
        switch (state.facing) {
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
        if (stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.PICKAXES) || stack.is(ItemTags.AXES) || stack.is(ItemTags.HOES)) {
            poseStack.translate(-0.04625F, -0.0625F, 0); // These item textures aren't centered when rotated vertically so we adjust
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
        } else if (item instanceof BowItem) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        } else if (item instanceof CrossbowItem) {
            poseStack.translate(0, -0.0625F, 0); // Move down to rest the hand guard on the rack arms
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        } else if (stack.is(ItemTags.SWORDS) || stack.is(ItemTags.SPEARS)) {
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
