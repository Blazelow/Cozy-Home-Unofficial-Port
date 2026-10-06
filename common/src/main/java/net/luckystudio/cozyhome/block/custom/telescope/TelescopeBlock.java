package net.luckystudio.cozyhome.block.custom.telescope;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.entity.custom.SeatEntity;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.interfaces.SeatBlock;
import net.luckystudio.cozyhome.entity.ModEntities;
import net.luckystudio.cozyhome.entity.custom.SeatEntity;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
public class TelescopeBlock extends BaseEntityBlock implements ItemTooltipProvider, SimpleWaterloggedBlock, SeatBlock {
    public static final MapCodec<TelescopeBlock> CODEC = simpleCodec(TelescopeBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 16, 11);

    private static final List<String> GENERAL_FACTS = Arrays.asList(
            "message.cozyhome.telescope.moon_fact_0",
            "message.cozyhome.telescope.moon_fact_1",
            "message.cozyhome.telescope.moon_fact_2"
    );

    private static final Supplier<List<String>> FULL_MOON_FACTS_SUPPLIER = () -> {
        List<String> facts = new ArrayList<>(Arrays.asList(
                "message.cozyhome.telescope.full_moon_facts_0",
                "message.cozyhome.telescope.full_moon_facts_1",
                "message.cozyhome.telescope.full_moon_facts_2",
                "message.cozyhome.telescope.full_moon_facts_3",
                "message.cozyhome.telescope.full_moon_facts_4"
        ));
        facts.add(randomText(GENERAL_FACTS)); // Add one random fact
        return facts;
    };

    // Access random facts dynamically
    private static List<String> getFullMoonFacts() {
        return FULL_MOON_FACTS_SUPPLIER.get();
    }

    private static final List<String> WANING_GIBBOUS_FACTS = Arrays.asList(
            "message.cozyhome.telescope.waning_gibbous_facts_0",
            GENERAL_FACTS.get(RandomSource.createThreadLocalInstance().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> LAST_QUARTER_FACTS = Arrays.asList(
            "message.cozyhome.telescope.last_quarter_facts_0",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> WANING_CRESCENT_FACTS = Arrays.asList(
            "message.cozyhome.telescope.waning_crescent_facts_0",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> NEW_MOON_FACTS = Arrays.asList(
            "message.cozyhome.telescope.new_moon_facts_0",
            "message.cozyhome.telescope.new_moon_facts_1",
            "message.cozyhome.telescope.new_moon_facts_2",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> WAXING_CRESCENT_FACTS = Arrays.asList(
            "message.cozyhome.telescope.waxing_crescent_facts_0",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> FIRST_QUARTER_FACTS = Arrays.asList(
            "message.cozyhome.telescope.first_quarter_facts_0",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static final List<String> WAXING_GIBBOUS_FACTS = Arrays.asList(
            "message.cozyhome.telescope.waxing_gibbous_facts_0",
            GENERAL_FACTS.get(RandomSource.create().nextInt(GENERAL_FACTS.size()))
    );

    private static String randomText(List<String> facts) {
        return facts.get(RandomSource.create().nextInt(facts.size()));
    }

    public TelescopeBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(TRIGGERED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TRIGGERED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TelescopeBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection())
                .setValue(WATERLOGGED, bl);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        boolean isDay = world.isBrightOutside();
        if (!player.isShiftKeyDown()) {
            SeatBlock.sitDown(state, world, pos, player);
            return InteractionResult.SUCCESS;
        } else {
            // The moon info is only shown while sitting at the telescope and aiming at the moon (see SeatEntity)
            player.sendOverlayMessage(Component.translatable(isDay ? "message.cozyhome.telescope.moon_not_up" : "message.cozyhome.telescope.find_moon"));
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    /** The moon phase symbol, name and a random fact, shown when the telescope is aimed at the moon. */
    public static Component getMoonMessage(Level world, BlockPos pos) {
        int phase = world.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos).index();
        return Component.literal(getMoonSymbol(phase))
                .append(Component.translatable(getMoonPhaseName(phase)))
                .append(": ")
                .append(Component.translatable(getMoonPhaseFact(phase)));
    }

    private static String getMoonSymbol(int phase) {
        return switch (phase) {
            case 0 -> "§9§l\uD83C\uDF15§r ";
            case 1 -> "§9§l\uD83C\uDF16§r ";
            case 2 -> "§9§l\uD83C\uDF17§r ";
            case 3 -> "§9§l\uD83C\uDF18§r ";
            case 4 -> "§9§l\uD83C\uDF11§r ";
            case 5 -> "§9§l\uD83C\uDF12§r ";
            case 6 -> "§9§l\uD83C\uDF13§r ";
            case 7 -> "§9§l\uD83C\uDF14§r ";
            default -> throw new IllegalStateException("Unexpected value: " + phase + " from " + CozyHome.MOD_ID);
        };
    }

    private static String getMoonPhaseFact(int phase) {
        return switch (phase) {
            case 0 -> randomText(getFullMoonFacts());
            case 1 -> randomText(WANING_GIBBOUS_FACTS);
            case 2 -> randomText(LAST_QUARTER_FACTS);
            case 3 -> randomText(WANING_CRESCENT_FACTS);
            case 4 -> randomText(NEW_MOON_FACTS);
            case 5 -> randomText(WAXING_CRESCENT_FACTS);
            case 6 -> randomText(FIRST_QUARTER_FACTS);
            case 7 -> randomText(WAXING_GIBBOUS_FACTS);
            default -> randomText(GENERAL_FACTS);
        };
    }

    private static String getMoonPhaseName(int phase) {
        return switch (phase) {
            case 0 -> "message.cozyhome.telescope.full_moon";
            case 1 -> "message.cozyhome.telescope.waning_gibbous";
            case 2 -> "message.cozyhome.telescope.last_quarter";
            case 3 -> "message.cozyhome.telescope.waning_crescent";
            case 4 -> "message.cozyhome.telescope.new_moon";
            case 5 -> "message.cozyhome.telescope.waxing_crescent";
            case 6 -> "message.cozyhome.telescope.first_quarter";
            case 7 -> "message.cozyhome.telescope.waxing_gibbous";
            default -> throw new IllegalStateException("Unexpected value: " + phase);
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("tooltip.cozyhome.interact_with_hand_at_night").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.lunar_tips")));
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
    public float getSeatRotation(BlockState state, Level world, BlockPos pos) {
        return ModProperties.setSeatRotationFromFacing(state) + 180;
    }

    @Override
    public float getSeatHeight(BlockState state) {
        return 0.5f;
    }

    /** True while the player sits at a telescope, which then works like a spyglass (day or night). */
    public static boolean isLookingThrough(Player player) {
        return isLookingThrough(player, null);
    }

    /** Same as above, but only for the telescope at the given position (null means any telescope). */
    public static boolean isLookingThrough(Player player, BlockPos telescopePos) {
        if (!(player.getVehicle() instanceof SeatEntity seat)) return false;
        Level level = seat.level();
        BlockPos pos = seat.blockPosition();
        if (telescopePos != null && !telescopePos.equals(pos)) return false;
        return level.getBlockState(pos).getBlock() instanceof TelescopeBlock;
    }

    /** How many degrees off the moon the telescope may point and still count as aimed at it (about what the scope shows). */
    private static final float AIM_TOLERANCE = 10.0F;

    /** True when the telescope points at the sun (which is up), within the same tolerance used for the moon. */
    public static boolean isFacingSun(Level world, float rawYaw, float pitchUp) {
        // The sun travels in the east-west plane: straight up at noon (6000), on the horizon around 0 and 12000
        double angle = (((world.getDefaultClockTime() % 24000L) - 6000L) / 24000.0) * 2.0 * Math.PI;
        double sunX = -Math.sin(angle);
        double sunY = Math.cos(angle);
        if (sunY < 0.0) return false;
        double yaw = Math.toRadians(rawYaw);
        double pitch = Math.toRadians(pitchUp);
        double lookX = -Math.sin(yaw) * Math.cos(pitch);
        double lookY = Math.sin(pitch);
        double lookZ = Math.cos(yaw) * Math.cos(pitch);
        double dot = lookX * sunX + lookY * sunY;
        return Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dot)))) <= AIM_TOLERANCE;
    }

    public static boolean isFacingMoon(Level world, BlockState state, BlockPos pos, float rawYaw, float pitch) {
        if (world.getBlockEntity(pos) instanceof TelescopeBlockEntity telescopeBlockEntity) {
            float yaw360 = (rawYaw % 360 + 360) % 360; // Now in range 0 to 360
            long timeOfDay = world.getDefaultClockTime();
            float moonYawNeeded = timeOfDay < 18000 ? 270 : 90; // Flips the yaw depending on the time of day, as when the moon is directionly 90 degrees, the direction flips
            float moonPitchBasedOnTime = getMoonPitchFromTime(timeOfDay);
            boolean isYawCorrect = moonPitchBasedOnTime >= 80 || (yaw360 >= moonYawNeeded - AIM_TOLERANCE && yaw360 <= moonYawNeeded + AIM_TOLERANCE); // Give the player a small threshold in the yaw to look at the moon
            boolean isPitchCorrect = pitch >= moonPitchBasedOnTime - AIM_TOLERANCE && pitch <= moonPitchBasedOnTime + AIM_TOLERANCE; // Give the player a small threshold in the pitch to look at the moon
            return isYawCorrect && isPitchCorrect;
        }
        return false;
    }

    public static float getMoonYawFromTime(long timeOfDay) {
        // Normalize time to range [0, 24000)
        timeOfDay = timeOfDay % 24000;

        // Calculate the moon's yaw based on the time of day
        float yaw = (timeOfDay / 24000f) * 360f; // 0° at sunrise, 180° at sunset

        return yaw;
    }

    public static float getMoonPitchFromTime(long timeOfDay) {
        // Normalize to [0, 23999]
        timeOfDay = timeOfDay % 24000;

        // Before moon rise or after moon set
        if (timeOfDay < 12775 || timeOfDay > 23225) {
            return Float.NaN; // Moon not visible
        }

        // Rising phase: 12775 → 18000 (pitch 0 → 90)
        if (timeOfDay <= 18000) {
            float t = (timeOfDay - 12775f) / (18000f - 12775f); // 0 → 1
            return t * 90f; // Linear interpolation
        }

        // Falling phase: 18000 → 23225 (pitch 90 → 0)
        else {
            float t = (timeOfDay - 18000f) / (23225f - 18000f); // 0 → 1
            return (1f - t) * 90f; // Linear interpolation
        }
    }
}
