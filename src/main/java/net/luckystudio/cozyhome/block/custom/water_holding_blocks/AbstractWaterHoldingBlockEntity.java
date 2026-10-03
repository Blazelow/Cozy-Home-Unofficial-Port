package net.luckystudio.cozyhome.block.custom.water_holding_blocks;

import net.minecraft.core.HolderLookup;

import net.luckystudio.cozyhome.block.custom.water_holding_blocks.sink.AbstractSinkBlock;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.block.util.interfaces.WaterHoldingBlock;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
public class AbstractWaterHoldingBlockEntity extends BlockEntity {

    public int timer;
    public int soupTime;

    public AbstractWaterHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.timer = 0;
        this.soupTime = 0;
    }

    public static void tick(Level world, BlockPos blockPos, BlockState state, AbstractWaterHoldingBlockEntity blockEntity) {

        // Handle soup time
        if (blockEntity.soupTime > 0) {
            if (state.getValue(ModProperties.CONTAINS) != ContainsBlock.WATER) blockEntity.soupTime = 0;
            blockEntity.soupTime--;
        }

        // If the block is not triggered, or if it's not a water holding block, return to stop the tick
        if (!state.getValue(BlockStateProperties.TRIGGERED) && !(state.getBlock() instanceof WaterHoldingBlock)) return;

        WaterHoldingBlock waterHoldingBlock = (WaterHoldingBlock) state.getBlock();

        Direction pullDirection = waterHoldingBlock.pullingDirection(state, world, blockPos);

        // Turn off the trigger if the block has no liquid to pull or the block is full
        if (pullDirection == null || waterHoldingBlock.isFull(state)) {
            world.setBlock(blockPos, state.setValue(BlockStateProperties.TRIGGERED, false), 3);
            return;
        }

        // Determine the liquid we should be pulling
        BlockPos pullPos = blockPos.offset(pullDirection);
        BlockState pullState = world.getBlockState(pullPos);
        ContainsBlock pullingLiquid;
        if (pullState.getFluidState().is(FluidTags.WATER) || pullState.hasProperty(BlockStateProperties.WATERLOGGED) && pullState.getValue(BlockStateProperties.WATERLOGGED) || pullState.getBlock() == Blocks.WATER_CAULDRON) {
            pullingLiquid = ContainsBlock.WATER;
        } else if (pullState.getFluidState().is(FluidTags.LAVA) || pullState.getBlock() == Blocks.LAVA_CAULDRON) {
            pullingLiquid = ContainsBlock.LAVA;
        }else {
            pullingLiquid = ContainsBlock.NONE;
        }

        // Choose the correct particles
        ParticleOptions dripParticle;
        ParticleOptions splashParticle;

        if (pullingLiquid == ContainsBlock.WATER) {
            dripParticle = ParticleTypes.FALLING_DRIPSTONE_WATER;
            splashParticle = ParticleTypes.SPLASH;
        } else {
            dripParticle = ParticleTypes.FALLING_LAVA;
            splashParticle = ParticleTypes.LANDING_LAVA;
        }

        // Particle positions
        double faucetHeight = state.getBlock() instanceof AbstractSinkBlock ? 0.6 : 0.4875; // Adjust this value to change the height of the faucet
        Vec3 centerTop = Vec3.atCenterOf(blockPos).add(0.0, faucetHeight, 0.0);
        Vec3 center = Vec3.atCenterOf(blockPos).add(blockPos.getX() + 0.5, blockPos.getY() + waterHoldingBlock.getLiquidLevelHeight(state), blockPos.getZ() + 0.5);

        // Spawn particles
        world.addParticle(dripParticle, centerTop.x, centerTop.y, centerTop.z, 0.0, 0.0, 0.0);
        world.addParticle(splashParticle, center.x, center.y, center.z, 0.0, 0.0, 0.0);

        // Update timer and level
        blockEntity.timer++;

        if (blockEntity.timer < 20) return;
        waterHoldingBlock.addLiquid(state, world, blockPos, pullState, pullDirection);
        blockEntity.timer = 0; // Reset timer
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putInt("timer", this.timer);
        nbt.putInt("soupTime", this.soupTime);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.timer = nbt.getInt("timer");
        this.soupTime = nbt.getInt("soupTime");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // This Syncs the Client and Server
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
