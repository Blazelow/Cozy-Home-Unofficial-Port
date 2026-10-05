package net.luckystudio.cozyhome.block.custom.seatable.sofas;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ARGB;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.luckystudio.cozyhome.block.custom.AbstractSeatBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.item.ModItems;
import net.luckystudio.cozyhome.item.custom.CushionItem;
import net.luckystudio.cozyhome.util.ModColorHandler;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
public class SofaBlock extends AbstractSeatBlock implements ItemTooltipProvider  {
    public static final MapCodec<SofaBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    SofaBlock.SofaType.CODEC.fieldOf("kind").forGetter(SofaBlock::getSofaType),
                    propertiesCodec() // Ensure this exists and works as expected
            ).apply(instance, SofaBlock::new)
    );

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    private static final VoxelShape BASE_SHAPE = SofaBlock.box(0, 2, 0, 16, 8, 16);
    private final SofaBlock.SofaType type;

    public SofaBlock(SofaBlock.SofaType SofaType, BlockBehaviour.Properties settings) {
        super(settings);
        this.defaultBlockState().setValue(ROTATION, 0);
        this.type = SofaType;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SofaBlockEntity(pos,state);
    }

    @Override
    protected MapCodec<? extends AbstractSeatBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean isSneaking = ctx.getPlayer().isShiftKeyDown();
        int rotationOffset = isSneaking ? 180 : 0;
        return super.getStateForPlacement(ctx)
                .setValue(ROTATION, RotationSegment.convertToSegment(ctx.getRotation() + rotationOffset));
    }

    // This is the hit-box of the block, we are applying our VoxelShape to it.
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BASE_SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Check if the block at the given position has an ItemRackBlockEntity associated with it.
        if (world.getBlockEntity(pos) instanceof SofaBlockEntity sofaBlockEntity) {
            if (stack.has(DataComponents.DYE)) {
                final int itemColor = stack.get(DataComponents.DYE).getTextureDiffuseColor();
                final int blockColor = ModColorHandler.getBlockColor(sofaBlockEntity, -17170434);
                final int newColor = ARGB.average(blockColor, itemColor);
                if (blockColor == newColor) {
                    player.sendOverlayMessage(Component.translatable("message.cozyhome.same_color"));
                    return InteractionResult.SUCCESS;
                }
                DataComponentMap components = DataComponentMap.builder().set(DataComponents.DYED_COLOR, new DyedItemColor(newColor)).build();
                sofaBlockEntity.setComponents(components);

                stack.consume(1, player);
                sofaBlockEntity.setChanged();
                world.sendBlockUpdated(pos, state, state, 0);
                return InteractionResult.SUCCESS;
            }
            ItemStack storedItem = sofaBlockEntity.getTheItem();
            // Check if the item in hand is a valid tool or weapon.
            if (stack.getItem() instanceof CushionItem && !stack.isEmpty() && (storedItem.isEmpty())) {
                // Get the item stack that is currently stored in the block
                // If the stack is not empty, and the rack is either empty or can accept the item (same type and enough space),
                // proceed to insert the item into the block.

                // Increment the player's use stat for the item in their hand.
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                // Split the stack unless the player is in creative mode (in which case the item won't be removed).
                ItemStack itemStack2 = stack.consumeAndReturn(1, player);

                // If the block was empty, store the item directly.
                if (sofaBlockEntity.isEmpty()) {
                    sofaBlockEntity.setTheItem(itemStack2);
                }

                if (sofaBlockEntity.getTheItem() == ModItems.HAY_CUSHION.getDefaultInstance()) {
                    world.playSound(player, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                // Mark the block entity as dirty, indicating it has changed.
                sofaBlockEntity.setChanged();

                // Notify the world that the block state has changed and trigger the block update.
                world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                // Emit a game event to notify of the block's state change
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                // Return a successful result to stop further interaction processing.
                return InteractionResult.SUCCESS;

            } else if (!sofaBlockEntity.isEmpty() && stack.getItem() == Items.SHEARS) {
                // Get the item stack currently in the block
                ItemStack storedStack = sofaBlockEntity.getTheItem();

                // Try to give the player the item from the block
                ItemStack itemToGive = storedStack.copy();  // Create a copy of the stored item

                // If the player can hold the item (inventory space check)
                if (player.getInventory().add(itemToGive)) {
                    // Remove the item from the block (decrement the stack)
                    storedStack.shrink(1);  // Decrease the count of the item in the block

                    // If the block is now empty, clear the item rack
                    if (storedStack.isEmpty()) {
                        sofaBlockEntity.setTheItem(ItemStack.EMPTY);
                    }

                    world.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Mark the block entity as dirty to save the changes
                    sofaBlockEntity.setChanged();

                    // Notify the world about the block's state change
                    world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                    // Emit a game event to notify of the block's state change
                    world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                    // Return a success result
                    return InteractionResult.SUCCESS;
                }
            } else {
                return super.useItemOn(stack, state, world, pos, player, hand, hit);
            }
        }
        // If the block at the given position doesn't have a block entity (ItemRackBlockEntity), skip default interaction.
        return InteractionResult.PASS;
    }

    public enum Type implements SofaBlock.SofaType {
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

    public SofaBlock.SofaType getSofaType() {
        return this.type;
    }

    public interface SofaType extends StringRepresentable {
        Map<String, SofaBlock.SofaType> TYPES = new Object2ObjectArrayMap<>();
        Codec<SofaBlock.SofaType> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        super.fallOn(world, state, pos, entity, fallDistance * 0.5);
    }

    // TODO 26.2: Block#updateEntityMovementAfterFallOn changed; this is not called until it is hooked up again
    public void updateEntityMovementAfterFallOn(BlockGetter world, Entity entity) {
        if (!entity.isSuppressingBounce()) {
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
        return ModProperties.setSeatRotationFromRotation(state);
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable("tooltip.cozyhome.dyeable").withStyle(ChatFormatting.GRAY));
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
