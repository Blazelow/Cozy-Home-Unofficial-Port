package net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink;

import net.neoforged.fml.ModList;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.AbstractWaterHoldingBlockEntity;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.block.util.interfaces.WaterHoldingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
public abstract class AbstractSinkBlock extends BaseEntityBlock implements WaterHoldingBlock {

    // Boolean properties
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    // Direction properties
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // Enum properties
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;

    // Integer properties
    public static final IntegerProperty LEVEL = ModProperties.FILLED_LEVEL_0_3;

    public AbstractSinkBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateManager.defaultBlockState()
                .setValue(TRIGGERED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(CONTAINS, ContainsBlock.NONE)
                .setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TRIGGERED, FACING, CONTAINS, LEVEL));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        ContainsBlock contents = state.getValue(CONTAINS);
        int level = state.getValue(LEVEL);

        // --- 0. Check if the block has water and the item is a soup ---
        if (WaterHoldingBlock.trySoup(item, world, pos, player, hand, contents)) {
            return ItemInteractionResult.SUCCESS;
        }

        // --- 1. Filling a bucket from a full block ---
        if (item == Items.BUCKET && level == 3 && contents != ContainsBlock.NONE) {
            ItemStack filledBucket = contents == ContainsBlock.WATER ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(Items.LAVA_BUCKET);
            SoundEvent soundEvent = contents == ContainsBlock.WATER ? SoundEvents.BUCKET_FILL : SoundEvents.BUCKET_FILL_LAVA;
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filledBucket));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.setBlock(pos, state.setValue(LEVEL, 0).setValue(CONTAINS, ContainsBlock.NONE), 3);
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            return ItemInteractionResult.SUCCESS;
        }

        // --- 2. Pouring water/lava bucket into the block ---
        if ((item == Items.WATER_BUCKET || item == Items.LAVA_BUCKET) && level < 3) {
            ContainsBlock newContents = item == Items.WATER_BUCKET ? ContainsBlock.WATER : ContainsBlock.LAVA;
            SoundEvent soundEvent = newContents == ContainsBlock.WATER ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA;
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
            world.setBlock(pos, state.setValue(LEVEL, 3).setValue(CONTAINS, newContents), 3);
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            return ItemInteractionResult.SUCCESS;
        }

        // --- 3. Using a water bottle to fill the block ---
        if (item == Items.POTION && ((contents == ContainsBlock.WATER && level < 3) || contents == ContainsBlock.NONE)) {
            PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
            if (potionContentsComponent != null && potionContentsComponent.matches(Potions.WATER)) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
                world.setBlock(pos, state.setValue(LEVEL, level + 1).setValue(CONTAINS, ContainsBlock.WATER), 3);
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                return ItemInteractionResult.SUCCESS;
            } else {
                return WaterHoldingBlock.toggleSwitch(state, world, pos, player);
            }
        }

        // --- 4. Filling a bottle from the block ---
        if (item == Items.GLASS_BOTTLE && contents == ContainsBlock.WATER && level > 0) {
            int newLevel = level - 1;
            ContainsBlock newContents = newLevel == 0 ? ContainsBlock.NONE : contents;
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
            world.setBlock(pos, state.setValue(LEVEL, level - 1).setValue(CONTAINS, newContents), 3);
            world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            return ItemInteractionResult.SUCCESS;
        }
        return WaterHoldingBlock.toggleSwitch(state, world, pos, player);
    }

    // CARLOS IS GAY

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntityTypes.SINK_BLOCK_ENTITY, SinkBlockEntity::tick);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(CONTAINS, ContainsBlock.NONE)
                .setValue(FACING, ctx.getHorizontalDirection())
                .setValue(TRIGGERED, false);
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
    public float getLiquidLevelHeight(BlockState state) {
        int level = state.getValue(LEVEL);
        return switch (level) {
            case 1 -> 0.438f;
            case 2 -> 0.688f;
            case 3 -> 0.938f;
            default -> 0.125f;
        };
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == 3;
    }

    @Override
    public void addLiquid(BlockState state, Level world, BlockPos pos, BlockState pullState, Direction pullDirection) {
        int level = state.getValue(LEVEL);
        int newLevel = Math.min(3, level + 1); // ensures level never goes above 2
        ContainsBlock contains;
        if (pullState.getFluidState().is(FluidTags.WATER) || pullState.hasProperty(BlockStateProperties.WATERLOGGED) && pullState.getValue(BlockStateProperties.WATERLOGGED)) {
            contains = ContainsBlock.WATER;
            world.setBlock(pos, state.setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
            return;
        }
        if (pullState.getFluidState().is(FluidTags.LAVA)) {
            world.setBlock(pos.offset(pullDirection), Blocks.AIR.defaultBlockState(), 3);
            contains = ContainsBlock.LAVA;
            world.setBlock(pos, state.setValue(LEVEL, 3).setValue(CONTAINS, contains), 3);
            return;
        }
        if (pullState.getBlock() == Blocks.WATER_CAULDRON) {
            LayeredCauldronBlock.decrementFluidLevel(pullState, world, pos.offset(pullDirection));
            contains = ContainsBlock.WATER;
            world.setBlock(pos, state.setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
            return;
        }
        if (pullState.getBlock() == Blocks.LAVA_CAULDRON) {
            world.setBlock(pos.offset(pullDirection), Blocks.CAULDRON.defaultBlockState(), 3);
            contains = ContainsBlock.LAVA;
            world.setBlock(pos, state.setValue(LEVEL, 3).setValue(CONTAINS, contains), 3);
        }
    }

    @Override
    public void removeLiquid(BlockState state, Level world, BlockPos pos) {
        int level = state.getValue(LEVEL);
        int newLevel = Math.max(0, level - 1); // ensures level never goes below 0
        ContainsBlock contains = newLevel == 0 ? ContainsBlock.NONE : state.getValue(CONTAINS);
        world.setBlock(pos, state.setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
    }

    @Override
    public List<Direction> getDirectionsToPull(BlockState state) {
        Direction behind = state.getValue(FACING);
        return List.of(behind);
    }

    @Override
    public Direction pullingDirection(BlockState state, Level world, BlockPos pos) {
        for (Direction direction : getDirectionsToPull(state)) {
            BlockPos offsetPos = pos.offset(direction);
            BlockState offsetState = world.getBlockState(offsetPos);
            if (offsetState.getFluidState().is(FluidTags.WATER) || offsetState.getFluidState().is(FluidTags.LAVA) || offsetState.getBlock() == Blocks.WATER_CAULDRON || offsetState.getBlock() == Blocks.LAVA_CAULDRON) {
                return direction;
            }
        }
        return null;
    }

    // Handles entity effects based on the block’s fill state (LAVA, POWDER_SNOW, WATER)
    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        ContainsBlock fillState = state.getValue(CONTAINS);

        // If block contains lava, apply burn damage and set the entity on fire
        if (fillState == ContainsBlock.LAVA && entity instanceof LivingEntity) {
            entity.hurt(world.damageSources().hotFloor(), 4.0F);
            entity.igniteForSeconds(3);
        }

        // Call the superclass method after handling custom effects
        super.stepOn(world, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (world.getBlockEntity(pos) instanceof AbstractWaterHoldingBlockEntity blockEntity) {
            if (world.isClientSide && blockEntity.soupTime > 0) {
                Minecraft client = Minecraft.getInstance();
                ParticleEngine particleManager = client.particleManager;

                // The particle ID (e.g. "supplementaries:suds") — must be registered with a factory!
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath("supplementaries", "suds");
                ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(id);

                if (type != null) {
                    double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
                    double y = pos.getY() + getLiquidLevelHeight(state);
                    double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

                    particleManager.addParticle((ParticleOptions) type, x, y, z, 0.0, 0.01, 0.0);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand_while_sneaking").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.toggle_switch")));
        if (ModList.get().isLoaded("supplementaries")) {
            tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_soup").withStyle(ChatFormatting.GRAY));
            tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.add_bubbles")));
        }
    }
}
