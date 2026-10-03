package net.luckystudio.cozyhome.block.custom.fountains;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;
import net.luckystudio.cozyhome.item.custom.ItemTooltipProvider;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.util.ModBlockUtilities;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.util.ModSoundEvents;
import java.util.List;
public class FountainSpoutBlock extends FaceAttachedHorizontalDirectionalBlock implements ItemTooltipProvider  {
    public static final MapCodec<FountainSpoutBlock> CODEC = simpleCodec(FountainSpoutBlock::new);
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;
    public static final BooleanProperty HAS_UNDER = ModProperties.HAS_UNDER;

    protected static final VoxelShape NORTH_WALL_SHAPE = Block.box(5, 10, 10, 11, 16, 16);
    protected static final VoxelShape SOUTH_WALL_SHAPE = Block.box(5, 10, 0, 11, 16, 6);
    protected static final VoxelShape WEST_WALL_SHAPE = Block.box(10, 10, 5, 16, 16, 11);
    protected static final VoxelShape EAST_WALL_SHAPE = Block.box(0, 10, 5, 6, 16, 11);
    protected static final VoxelShape FLOOR_SHAPE = Block.box(5, 0, 5, 11, 6, 11);
    protected static final VoxelShape CEILING_SHAPE = Block.box(5, 10, 5, 11, 16, 11);

    public FountainSpoutBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_UNDER, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(CONTAINS, ContainsBlock.NONE)
                .setValue(FACE, AttachFace.WALL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS,FACE,FACING, HAS_UNDER);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACE)) {
            case WALL -> switch (state.getValue(FACING)) {
                case EAST -> EAST_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                default -> NORTH_WALL_SHAPE;
            };
            case CEILING -> CEILING_SHAPE;
            default -> FLOOR_SHAPE;
        };
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify); // Call super first
        world.setBlock(pos, state
                .setValue(CONTAINS, determineContains(state, world, pos))
                .setValue(HAS_UNDER, hasUnder(state, world, pos)), Block.UPDATE_ALL);
        world.scheduleTick(pos, this, 10); // Schedule the tick
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return canStay(world, pos, getConnectedDirection(state).getOpposite());
    }

    public static boolean canStay(LevelReader world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.relative(direction);
        return world.getBlockState(blockPos).getBlock() == Blocks.WATER_CAULDRON ||
                world.getBlockState(blockPos).getBlock() == Blocks.LAVA_CAULDRON ||
                world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON ||
                world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction.getOpposite()) ||
                world.getBlockState(blockPos).getBlock() instanceof FountainBlock;
    }

    private void elongate(Level world, BlockPos pos) {
        if (world.getBlockState(pos.below()).getBlock() == Blocks.AIR) {
            world.setBlock(pos.below(), ModBlocks.FALLING_LIQUID.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        tickAccess.scheduleTick(pos, this, 20);
        if (getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
        return state.setValue(CONTAINS, determineContains(state, world, pos)).setValue(HAS_UNDER, hasUnder(state, world, pos));
    }

    private ContainsBlock determineContains(BlockState state, LevelAccessor world, BlockPos pos) {
        BlockPos targetPos = pos.relative(getConnectedDirection(state).getOpposite());
        BlockState targetState = world.getBlockState(targetPos);
        BooleanProperty property = BlockStateProperties.WATERLOGGED;
        if (isFountainBlock(targetState)) {
            if (targetState.getValue(CONTAINS) == ContainsBlock.WATER) return ContainsBlock.WATER;
            if (targetState.getValue(CONTAINS) == ContainsBlock.LAVA) return ContainsBlock.LAVA;
        } else if (targetState.hasProperty(property)) {
            if (targetState.getValue(property)) return ContainsBlock.WATER;
        } else if (targetState.getBlock() == Blocks.WATER_CAULDRON) {
            return ContainsBlock.WATER;
        } else if (targetState.getBlock() == Blocks.LAVA_CAULDRON) {
            return ContainsBlock.LAVA;
        }
        return ContainsBlock.NONE;
    }

    private boolean isFountainBlock(BlockState targetState) {
        return targetState.getBlock() instanceof FountainBlock;
    }

    private boolean hasUnder(BlockState state, LevelAccessor world, BlockPos pos) {
        BlockPos posBelow = pos.below();
        BlockState blockStateBelow = world.getBlockState(posBelow);
        return state.getValue(FACE) != AttachFace.FLOOR && (blockStateBelow.isFaceSturdy(world, pos, Direction.UP) || blockStateBelow.getBlock() instanceof FountainBlock);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        ContainsBlock contents = state.getValue(CONTAINS);
        boolean isFloor = state.getValue(FACE) == AttachFace.FLOOR;
        boolean hasUnder = state.getValue(HAS_UNDER);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.1;
        double z = pos.getZ() + 0.5;

        if (isFloor) {
            switch (contents) {
                case LAVA -> world.addParticle(ParticleTypes.LAVA, false, x, y, z, 0, 0, 0);
                case WATER -> {
                    world.addParticle(ParticleTypes.CLOUD, false, x, y, z, 0, 0.1, 0);
                    world.addParticle(ParticleTypes.SPLASH, false, x, pos.getY() + 2.1, z, 0, 0.1, 0);
                }
            }
        } else if (hasUnder) {
            switch (contents) {
                case LAVA -> world.addParticle(ParticleTypes.SMOKE, false, x, y, z, 0, 0, 0);
                case WATER -> {
                    world.playSound(null, pos, ModSoundEvents.LIGHT_WATER_FLOW, SoundSource.AMBIENT, 0.1f, 1);
                    world.addParticle(ParticleTypes.CLOUD, false, x, y, z, 0, 0, 0);
                    world.addParticle(ParticleTypes.SPLASH, false, x, y, z, 0, 0, 0);
                }
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockPos posBehind = pos.relative(getConnectedDirection(state).getOpposite());
        BlockState stateBehind = world.getBlockState(posBehind);

        BlockPos posBelow = pos.below();
        BlockState stateBelow = world.getBlockState(posBelow);

        ContainsBlock contains = state.getValue(CONTAINS);

        // Attempt to transfer liquid
        if (contains != ContainsBlock.NONE && canPullLiquid(stateBehind) && canPourLiquidBelow(stateBelow)) {

            // Pour into block below
            if (stateBelow.getBlock() instanceof FountainBlock) {
                world.setBlock(posBelow, stateBelow.setValue(CONTAINS, contains), 3);
            } else if (stateBelow.is(Blocks.CAULDRON)) {
                BlockState filledCauldron = contains == ContainsBlock.WATER
                        ? Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 3)
                        : Blocks.LAVA_CAULDRON.defaultBlockState();
                world.setBlock(posBelow, filledCauldron, 3);
            }

            // Empty source behind
            if (stateBehind.getBlock() instanceof FountainBlock) {
                world.setBlock(posBehind, stateBehind.setValue(CONTAINS, ContainsBlock.NONE), 3);
            } else if (stateBehind.is(Blocks.WATER_CAULDRON) || stateBehind.is(Blocks.LAVA_CAULDRON)) {
                world.setBlock(posBehind, Blocks.CAULDRON.defaultBlockState(), 3);
            }

        } else if (!ModBlockUtilities.isEntityObstructing(world, pos)
                && ModBlockUtilities.canPlaceBelow(world, pos)
                && contains != ContainsBlock.NONE) {
            elongate(world, pos);
        }
    }

    private boolean canPullLiquid(BlockState state) {
        Block block = state.getBlock();
        return block instanceof FountainBlock
                || (block == Blocks.WATER_CAULDRON && state.getValue(BlockStateProperties.LEVEL_CAULDRON) == 3)
                || block == Blocks.LAVA_CAULDRON;
    }

    private boolean canPourLiquidBelow(BlockState state) {
        Block block = state.getBlock();
        return (block instanceof FountainBlock && state.getValue(CONTAINS) == ContainsBlock.NONE)
                || block == Blocks.CAULDRON;
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersects) {
        if (!(entity instanceof LivingEntity) || entity.getInBlockState().is(this)) {
            if (state.getValue(CONTAINS) == ContainsBlock.WATER) {
                if (entity.isOnFire()) entity.extinguishFire();
            }
            if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
                entity.hurt(world.damageSources().lava(), 4.0F);
                entity.igniteForSeconds(3);
            }
        }
        super.entityInside(state, world, pos, entity, effectApplier, intersects);
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable("tooltip.cozyhome.pours_liquid_from_liquid_holding_blocks_into_others").withStyle(ChatFormatting.GRAY));
    }
}

