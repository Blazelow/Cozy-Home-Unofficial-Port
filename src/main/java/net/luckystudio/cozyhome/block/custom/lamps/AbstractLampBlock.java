package net.luckystudio.cozyhome.block.custom.lamps;

import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import net.luckystudio.cozyhome.util.ModSoundEvents;
import net.luckystudio.cozyhome.util.ModColorHandler;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
public abstract class AbstractLampBlock extends BaseEntityBlock implements ConnectingBlock {
    public static final EnumProperty<VerticalLinearConnectionBlock> CONNECTION = ModProperties.VERTICAL_CONNECTION;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public AbstractLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(CONNECTION, VerticalLinearConnectionBlock.SINGLE)
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.FALSE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LampBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION, FACING, LIT);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (world.getBlockState(pos.below()).getBlock() == this) {
            if (world.getBlockState(pos.below(2)).getBlock() == this) {
                return world.getBlockState(pos.below(2)).getValue(CONNECTION) != VerticalLinearConnectionBlock.MIDDLE;
            } else {
                return true;
            }
        } else {
            return Block.sideCoversSmallSquare(world, pos.below(), Direction.UP);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction;
        if (canSurvive(ctx.getLevel().getBlockState(ctx.getBlockPos()), ctx.getLevel(), ctx.getBlockPos())) {
            if (ctx.getLevel().getBlockState(ctx.getBlockPos().below()).getBlock() == this) {
                direction = ctx.getLevel().getBlockState(ctx.getBlockPos().below()).getValue(FACING);
                return this.defaultBlockState()
                        .setValue(CONNECTION, VerticalLinearConnectionBlock.HEAD)
                        .setValue(FACING, direction)
                        .setValue(LIT, ctx.getLevel().getBlockState(ctx.getBlockPos().below()).getValue(LIT));
            }
        }
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof LampBlockEntity lampBlockEntity) {
            VerticalLinearConnectionBlock connection = state.getValue(CONNECTION);
                if (stack.getItem() instanceof DyeItem dyeItem) {
                    final int itemColor = dyeItem.getColor().getEntityColor();
                    final int blockColor = ModColorHandler.getBlockColor(lampBlockEntity, -17170434);
                    final int newColor = FastColor.Argb.averageArgb(blockColor, itemColor);
                    if (blockColor == newColor) {
                        player.displayClientMessage(Component.translatable("message.cozyhome.same_color"), true);
                        return ItemInteractionResult.SUCCESS;
                    }
                    DataComponentMap components = DataComponentMap.builder().add(DataComponents.DYED_COLOR, new DyedItemColor(newColor, false)).build();
                    lampBlockEntity.setComponents(components);
                    stack.consume(1, player);
                    lampBlockEntity.setChanged();
                    world.sendBlockUpdated(pos, state, state, 0);
                } else if (stack.getItem() == this.asItem() && hit.getSide() == Direction.UP) {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                } else {
                    state = state.cycle(LIT);
                    float f = state.getValue(LIT) ? 1.0F : 0.8F;
                    if (connection == VerticalLinearConnectionBlock.HEAD) {
                        world.setBlock(pos.below(), state, Block.UPDATE_ALL);
                        if (world.getBlockState(pos.below(2)).getBlock() == this) {
                            world.setBlock(pos.below(2), state, Block.UPDATE_ALL);
                        }
                    } else if (connection == VerticalLinearConnectionBlock.MIDDLE) {
                        world.setBlock(pos.above(), state, Block.UPDATE_ALL);
                        world.setBlock(pos.below(), state, Block.UPDATE_ALL);
                    } else if (connection == VerticalLinearConnectionBlock.TAIL) {
                        world.setBlock(pos.above(), state, Block.UPDATE_ALL);
                        if (world.getBlockState(pos.above(2)).getBlock() == this) {
                            world.setBlock(pos.above(2), state, Block.UPDATE_ALL);
                        }
                    }
                    world.setBlock(pos, state, Block.UPDATE_ALL);
                    world.playSound(player, pos, ModSoundEvents.LAMP_TOGGLE, SoundSource.BLOCKS, 0.3F, f);
                }
                return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state, world, pos) ? state.setValue(CONNECTION, VerticalLinearConnectionBlock.setVerticalConnection(state, world, pos)) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() == this;
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(Component.translatable("tooltip.cozyhome.dyeable").formatted(ChatFormatting.GRAY).append(""));
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.toggle_light")));
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_dye").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.sets_block_color")));
    }
}
