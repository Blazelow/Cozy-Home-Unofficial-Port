package net.luckystudio.cozyhome.block.custom.tent;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.util.ModColorHandler;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** A dyeable tent that can be opened and closed with an empty hand. */
public class TentBlock extends BaseEntityBlock {
    public static final MapCodec<TentBlock> CODEC = simpleCodec(TentBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final int DEFAULT_COLOR = -393218;

    public TentBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            if (world.getBlockEntity(pos) instanceof TentBlockEntity tent) {
                int itemColor = dyeItem.getDyeColor().getTextureDiffuseColor();
                int blockColor = ModColorHandler.getBlockColor(tent, DEFAULT_COLOR);
                int newColor = FastColor.ARGB32.average(blockColor, itemColor);
                if (blockColor == newColor) {
                    player.displayClientMessage(Component.translatable("message.cozyhome.same_color"), true);
                    return ItemInteractionResult.SUCCESS;
                }
                tent.setComponents(DataComponentMap.builder().set(DataComponents.DYED_COLOR, new DyedItemColor(newColor, false)).build());
                stack.consume(1, player);
                tent.setChanged();
                world.sendBlockUpdated(pos, state, state, 0);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }
        // Only an empty hand opens or closes the tent, so held blocks and items can still be used on it
        return stack.isEmpty() ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            world.setBlock(pos, state.cycle(OPEN), Block.UPDATE_ALL);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.toggle_tent")));
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_dye").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.sets_block_color")));
    }
}
