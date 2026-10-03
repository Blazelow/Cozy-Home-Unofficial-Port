package net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.interfaces.WaterHoldingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class SinkCounterBlock extends AbstractSinkBlock implements WaterHoldingBlock {
    public static final MapCodec<SinkCounterBlock> CODEC = simpleCodec(SinkCounterBlock::new);

    public static final VoxelShape COUNTER_TOP = Shapes.box(0, 12, 0, 16, 16, 16);

    @Override
    protected MapCodec<? extends SinkCounterBlock> codec() {
        return CODEC;
    }

    public SinkCounterBlock(BlockBehaviour.Properties settings) {
        super(settings);
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
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    private VoxelShape getShape(BlockState state) {
        Direction direction = state.getValue(FACING);

        VoxelShape topShape = Block.box(0, 12, 0, 16, 16, 16);

        VoxelShape bottom = switch (direction) {
            case NORTH -> Block.box(0, 0, 0, 16, 12, 14);
            case SOUTH -> Block.box(0, 0, 2, 16, 12, 16);
            case EAST -> Block.box(2, 0, 0, 16, 12, 16);
            case WEST -> Block.box(0, 0, 0, 14, 12, 16);
            default -> Shapes.empty();
        };

        VoxelShape baseShape = Shapes.or(
                topShape,
                bottom
        );

        VoxelShape holeShape = Block.box(3, 2, 3, 13, 16, 13);
        return Shapes.join(baseShape, holeShape, BooleanOp.ONLY_FIRST);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(Component.translatable("tooltip.cozyhome.pulls_water_from").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.behind")));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.below")));
    }

    @Override
    public List<Direction> getDirectionsToPull(BlockState state) {
        List<Direction> directions = new ArrayList<>(super.getDirectionsToPull(state));
        directions.add(Direction.DOWN);
        return directions;
    }
}
