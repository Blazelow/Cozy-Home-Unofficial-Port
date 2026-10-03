package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.TripleTallBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelEvent;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class GrandfatherClockBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<GrandfatherClockBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(GrandfatherClockType.CODEC.fieldOf("kind").forGetter(GrandfatherClockBlock::getGrandfatherClockType), createSettingsCodec())
                    .apply(instance, GrandfatherClockBlock::new));

    public static final EnumProperty<TripleTallBlock> TRIPLE_TALL_BLOCK = ModProperties.TRIPLE_TALL_BLOCK;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final int MAX_ROTATION_INDEX = RotationSegment.getMaxSegmentIndex();
    protected static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;

    private static final VoxelShape BOTTOM_BOTTOM_PIECE = GrandfatherClockBlock.box(1, 0, 1, 15, 2, 15);
    private static final VoxelShape BOTTOM_MIDDLE_PIECE = GrandfatherClockBlock.box(2, 2, 2, 14, 30, 14);
    private static final VoxelShape BOTTOM_TOP_PIECE = GrandfatherClockBlock.box(1, 30, 1, 15, 44, 15);
    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(BOTTOM_BOTTOM_PIECE, BOTTOM_MIDDLE_PIECE, BOTTOM_TOP_PIECE);
    private static final VoxelShape MIDDLE_BOTTOM_PIECE = GrandfatherClockBlock.box(1, -16, 1, 15, -14, 15);
    private static final VoxelShape MIDDLE_MIDDLE_PIECE = GrandfatherClockBlock.box(2, -14, 2, 14, 14, 14);
    private static final VoxelShape MIDDLE_TOP_PIECE = GrandfatherClockBlock.box(1, 14, 1, 15, 28, 15);
    private static final VoxelShape MIDDLE_SHAPE = Shapes.or(MIDDLE_BOTTOM_PIECE, MIDDLE_MIDDLE_PIECE, MIDDLE_TOP_PIECE);
    private static final VoxelShape TOP_BOTTOM_PIECE = GrandfatherClockBlock.box(1, -32, 1, 15, -30, 15);
    private static final VoxelShape TOP_MIDDLE_PIECE = GrandfatherClockBlock.box(2, -30, 2, 14, -2, 14);
    private static final VoxelShape TOP_TOP_PIECE = GrandfatherClockBlock.box(1, -2, 1, 15, 12, 15);
    private static final VoxelShape TOP_SHAPE = Shapes.or(TOP_BOTTOM_PIECE, TOP_MIDDLE_PIECE, TOP_TOP_PIECE);

    private final GrandfatherClockType type;

    @Override
    protected MapCodec<? extends GrandfatherClockBlock> codec() {
        return CODEC;
    }

    public GrandfatherClockBlock(GrandfatherClockType grandfatherClockType, BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(WATERLOGGED, false)
                .setValue(TRIPLE_TALL_BLOCK, TripleTallBlock.BOTTOM)
                .setValue(TRIGGERED, false)
                .setValue(ROTATION, 0));
        this.type = grandfatherClockType;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRIPLE_TALL_BLOCK, TRIGGERED, ROTATION, WATERLOGGED);
    }

    /**
     * This creates the solid looking hitbox for the entire block
     */
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TRIPLE_TALL_BLOCK)) {
            case TOP -> TOP_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case BOTTOM -> BOTTOM_SHAPE;
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrandfatherClockBlockEntity(pos, state);
    }

    /**
     * This makes sure the tick only runs on the client and only the top part of the block, which in this case is what we need
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (state.getValue(TRIPLE_TALL_BLOCK) == TripleTallBlock.TOP) ? validateTicker(type, ModBlockEntityTypes.GRANDFATHER_CLOCK_BLOCK_ENTITY, GrandfatherClockBlockEntity::tick) : null;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        TripleTallBlock currentPart = state.getValue(TRIPLE_TALL_BLOCK); // Get the part of the block (TOP, MIDDLE, or BOTTOM)
        if (direction.getAxis() != Direction.Axis.Y) { // Check if the direction is along the Y-axis (up or down)
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
        switch (currentPart) { // Handle the logic based on which part of the block this is
            case TOP:
                if (direction == Direction.DOWN) { // Ensure the middle part is below and the block is placeable
                    BlockState belowState = world.getBlockState(pos.below());
                    return (!belowState.is(this) || belowState.getValue(TRIPLE_TALL_BLOCK) != TripleTallBlock.MIDDLE) ?
                            Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos); // Break the block if the middle part is missing
                }
                break;
            case MIDDLE:
                if (direction == Direction.UP) { // Ensure the top part is above and the bottom part is below
                    BlockState aboveState = world.getBlockState(pos.above());
                    return (!aboveState.is(this) || aboveState.getValue(TRIPLE_TALL_BLOCK) != TripleTallBlock.TOP) ?
                    Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos); // Break the block if the middle part is missing
                } else if (direction == Direction.DOWN) {
                    BlockState belowState = world.getBlockState(pos.below());
                    return  (!belowState.is(this) || belowState.getValue(TRIPLE_TALL_BLOCK) != TripleTallBlock.BOTTOM) ?
                    Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos); // Break the block if the middle part is missing
                }
                break;
            case BOTTOM:
                if (direction == Direction.UP) { // Ensure the middle part is above
                    BlockState aboveState = world.getBlockState(pos.above());
                    if (!aboveState.is(this) || aboveState.getValue(TRIPLE_TALL_BLOCK) != TripleTallBlock.MIDDLE) {
                        world.destroyBlock(pos, true);
                        return Blocks.AIR.defaultBlockState();  // Break the block if the middle part is missing
                    }
                }
                break;
            default:
                break;
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        Level world = ctx.getLevel();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getBlockPos());
        boolean water = fluidState.getFluid() == Fluids.WATER;
        return blockPos.getY() < world.getTopY() - 2 && world.getBlockState(blockPos.above()).canBeReplaced(ctx) && world.getBlockState(blockPos.above(2)).canBeReplaced(ctx) ? super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, water)
                .setValue(ROTATION, RotationSegment.convertToSegment(ctx.getPlayerYaw()))
                .setValue(TRIPLE_TALL_BLOCK, TripleTallBlock.BOTTOM) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlock(pos.above(), withWaterloggedState(world, pos.above(), this.defaultBlockState()
                .setValue(TRIPLE_TALL_BLOCK, TripleTallBlock.MIDDLE)
                .setValue(ROTATION, state.getValue(ROTATION))), Block.UPDATE_ALL);
        world.setBlock(pos.above(2), withWaterloggedState(world, pos.above(2), this.defaultBlockState()
                .setValue(TRIPLE_TALL_BLOCK, TripleTallBlock.TOP)
                .setValue(ROTATION, state.getValue(ROTATION))), Block.UPDATE_ALL);
    }

    public static BlockState withWaterloggedState(LevelReader world, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, world.isWater(pos)) : state;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                dropStacks(state, world, pos, null, player, player.getMainHandItem());
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    /**
     * Destroys a bottom half of a tall double block (such as a plant or a door)
     * without dropping an item when broken in creative.
     *
     * @see Block#playerWillDestroy(Level, BlockPos, BlockState, Player)
     */
    protected static void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
        TripleTallBlock tripleTallBlock = state.getValue(TRIPLE_TALL_BLOCK);
        BlockPos blockPosBelow = pos.below();
        BlockState blockStateBelow = world.getBlockState(blockPosBelow);
        BlockPos blockPosFarBelow = pos.below(2);
        BlockState blockStateFarBelow = world.getBlockState(blockPosFarBelow);
        if (tripleTallBlock == TripleTallBlock.TOP) {
            if (blockStateBelow.is(state.getBlock()) && blockStateBelow.getValue(TRIPLE_TALL_BLOCK) == TripleTallBlock.BOTTOM) {
                BlockState blockState2 = blockStateBelow.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                world.setBlock(blockPosBelow, blockState2, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                world.levelEvent(player, LevelEvent.BLOCK_BROKEN, blockPosBelow, Block.getId(blockStateBelow));
            }
            if (blockStateFarBelow.is(state.getBlock()) && blockStateFarBelow.getValue(TRIPLE_TALL_BLOCK) == TripleTallBlock.BOTTOM) {
                BlockState blockState3 = blockStateFarBelow.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                world.setBlock(blockPosFarBelow, blockState3, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                world.levelEvent(player, LevelEvent.BLOCK_BROKEN, blockPosFarBelow, Block.getId(blockStateFarBelow));
            }
        } else if (tripleTallBlock == TripleTallBlock.MIDDLE) {
            if (blockStateBelow.is(state.getBlock()) && blockStateBelow.getValue(TRIPLE_TALL_BLOCK) == TripleTallBlock.BOTTOM) {
                BlockState blockState2 = blockStateBelow.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                world.setBlock(blockPosBelow, blockState2, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                world.levelEvent(player, LevelEvent.BLOCK_BROKEN, blockPosBelow, Block.getId(blockStateBelow));
            }
        }
    }

    @Override
    public void afterBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, tool);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide && player instanceof ServerPlayer) {
            long time = world.getDayTime() % 24000; // Get the in-game time (0-23999)
            String formattedTime = formatInGameTime(time); // Convert to readable format
            String symbol = (time >= 0 && time < 12300) || (time > 23850) ? "§6☀§f " : "§9☽§f "; // Night: 0-12300, 23850-24000; Day: 12300-23850
            player.displayClientMessage(Component.literal(symbol + formattedTime), true);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Formats the in-game time to a readable format (e.g., HH:MM AM/PM).
     */
    private String formatInGameTime(long time) {
        int hours = (int) (time / 1000 + 6) % 24; // Each 1000 ticks = 1 hour, offset by 6 for MC time
        int minutes = (int) (time % 1000 * 60 / 1000); // Convert remainder ticks to minutes
        String period = hours >= 12 ? "PM" : "AM"; // Determine AM or PM
        hours = hours % 12; // Convert to 12-hour format
        if (hours == 0) hours = 12; // Adjust 0 to 12 for 12-hour clock
        return String.format("%02d:%02d %s", hours, minutes, period);
    }

    public enum Type implements GrandfatherClockType {
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

    public GrandfatherClockType getGrandfatherClockType() {
        return this.type;
    }

    public interface GrandfatherClockType extends StringRepresentable {
        Map<String, GrandfatherClockType> TYPES = new Object2ObjectArrayMap<>();
        Codec<GrandfatherClockType> CODEC = Codec.stringResolver(StringRepresentable::asString, TYPES::get);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.tells_time")));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, Integer.valueOf(rotation.rotate((Integer)state.getValue(ROTATION), MAX_ROTATIONS)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, Integer.valueOf(mirror.mirror((Integer)state.getValue(ROTATION), MAX_ROTATIONS)));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
