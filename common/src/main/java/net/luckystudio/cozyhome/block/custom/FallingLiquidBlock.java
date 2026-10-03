package net.luckystudio.cozyhome.block.custom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.fountains.FountainSpoutBlock;
import net.luckystudio.cozyhome.block.util.ModBlockUtilities;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
public class FallingLiquidBlock extends Block {
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;
    public static final BooleanProperty HAS_UNDER = ModProperties.HAS_UNDER;
    public static final VoxelShape BASE = Shapes.or(Block.box(0, 0, 0, 0, 0, 0));
    public FallingLiquidBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CONTAINS, ContainsBlock.NONE)
                .setValue(HAS_UNDER, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BASE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS, HAS_UNDER);
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.setBlock(pos, state
                .setValue(CONTAINS, determineContains(world, pos))
                .setValue(HAS_UNDER, hasUnder(world, pos)), Block.UPDATE_ALL);
        world.scheduleTick(pos, this, 10);
        super.onPlace(state, world, pos, oldState, notify);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        world.scheduleTick(pos, this, 10);
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(state, world, pos, random);
        if (!canStay(state, world, pos)) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else if (!ModBlockUtilities.isEntityObstructing(world, pos) && ModBlockUtilities.canPlaceBelow(world, pos)) {
            world.setBlock(pos.below(), ModBlocks.FALLING_LIQUID.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return state.setValue(CONTAINS, determineContains((Level) world, pos))
                .setValue(HAS_UNDER, hasUnder(world, pos));
    }

    private static boolean canStay(BlockState state, Level world, BlockPos pos) {
        BlockPos posAbove = pos.above();
        BlockState blockStateAbove = world.getBlockState(posAbove);
        return (blockStateAbove.getBlock() instanceof FountainSpoutBlock || blockStateAbove.getBlock() instanceof FallingLiquidBlock) && blockStateAbove.getValue(CONTAINS) == state.getValue(CONTAINS);
    }

    private ContainsBlock determineContains(Level world, BlockPos pos) {
        BlockPos posAbove = pos.above();
        if (world.getBlockState(posAbove).hasProperty(CONTAINS)) {
            ContainsBlock containsAbove = world.getBlockState(posAbove).getValue(CONTAINS);
            if (containsAbove == ContainsBlock.LAVA) return ContainsBlock.LAVA;
            if (containsAbove == ContainsBlock.WATER) return ContainsBlock.WATER;
        }
        return ContainsBlock.NONE;
    }

    private boolean hasUnder(LevelAccessor world, BlockPos pos) {
        BlockPos posBelow = pos.below();
        BlockState blockStateBelow = world.getBlockState(posBelow);
        return blockStateBelow.isFaceSturdy(world, pos, Direction.UP);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        // Check if the block can see the sky and spawn smoke particles if block contains lava
        if (state.getValue(HAS_UNDER)) {
            if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
                world.addParticle(ParticleTypes.SMOKE,
                        false,
                        pos.getX() + 0.5,
                        pos.getY() + 0.1,
                        pos.getZ() + 0.5,
                        0,
                        0,
                        0);
                return;
            }
            if (state.getValue(CONTAINS) == ContainsBlock.WATER) {
                world.addParticle(ParticleTypes.CLOUD,
                        false,
                        pos.getX() + 0.5,
                        pos.getY() + 0.1,
                        pos.getZ() + 0.5,
                        0,
                        0,
                        0);
                world.addParticle(ParticleTypes.SPLASH,
                        false,
                        pos.getX() + 0.5,
                        pos.getY() + 0.1,
                        pos.getZ() + 0.5,
                        0,
                        0,
                        0);
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersects) {
        float aboveEntity = ((float) entity.getY()) + entity.getBbHeight();
        if (!(entity instanceof LivingEntity) || entity.getInBlockState().is(this)) {
            if (state.getValue(CONTAINS) == ContainsBlock.LAVA) {
                entity.hurt(world.damageSources().lava(), 4.0F);
                entity.igniteForSeconds(3);
                world.addParticle(ParticleTypes.SMOKE,
                        false,
                        pos.getX() + 0.5,
                        aboveEntity,
                        pos.getZ() + 0.5,
                        0,
                        0,
                        0);
            }
            if (state.getValue(CONTAINS) == ContainsBlock.WATER) {
                if (entity.isOnFire()) entity.extinguishFire();
                world.addParticle(ParticleTypes.SPLASH,
                        false,
                        pos.getX() + 0.5,
                        aboveEntity,
                        pos.getZ() + 0.5,
                        0,
                        0,
                        0);
            }
        }
        super.entityInside(state, world, pos, entity, effectApplier, intersects);
    }
}
