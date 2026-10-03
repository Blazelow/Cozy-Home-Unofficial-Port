package net.luckystudio.cozyhome.block.custom.water_holding_blocks.bathtub;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.block.util.enums.DoubleLongPart;
import net.luckystudio.cozyhome.block.util.interfaces.SeatBlock;
import net.luckystudio.cozyhome.block.util.interfaces.WaterHoldingBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelEvent;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
// Copied from BedBlock
public class BathTubBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, SeatBlock, WaterHoldingBlock {
    public static final MapCodec<BathTubBlock> CODEC = simpleCodec(BathTubBlock::new);

    // Boolean properties
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // Direction properties
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // Enum properties
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;
    public static final EnumProperty<DoubleLongPart> PART = ModProperties.DOUBLE_LONG_PART;

    // Integer properties
    public static final IntegerProperty LEVEL = ModProperties.FILLED_LEVEL_0_2;

    // Total of 8 combinations: 4 directions * 2 parts
    private static final VoxelShape[] SHAPES = new VoxelShape[8];

    public BathTubBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.generateShapes();
        this.registerDefaultState(super.defaultBlockState()
                .setValue(TRIGGERED, false)
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(CONTAINS, ContainsBlock.NONE)
                .setValue(PART, DoubleLongPart.FRONT)
                .setValue(LEVEL, 0));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BathTubBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<BathTubBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TRIGGERED, WATERLOGGED, FACING, CONTAINS, PART, LEVEL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getShapeIndex(state.getValue(FACING), state.getValue(PART))];
    }

    private static int getShapeIndex(Direction facing, DoubleLongPart part) {
        return facing.get2DDataValue() + (part == DoubleLongPart.BACK ? 4 : 0);
    }

    private void generateShapes() {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            for (DoubleLongPart part : DoubleLongPart.values()) {
                SHAPES[getShapeIndex(direction, part)] = createShape(direction, part);
            }
        }
    }

    private VoxelShape createShape(Direction facing, DoubleLongPart part) {
        boolean front = part == DoubleLongPart.FRONT;

        VoxelShape topShape = Block.box(0, 10, 0, 16, 12, 16);

        int minX = 1, maxX = 15, minZ = 1, maxZ = 15;
        switch (facing) {
            case WEST -> {
                if (front) minX = 0;
                else maxX = 16;
            }
            case EAST -> {
                if (front) maxX = 16;
                else minX = 0;
            }
            case NORTH -> {
                if (front) minZ = 0;
                else maxZ = 16;
            }
            case SOUTH -> {
                if (front) maxZ = 16;
                else minZ = 0;
            }
        }

        VoxelShape baseShape = Shapes.or(
                topShape,
                Block.box(minX, 1, minZ, maxX, 10, maxZ)
        );

        int holeMinX = 2, holeMaxX = 14, holeMinZ = 2, holeMaxZ = 14;
        switch (facing) {
            case WEST -> {
                if (front) holeMinX = 0;
                else holeMaxX = 16;
            }
            case EAST -> {
                if (front) holeMaxX = 16;
                else holeMinX = 0;
            }
            case NORTH -> {
                if (front) holeMinZ = 0;
                else holeMaxZ = 16;
            }
            case SOUTH -> {
                if (front) holeMaxZ = 16;
                else holeMinZ = 0;
            }
        }

        VoxelShape holeShape = Block.box(holeMinX, 2, holeMinZ, holeMaxX, 12, holeMaxZ);
        return Shapes.join(baseShape, holeShape, BooleanOp.ONLY_FIRST);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos
    ) {
        if (direction == getDirectionTowardsOtherPart(state.getValue(PART), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART)
                    ? state
                    : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private static Direction getDirectionTowardsOtherPart(DoubleLongPart part, Direction direction) {
        return part == DoubleLongPart.FRONT ? direction : direction.getOpposite();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        ContainsBlock contents = state.getValue(CONTAINS);
        int level = state.getValue(LEVEL);

        if (player.isSecondaryUseActive()) {
            BlockState stateToRun = state.getValue(PART) == DoubleLongPart.BACK ? state : getOtherPartState(state, world, pos);
            BlockPos posToRun = state.getValue(PART) == DoubleLongPart.BACK ? pos : getOtherPartPos(state, pos);
            return WaterHoldingBlock.toggleSwitch(stateToRun, world, posToRun, player);
        }

        // --- 0. Check if the block has water and the item is a soup ---
        if (WaterHoldingBlock.trySoup(item, world, pos, player, hand, contents)) {
            return ItemInteractionResult.SUCCESS;
        }

        // --- 1. Filling a bucket from a full block ---
        if (item == Items.BUCKET && level >= 1) {
            ItemStack filledBucket = contents == ContainsBlock.WATER ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(Items.LAVA_BUCKET);
            SoundEvent soundEvent = contents == ContainsBlock.WATER ? SoundEvents.BUCKET_FILL : SoundEvents.BUCKET_FILL_LAVA;
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filledBucket));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (level - 1 == 0) {
                world.setBlock(pos, state.setValue(LEVEL, 0).setValue(CONTAINS, ContainsBlock.NONE), 3);
                world.setBlock(getOtherPartPos(state, pos), getOtherPartState(state, world, pos).setValue(LEVEL, 0).setValue(CONTAINS, ContainsBlock.NONE), 3);
            } else {
                world.setBlock(pos, state.setValue(LEVEL, level - 1), 3);
                world.setBlock(getOtherPartPos(state, pos), getOtherPartState(state, world, pos).setValue(LEVEL, level - 1), 3);
            }
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            return ItemInteractionResult.SUCCESS;
        }

        // --- 2. Pouring water/lava bucket into the block ---
        if ((item == Items.WATER_BUCKET || item == Items.LAVA_BUCKET) && level < 2) {
            ContainsBlock newContents = item == Items.WATER_BUCKET ? ContainsBlock.WATER : ContainsBlock.LAVA;

            // Prevent mixing fluids
            if (contents != ContainsBlock.NONE && contents != newContents) {
                return SeatBlock.sitDown(state, world, pos, player);
            }

            SoundEvent soundEvent = newContents == ContainsBlock.WATER ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA;
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.getOrCreateStat(item));
            world.setBlock(pos, state.setValue(LEVEL, level + 1).setValue(CONTAINS, newContents), 3);
            world.setBlock(getOtherPartPos(state, pos), getOtherPartState(state, world, pos).setValue(LEVEL, level + 1).setValue(CONTAINS, newContents), 3);
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            return ItemInteractionResult.SUCCESS;
        }
        return SeatBlock.sitDown(state, world, pos, player);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return state.getValue(PART) == DoubleLongPart.BACK && state.getValue(TRIGGERED) ? createTickerHelper(type, ModBlockEntityTypes.BATHTUB_BLOCK_ENTITY, BathTubBlockEntity::tick) : null;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (state.getValue(CONTAINS) == ContainsBlock.WATER && state.getValue(LEVEL) > 0) {
            if (world.getBlockState(pos.below()).getBlock() == Blocks.MAGMA_BLOCK) {
                float randomOffset = (float) (Math.random() * 0.5 - 0.25);
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5 + randomOffset, pos.getY() + getLiquidLevelHeight(state), pos.getZ() + 0.5 + randomOffset, 0.0, 0.0, 0.0);
                world.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + 0.5 + randomOffset, pos.getY() + getLiquidLevelHeight(state), pos.getZ() + 0.5 + randomOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            DoubleLongPart tubPart = state.getValue(PART);
            if (tubPart == DoubleLongPart.FRONT) {
                BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(tubPart, state.getValue(FACING)));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.is(this) && blockState.getValue(PART) == DoubleLongPart.BACK) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                    world.levelEvent(player, LevelEvent.BLOCK_BROKEN, blockPos, Block.getId(blockState));
                }
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection();
        BlockPos blockPos = ctx.getClickedPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        Level world = ctx.getLevel();
        return world.getBlockState(blockPos2).canBeReplaced(ctx) && world.getWorldBorder().contains(blockPos2) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (!world.isClientSide) {
            BlockPos backPos = pos.offset(state.getValue(FACING));
            // Check if the offset position contains water
            boolean isWater = world.getFluidState(backPos).isSourceOfType(Fluids.WATER);
            // Set the blockstate at the back position with PART = BACK and WATERLOGGED if needed
            BlockState backState = state
                    .setValue(PART, DoubleLongPart.BACK)
                    .setValue(WATERLOGGED, isWater);
            world.setBlock(backPos, backState, Block.UPDATE_ALL);
            // Notify neighbors
            world.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighborsAt(world, pos, Block.UPDATE_ALL);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public float getSeatRotation(BlockState state, Level world, BlockPos pos) {
        return ModProperties.setSeatRotationFromFacing(state) + (state.getValue(PART) == DoubleLongPart.FRONT ? 0 : 180);
    }

    @Override
    public float getSeatHeight(BlockState state) {
        return 0.2f;
    }

    public static BlockPos getOtherPartPos(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return state.getValue(PART) == DoubleLongPart.FRONT ? pos.offset(facing) : pos.offset(facing.getOpposite());
    }

    public static BlockState getOtherPartState(BlockState state, Level world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos otherPartPos = state.getValue(PART) == DoubleLongPart.FRONT ? pos.offset(facing) : pos.offset(facing.getOpposite());
        return world.getBlockState(otherPartPos);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand_while_sneaking").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.toggle_switch")));
        tooltip.add(Component.translatable("tooltip.cozyhome.pulls_water_from").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.behind")));
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
            case 0 -> 0.125f;
            case 1 -> 0.5f;
            case 2 -> 0.6875f;
            default -> throw new IllegalStateException("Unexpected value: " + level);
        };
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

    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == 2;
    }

    @Override
    public void addLiquid(BlockState state, Level world, BlockPos pos, BlockState pullState, Direction pullDirection) {
        int level = state.getValue(LEVEL);
        int newLevel = Math.min(2, level + 1); // ensures level never goes above 2

        ContainsBlock contains = ContainsBlock.NONE;

        // Add  1 level of water without removing water from the block
        if (pullState.getFluidState().is(FluidTags.WATER) || pullState.hasProperty(BlockStateProperties.WATERLOGGED) && pullState.getValue(BlockStateProperties.WATERLOGGED)) {
            contains = ContainsBlock.WATER;
        }

        // Add 1 level of lava while removing lava from the block
        if (pullState.getFluidState().is(FluidTags.LAVA)) {
            world.setBlock(pos.offset(pullDirection), Blocks.AIR.defaultBlockState(), 3);
            contains = ContainsBlock.LAVA;
        }

        // Adding 1 water to the block while removing water from the block
        if (pullState.getBlock() == Blocks.WATER_CAULDRON) {
            world.setBlock(pos.offset(pullDirection), Blocks.CAULDRON.defaultBlockState(), 3);
            contains = ContainsBlock.WATER;
        }

        // Adding 1 lava to the block while removing lava from the block
        if (pullState.getBlock() == Blocks.LAVA_CAULDRON) {
            world.setBlock(pos.offset(pullDirection), Blocks.CAULDRON.defaultBlockState(), 3);
            contains = ContainsBlock.LAVA;
        }
        world.setBlock(pos, state.setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
        world.setBlock(getOtherPartPos(state, pos), getOtherPartState(state, world, pos).setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
    }

    public void removeLiquid(BlockState state, Level world, BlockPos pos) {
        int level = state.getValue(LEVEL);
        int newLevel = Math.max(0, level - 1); // ensures level never goes below 0
        ContainsBlock contains = newLevel == 0 ? ContainsBlock.NONE : state.getValue(CONTAINS);
        world.setBlock(pos, state.setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
        world.setBlock(getOtherPartPos(state, pos), getOtherPartState(state, world, pos).setValue(LEVEL, newLevel).setValue(CONTAINS, contains), 3);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
        world.gameEvent(GameEvent.BLOCK_CHANGE, getOtherPartPos(state, pos), GameEvent.Emitter.of(getOtherPartState(state, world, pos)));
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (!world.isClientSide && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
                if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
                    entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
                    entity.hurt(world.damageSources().lava(), 3.0F);
                    entity.igniteForSeconds(2.0F);
                } else if (state.getValue(CONTAINS) == ContainsBlock.WATER && entity.isOnFire()) {
                    entity.clearFire();
                    if (entity.mayInteract(world, pos)) {
                        removeLiquid(state, world, pos);
                    }
                }
            }
        }
    }
}
