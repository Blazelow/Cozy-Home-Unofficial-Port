package net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.WaterHoldingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.List;
public class SinkBlock extends AbstractSinkBlock implements ItemTooltipProvider, SimpleWaterloggedBlock, WaterHoldingBlock {
    public static final MapCodec<SinkBlock> CODEC = simpleCodec(SinkBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // Integer properties
    public static final IntegerProperty LEVEL = ModProperties.FILLED_LEVEL_0_3;

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(1, 8, 1, 15, 14, 15));

    @Override
    protected MapCodec<? extends SinkBlock> codec() {
        return CODEC;
    }

    public SinkBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(super.defaultBlockState()
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SinkBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.join(SHAPE, Block.box(3, 10, 3, 13, 16, 13), BooleanOp.ONLY_FIRST);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return super.getStateForPlacement(ctx).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        super.appendTooltip(stack, tooltip);
        tooltip.accept(Component.translatable("tooltip.cozyhome.pulls_water_from").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.behind")));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public float getLiquidLevelHeight(BlockState state) {
        int level = state.getValue(LEVEL);
        return switch (level) {
            case 1 -> 0.438f;
            case 2 -> 0.688f;
            case 3 -> 0.938f;
            default -> 0.125f;
        };
    }
}