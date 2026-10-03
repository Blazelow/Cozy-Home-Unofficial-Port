package net.luckystudio.cozyhome.block.custom.seatable.couches;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.custom.AbstractSeatBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.HorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.util.ModColorHandler;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import static net.luckystudio.cozyhome.block.util.ModProperties.setStairShapeNoFlip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
public class CouchBlock extends AbstractSeatBlock implements ConnectingBlock {
    public static final MapCodec<CouchBlock> CODEC = simpleCodec(CouchBlock::new);

    public static final EnumProperty<HorizontalLinearConnectionBlock> CONNECTION = ModProperties.HORIZONTAL_CONNECTION;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape BASE_SHAPE = CouchBlock.box(0, 2, 0, 16, 8, 16);

    public CouchBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.defaultBlockState()
                .setValue(CONNECTION, HorizontalLinearConnectionBlock.SINGLE)
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(FACING, Direction.NORTH);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CouchBlockEntity(pos,state);
    }

    @Override
    protected MapCodec<? extends AbstractSeatBlock> codec() {
        return CODEC;
    }

    // This is the hit-box of the block, we are applying our VoxelShape to it.
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BASE_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, CONNECTION, SHAPE);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return Objects.requireNonNull(super.getStateForPlacement(ctx))
                .setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
                .setValue(CONNECTION, HorizontalLinearConnectionBlock.setHorizontalConnection(state, world, pos))
                .setValue(SHAPE, setStairShapeNoFlip(state, world, pos));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Check if the block at the given position has an ItemRackBlockEntity associated with it.
        if (world.getBlockEntity(pos) instanceof CouchBlockEntity couchBlockEntity) {
            // Get the item stack that is currently stored in the block
            ItemStack storedItem = couchBlockEntity.getTheItem();

            if (stack.getItem() instanceof DyeItem dyeItem) {
                final int itemColor = dyeItem.getDyeColor().getTextureDiffuseColor();
                final int blockColor = ModColorHandler.getBlockColor(couchBlockEntity, -17170434);
                final int newColor = FastColor.ARGB32.average(blockColor, itemColor);
                if (blockColor == newColor) {
                    player.displayClientMessage(Component.translatable("message.cozyhome.same_color"), true);
                    return ItemInteractionResult.SUCCESS;
                }
                DataComponentMap components = DataComponentMap.builder().add(DataComponents.DYED_COLOR, new DyedItemColor(newColor, false)).build();
                couchBlockEntity.setComponents(components);

                stack.consume(1, player);
                couchBlockEntity.setChanged();
                world.sendBlockUpdated(pos, state, state, 0);
                return ItemInteractionResult.SUCCESS;
            }

            // Check if the item in hand is a valid tool or weapon.
            if (stack.getItem() instanceof CushionItem && !stack.isEmpty() && (storedItem.isEmpty())) {
                // If the stack is not empty, and the rack is either empty or can accept the item (same type and enough space),
                // proceed to insert the item into the block.

                // Increment the player's use stat for the item in their hand.
                player.awardStat(Stats.ITEM_USED.getOrCreateStat(stack.getItem()));

                // Split the stack unless the player is in creative mode (in which case the item won't be removed).
                ItemStack itemStack2 = stack.consumeAndReturn(1, player);

                // If the block was empty, store the item directly.
                if (couchBlockEntity.isEmpty()) {
                    couchBlockEntity.setTheItem(itemStack2);
                }

                if (couchBlockEntity.getTheItem() == ModItems.HAY_CUSHION.getDefaultInstance()) {
                    world.playSound(player, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                // Mark the block entity as dirty, indicating it has changed.
                couchBlockEntity.setChanged();

                // Notify the world that the block state has changed and trigger the block update.
                world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                // Emit a game event to notify of the block's state change
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                // Return a successful result to stop further interaction processing.
                return ItemInteractionResult.SUCCESS;

            } else if (!couchBlockEntity.isEmpty() && stack.getItem() == Items.SHEARS) {
                // Get the item stack currently in the block
                ItemStack storedStack = couchBlockEntity.getTheItem();

                // Try to give the player the item from the block
                ItemStack itemToGive = storedStack.copy();  // Create a copy of the stored item

                // If the player can hold the item (inventory space check)
                if (player.getInventory().add(itemToGive)) {
                    // Remove the item from the block (decrement the stack)
                    storedStack.shrink(1);  // Decrease the count of the item in the block

                    // If the block is now empty, clear the item rack
                    if (storedStack.isEmpty()) {
                        couchBlockEntity.setTheItem(ItemStack.EMPTY);
                    }

                    world.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Mark the block entity as dirty to save the changes
                    couchBlockEntity.setChanged();

                    // Notify the world about the block's state change
                    world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                    // Emit a game event to notify of the block's state change
                    world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                    // Return a success result
                    return ItemInteractionResult.SUCCESS;
                }
            } else {
                return super.useItemOn(stack, state, world, pos, player, hand, hit);
            }
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    // Causes the contents of the block to drop when block is broken.
    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        Containers.onRemove(state, newState, world, pos);
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(world, state, pos, entity, fallDistance * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(world, entity);
        } else {
            this.bounceEntity(entity);
        }
    }

    private void bounceEntity(Entity entity) {
        Vec3 vec3d = entity.getDeltaMovement();
        if (vec3d.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3d.x, -vec3d.y * 0.66F * d, vec3d.z);
        }
    }

    @Override
    public float getSeatRotation(BlockState state, Level world, BlockPos pos) {
        return ModProperties.setSeatRotationFromShape(state) + 180;
    }

    @Override
    public float getSeatHeight(BlockState state) {
        return 0.4f;
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() instanceof CouchBlock;
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
        tooltip.add(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
    }
}
