package net.luckystudio.cozyhome.block.custom.toolrack;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/** A wall mounted rack that displays one tool or weapon. */
public class ToolRackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, ItemTooltipProvider {
    public static final MapCodec<ToolRackBlock> CODEC = simpleCodec(ToolRackBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape NORTH_SHAPE = Shapes.or(
            Block.box(2, 4, 14, 14, 12, 16),
            Block.box(10, 7, 11, 12, 9, 14),
            Block.box(4, 7, 11, 6, 9, 14));
    public static final VoxelShape EAST_SHAPE = Shapes.or(
            Block.box(0, 4, 2, 2, 12, 14),
            Block.box(2, 7, 10, 5, 9, 12),
            Block.box(2, 7, 4, 5, 9, 6));
    public static final VoxelShape SOUTH_SHAPE = Shapes.or(
            Block.box(2, 4, 0, 14, 12, 2),
            Block.box(4, 7, 2, 6, 9, 5),
            Block.box(10, 7, 2, 12, 9, 5));
    public static final VoxelShape WEST_SHAPE = Shapes.or(
            Block.box(14, 4, 2, 16, 12, 14),
            Block.box(11, 7, 4, 14, 9, 6),
            Block.box(11, 7, 10, 14, 9, 12));

    // A shield on the rack also blocks movement in front of it
    public static final VoxelShape NORTH_SHIELD_SHAPE = Shapes.or(NORTH_SHAPE, Block.box(2, -3, 10, 14, 19, 12));
    public static final VoxelShape EAST_SHIELD_SHAPE = Shapes.or(EAST_SHAPE, Block.box(4, -3, 2, 6, 19, 14));
    public static final VoxelShape SOUTH_SHIELD_SHAPE = Shapes.or(SOUTH_SHAPE, Block.box(2, -3, 4, 14, 19, 6));
    public static final VoxelShape WEST_SHIELD_SHAPE = Shapes.or(WEST_SHAPE, Block.box(10, -3, 2, 12, 19, 14));

    public ToolRackBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemRackBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    private static VoxelShape shapeFor(Direction facing, boolean shield) {
        return switch (facing) {
            case EAST -> shield ? EAST_SHIELD_SHAPE : EAST_SHAPE;
            case SOUTH -> shield ? SOUTH_SHIELD_SHAPE : SOUTH_SHAPE;
            case WEST -> shield ? WEST_SHIELD_SHAPE : WEST_SHAPE;
            default -> shield ? NORTH_SHIELD_SHAPE : NORTH_SHAPE;
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapeFor(state.getValue(FACING), false);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean shield = world.getBlockEntity(pos) instanceof ItemRackBlockEntity rack && rack.getStack().getItem() instanceof ShieldItem;
        return shapeFor(state.getValue(FACING), shield);
    }

    @Override
    protected void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        super.onProjectileHit(world, state, hit, projectile);
        if (!world.isClientSide()) {
            BlockPos pos = hit.getBlockPos();
            if (world.getBlockEntity(pos) instanceof ItemRackBlockEntity rack && rack.getStack().getItem() instanceof ShieldItem) {
                world.playSound(null, pos, SoundEvents.SHIELD_BLOCK.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return Block.canSupportCenter(world, pos.relative(facing.getOpposite()), facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!ctx.replacingClickedOnBlock()) {
            BlockState clicked = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite()));
            if (clicked.is(this) && clicked.getValue(FACING) == ctx.getClickedFace()) {
                return null;
            }
        }
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        for (Direction direction : ctx.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState state = this.defaultBlockState().setValue(FACING, direction.getOpposite());
                if (state.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
                    return state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof ItemRackBlockEntity rack)) {
            return InteractionResult.PASS;
        }
        ItemStack held = rack.getStack();

        if (isToolOrWeapon(stack)) {
            // The rack takes one item at a time, stacking more of the same kind
            boolean canAdd = held.isEmpty() || (ItemStack.isSameItemSameComponents(held, stack) && held.getCount() < held.getMaxStackSize());
            if (!canAdd) return InteractionResult.FAIL;
            if (world.isClientSide()) return InteractionResult.SUCCESS; // the server does the work

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            ItemStack one = stack.copyWithCount(1);
            stack.consume(1, player);
            float fill;
            if (held.isEmpty()) {
                rack.setStack(one);
                fill = 1.0F / one.getMaxStackSize();
            } else {
                held.grow(1);
                rack.setStack(held);
                fill = (float) held.getCount() / (float) held.getMaxStackSize();
            }
            world.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * fill);
            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.SUCCESS;
        }

        if (!held.isEmpty() && stack.isEmpty()) {
            if (world.isClientSide()) return InteractionResult.SUCCESS; // the server does the work
            // An empty hand takes one item back off the rack
            ItemStack give = held.copyWithCount(1);
            if (!player.getInventory().add(give)) {
                player.drop(give, false);
            }
            held.shrink(1);
            rack.setStack(held.isEmpty() ? ItemStack.EMPTY : held);
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.SUCCESS;
        }
        return stack.isEmpty() ? InteractionResult.TRY_WITH_EMPTY_HAND : InteractionResult.PASS;
    }

    private static boolean isToolOrWeapon(ItemStack stack) {
        Item item = stack.getItem();
        return stack.is(ItemTags.PICKAXES) || stack.is(ItemTags.AXES) || stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.HOES)
                || stack.is(ItemTags.SWORDS) || stack.is(ItemTags.SPEARS)
                || item instanceof ProjectileWeaponItem
                || item instanceof TridentItem
                || item instanceof MaceItem
                || item instanceof ShieldItem
                || item instanceof FishingRodItem
                || item instanceof SpyglassItem
                || item instanceof InstrumentItem;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("tooltip.cozyhome.block.can_hold").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.weapons_and_tools")));
    }
}
