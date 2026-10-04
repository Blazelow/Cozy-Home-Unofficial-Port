package net.luckystudio.cozyhome.item.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import com.mojang.serialization.MapCodec;

import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.DoubleLongPart;

/** A bathtub is two blocks long, so its item draws both halves next to each other. */
public class BathtubSpecialRenderer extends CozyItemModelRenderer {
    private final BlockModelRenderState back = new BlockModelRenderState();
    private final BlockModelRenderState front = new BlockModelRenderState();

    @Override
    protected void render(Arg arg, PoseStack matrices, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        BlockState backState = arg.block().defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .setValue(ModProperties.DOUBLE_LONG_PART, DoubleLongPart.BACK);
        BlockState frontState = arg.block().defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .setValue(ModProperties.DOUBLE_LONG_PART, DoubleLongPart.FRONT);

        var resolver = Minecraft.getInstance().getBlockEntityRenderDispatcher().blockModelResolver();
        this.back.clear();
        this.front.clear();
        resolver.update(this.back, backState, BlockDisplayContext.create());
        resolver.update(this.front, frontState, BlockDisplayContext.create());

        this.back.submit(matrices, collector, light, overlay, outlineColor);

        // Move backward one block length to draw the other half
        matrices.translate(0, 0, -1);
        this.front.submit(matrices, collector, light, overlay, outlineColor);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Arg> {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Arg> bake(SpecialModelRenderer.BakingContext context) {
            return new BathtubSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
