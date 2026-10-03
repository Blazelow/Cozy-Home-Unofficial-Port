package net.luckystudio.cozyhome.block.custom.fountains;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.custom.horizontal_connecting_blocks.AbstractHorizontalConnectingBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.block.util.interfaces.AllSidesConnectingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class FountainBlock extends AbstractHorizontalConnectingBlock implements AllSidesConnectingBlock {
    public static final MapCodec<FountainBlock> CODEC = createCodec(FountainBlock::new);
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;

    public static final VoxelShape TOP_PIECE = Block.box(0, 10, 0, 16, 16, 16);
    public static final VoxelShape TOP_PIECE_VOID = Block.box(2, 14, 2, 14, 16, 14);
    public static final VoxelShape TOP = Shapes.join(TOP_PIECE, TOP_PIECE_VOID, BooleanOp.ONLY_FIRST);
    public static final VoxelShape MIDDLE = Block.box(4, 2, 4, 12, 10, 12);
    public static final VoxelShape BASE = Block.box(2, 0, 2, 14, 2, 14);
    public static final VoxelShape SHAPE = Shapes.or(TOP, MIDDLE, BASE);

    public FountainBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateManager.defaultBlockState()
                .setValue(CONTAINS, ContainsBlock.NONE));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CONTAINS));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ContainsBlock contents = state.getValue(CONTAINS);
        if (stack.getItem() == Items.WATER_BUCKET) {
            player.setItemInHand(hand, ItemUtils.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
            return changeState(state, ContainsBlock.WATER, SoundEvents.ITEM_BUCKET_EMPTY, world, pos, player);
        } else if (stack.getItem() == Items.LAVA_BUCKET) {
            player.setItemInHand(hand, ItemUtils.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
            return changeState(state, ContainsBlock.LAVA, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, world, pos, player);
        } else if (contents != ContainsBlock.NONE) {
            if (stack.getItem() == Items.BUCKET) {
                if (contents == ContainsBlock.WATER) {
                    player.setItemInHand(hand, ItemUtils.exchangeStack(stack, player, new ItemStack(Items.WATER_BUCKET)));
                    return changeState(state, ContainsBlock.NONE, SoundEvents.ITEM_BUCKET_FILL, world, pos, player);
                } else if (contents == ContainsBlock.LAVA) {
                    player.setItemInHand(hand, ItemUtils.exchangeStack(stack, player, new ItemStack(Items.LAVA_BUCKET)));
                    return changeState(state, ContainsBlock.NONE, SoundEvents.ITEM_BUCKET_FILL_LAVA, world, pos, player);
                }
            } else {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static ItemInteractionResult changeState(BlockState state, ContainsBlock newContains, SoundEvent soundEvent, Level world, BlockPos pos, Player player) {
        // Only run if the state actually should change
        if (state.getValue(CONTAINS) != newContains) {
            state = state.setValue(ModProperties.CONTAINS, newContains);
            world.setBlock(pos, state, Block.UPDATE_ALL);
            world.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1F, 1f);
            world.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    // This spawns particles when the block contains lava using the LavaFluid Classes animateTick method
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
            LavaFluid lavaFluid = (LavaFluid) Fluids.LAVA;
            lavaFluid.animateTick(world, pos, state.getFluidState(), random);
        }
    }

    // Handles entity effects based on the block’s fill state (LAVA, POWDER_SNOW, WATER)
    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        ContainsBlock fillState = state.getValue(CONTAINS);

        // If block contains lava, apply burn damage and set the entity on fire
        if (fillState == ContainsBlock.LAVA && entity instanceof LivingEntity) {
            entity.damage(world.damageSources().hotFloor(), 4.0F);
            entity.igniteForSeconds(3);
        }

        // Call the superclass method after handling custom effects
        super.stepOn(world, pos, state, entity);
    }

    // So mobs don't stand on it
    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, context, tooltip, options);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.block.can_hold").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("block.minecraft.water")));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("block.minecraft.lava")));
    }

    @Override
    public void onBroken(LevelAccessor worldAccess, BlockPos pos, BlockState state) {
        onBlockDestroyed((Level)worldAccess, state, pos);
        super.onBroken(worldAccess, pos, state);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        onBlockDestroyed(world, state, pos);
        return super.playerWillDestroy(world, pos, state, player);
    }

    // Spill out liquids when the block is destroyed
    private static void onBlockDestroyed(Level world, BlockState state, BlockPos pos) {
            if (state.getValue(CONTAINS) == ContainsBlock.WATER) {
                FluidState fluidState = Fluids.FLOWING_WATER.defaultBlockState().setValue(BlockStateProperties.LEVEL_1_8, 4);
                world.setBlock(pos, fluidState.getBlockState(), Block.UPDATE_ALL);
            }
            if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
                FluidState fluidState = Fluids.FLOWING_LAVA.defaultBlockState().setValue(BlockStateProperties.LEVEL_1_8, 4);
                world.setBlock(pos, fluidState.getBlockState(), Block.UPDATE_ALL);
            }
    }

    @Override
    public boolean isMatchingBlock(BlockState state, BlockState targetState) {
        return targetState.getBlock() instanceof FountainBlock &&
                targetState.hasProperty(CONTAINS) && state.hasProperty(CONTAINS) &&
                targetState.getValue(CONTAINS).equals(state.getValue(CONTAINS));
    }
}