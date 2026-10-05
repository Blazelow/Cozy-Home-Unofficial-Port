package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
public class WallClockBlock extends BaseEntityBlock implements SimpleWaterloggedBlock{
    public static final MapCodec<WallClockBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(ClockType.CODEC.fieldOf("kind").forGetter(WallClockBlock::getClockType), propertiesCodec())
                    .apply(instance, WallClockBlock::new));

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape NORTH_SHAPE = WallClockBlock.box(2, 2, 15, 14, 14, 16);
    private static final VoxelShape EAST_SHAPE = WallClockBlock.box(0, 2, 2, 1, 14, 14);
    private static final VoxelShape SOUTH_SHAPE = WallClockBlock.box(2, 2, 0, 14, 14, 1);
    private static final VoxelShape WEST_SHAPE = WallClockBlock.box(15, 2, 2, 16, 14, 14);

    private final ClockType type;

    @Override
    protected MapCodec<? extends WallClockBlock> codec() {
        return CODEC;
    }

    public WallClockBlock(ClockType grandfatherClockType, BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, Boolean.FALSE));
        this.type = grandfatherClockType;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    /**
     * This creates the solid looking hit-box for the entire block
     */
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> null;
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WallClockBlockEntity(pos, state);
    }

    /**
     * This makes sure the tick only runs on the client and only the top part of the block, which in this case is what we need
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, WallClockBlockEntity::tick);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        LevelReader worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }
    @Override
    public BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos
    ) {
        // Check if the block can remain in place
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        // Schedule fluid tick if waterlogged
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        // Return updated state
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
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

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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

    public enum Type implements ClockType {
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

    public ClockType getClockType() {
        return this.type;
    }

    public interface ClockType extends StringRepresentable {
        Map<String, ClockType> TYPES = new Object2ObjectArrayMap<>();
        Codec<ClockType> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
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
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_hand").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.tells_time")));
    }
}
