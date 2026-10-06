package net.luckystudio.cozyhome.block.custom.tent;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;
import net.luckystudio.cozyhome.util.ModColorHandler;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/** A dyeable tent that can be opened and closed with an empty hand. */
public class TentBlock extends BaseEntityBlock implements ItemTooltipProvider {
    public static final MapCodec<TentBlock> CODEC = simpleCodec(TentBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final int DEFAULT_COLOR = -393218;

    public TentBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(OPEN, false);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TentBlockEntity(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.has(DataComponents.DYE)) {
            if (world.getBlockEntity(pos) instanceof TentBlockEntity tent) {
                int itemColor = stack.get(DataComponents.DYE).getTextureDiffuseColor();
                int blockColor = ModColorHandler.getBlockColor(tent, DEFAULT_COLOR);
                int newColor = ARGB.average(blockColor, itemColor);
                if (blockColor == newColor) {
                    player.sendOverlayMessage(Component.translatable("message.cozyhome.same_color"));
                    return InteractionResult.SUCCESS;
                }
                tent.setComponents(DataComponentMap.builder().set(DataComponents.DYED_COLOR, new DyedItemColor(newColor)).build());
                stack.consume(1, player);
                tent.setChanged();
                world.sendBlockUpdated(pos, state, state, 0);
            }
            return InteractionResult.SUCCESS;
        }
        // Only an empty hand opens or closes the tent, so held blocks and items can still be used on it
        return stack.isEmpty() ? InteractionResult.TRY_WITH_EMPTY_HAND : InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide()) {
            world.setBlock(pos, state.cycle(OPEN), Block.UPDATE_ALL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("tooltip.cozyhome.interact_with_hand").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.toggle_tent")));
        tooltip.accept(Component.translatable("tooltip.cozyhome.interact_with_dye").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.sets_block_color")));
    }
}
