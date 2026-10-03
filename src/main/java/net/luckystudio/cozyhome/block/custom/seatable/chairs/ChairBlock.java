package net.luckystudio.cozyhome.block.custom.seatable.chairs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.luckystudio.cozyhome.block.custom.AbstractSeatBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.TuckableBlock;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class ChairBlock extends AbstractSeatBlock implements TuckableBlock, SimpleWaterloggedBlock {
    public static final MapCodec<ChairBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ChairBlock.ChairType.CODEC.fieldOf("kind").forGetter(ChairBlock::getChairType),
                    createSettingsCodec() // Ensure this exists and works as expected
            ).apply(instance, ChairBlock::new)
    );
    public static final BooleanProperty TUCKED = ModProperties.TUCKED;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    private static final VoxelShape BASE_SHAPE = ChairBlock.box(2,0,2,14,10,14);
    public static final VoxelShape TUCKED_SOUTH = Shapes.or(
            Block.box(2, 0, -8, 14, 10, 4),
            Block.box(2, 10, 2, 14, 24, 4));
    public static final VoxelShape TUCKED_WEST = Shapes.or(
            Block.box(12, 0, 2, 24, 10, 14),
            Block.box(12, 10, 2, 14, 24, 14));
    public static final VoxelShape TUCKED_NORTH = Shapes.or(
            Block.box(2, 0, 12, 14, 10, 24),
            Block.box(2, 10, 12, 14, 24, 14));
    public static final VoxelShape TUCKED_EAST = Shapes.or(
            Block.box(-8, 0, 2, 4, 10, 14),
            Block.box(2, 10, 2, 4, 24, 14));
    private final ChairType type;

    public ChairBlock(ChairType chairType, BlockBehaviour.Properties settings) {
        super(settings);
        this.defaultBlockState()
                .setValue(TUCKED, false)
                .setValue(ROTATION, 0);
        this.type = chairType;
    }


    @Override
    protected MapCodec<? extends AbstractSeatBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TUCKED, ROTATION);
    }

    // This is the hit-box of the block, we are applying our VoxelShape to it.
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch (state.getValue(ROTATION)) {
            case 0:
                if (state.getValue(TUCKED)) return TUCKED_SOUTH;
            case 4:
                if (state.getValue(TUCKED)) return TUCKED_WEST;
            case 8:
                if (state.getValue(TUCKED)) return TUCKED_NORTH;
            case 12:
                if (state.getValue(TUCKED)) return TUCKED_EAST;
            case null, default:
                return BASE_SHAPE;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean isSneaking = Objects.requireNonNull(ctx.getPlayer()).isShiftKeyDown();
        int rotationOffset = isSneaking ? 180 : 0;
        return Objects.requireNonNull(super.getStateForPlacement(ctx))
                .setValue(TUCKED, false)
                .setValue(ROTATION, RotationSegment.convertToSegment(ctx.getPlayerYaw() + rotationOffset));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return ItemInteractionResult.SUCCESS;
        // Check if the block at the given position has an ItemRackBlockEntity associated with it.
        if (world.getBlockEntity(pos) instanceof ChairBlockEntity chairBlockEntity) {
            // Get the item stack that is currently stored in the block
            ItemStack storedItem = chairBlockEntity.getStack();
            // Check if the item in hand is a valid tool or weapon.
            if (stack.getItem() instanceof CushionItem && !stack.isEmpty() && (storedItem.isEmpty())) {
                // If the stack is not empty, and the rack is either empty or can accept the item (same type and enough space),
                // proceed to insert the item into the block.

                // Increment the player's use stat for the item in their hand.
                player.awardStat(Stats.USED.getOrCreateStat(stack.getItem()));

                // Split the stack unless the player is in creative mode (in which case the item won't be removed).
                ItemStack itemStack2 = stack.consumeAndReturn(1, player);

                // If the block was empty, store the item directly.
                if (chairBlockEntity.isEmpty()) {
                    chairBlockEntity.setStack(itemStack2);
                }

                if (chairBlockEntity.getStack() == ModItems.HAY_CUSHION.getDefaultInstance()) {
                    world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                // Mark the block entity as dirty, indicating it has changed.
                chairBlockEntity.setChanged();

                // Notify the world that the block state has changed and trigger the block update.
                world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                // Emit a game event to notify of the block's state change
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                // Return a successful result to stop further interaction processing.
                return ItemInteractionResult.SUCCESS;

            } else if (!chairBlockEntity.isEmpty() && stack.getItem() == Items.SHEARS) {
                // Get the item stack currently in the block
                ItemStack storedStack = chairBlockEntity.getStack();

                // Try to give the player the item from the block
                ItemStack itemToGive = storedStack.copy();  // Create a copy of the stored item

                // If the player can hold the item (inventory space check)
                if (player.getInventory().insertStack(itemToGive)) {
                    // Remove the item from the block (decrement the stack)
                    storedStack.shrink(1);  // Decrease the count of the item in the block

                    // If the block is now empty, clear the item rack
                    if (storedStack.isEmpty()) {
                        chairBlockEntity.setStack(ItemStack.EMPTY);
                    }

                    world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Mark the block entity as dirty to save the changes
                    chairBlockEntity.setChanged();

                    // Notify the world about the block's state change
                    world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                    // Emit a game event to notify of the block's state change
                    world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                    // Return a success result
                    return ItemInteractionResult.SUCCESS;
                }
            } else if (player.isShiftKeyDown()) {
                // Call tuckable logic or fallback to super
                TuckableBlock.toggleTuck(state, world, pos, player);
                return ItemInteractionResult.SUCCESS;
            } else {
                return super.useItemOn(stack, state, world, pos, player, hand, hit);
            }
        }
        // If the block at the given position doesn't have a block entity (ItemRackBlockEntity), skip default interaction.
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;
        if (player.isShiftKeyDown() || state.getValue(TUCKED)) {
            // Call tuckable logic or fallback to super
            TuckableBlock.toggleTuck(state, world, pos, player);
            return InteractionResult.SUCCESS;
        }
         return super.onUse(state, world, pos, player, hit);
    }

    public enum Type implements ChairType {
        OAK("oak"),
        SPRUCE("spruce"),
        BIRCH("birch"),
        JUNGLE("jungle"),
        ACACIA("acacia"),
        DARK_OAK("dark_oak"),
        MANGROVE("mangrove"),
        CHERRY("cherry"),
        BAMBOO("bamboo"),
        CRIMSON("crimson"),
        WARPED("warped"),
        PRINCESS("princess"),
        IRON("iron"),
        GLASS("iron"),
        UNDEAD("undead"),
        OMINOUS("ominous");

        private final String id;

        Type(final String id) {
            this.id = id;
            TYPES.put(id, this);
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    public ChairType getChairType() {
        return this.type;
    }

    public interface ChairType extends StringRepresentable {
        Map<String, ChairType> TYPES = new Object2ObjectArrayMap<>();
        Codec<ChairType> CODEC = Codec.stringResolver(StringRepresentable::asString, TYPES::get);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand_while_sneaking").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.can_tuck_into_certain_blocks")));
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_cushion").formatted(ChatFormatting.GRAY));
    }

    // Causes the contents of the block to drop when block is broken.
    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        Containers.onRemove(state, newState, world, pos);
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public float getSeatHeight(BlockState state) {
        return 0.65f;
    }

    @Override
    public float getSeatRotation(BlockState state, Level world, BlockPos pos) {
        return ModProperties.setSeatRotationFromRotation(state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), MAX_ROTATIONS));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), MAX_ROTATIONS));
    }
}
