package net.luckystudio.cozyhome.entity.custom;

import net.minecraft.world.level.block.Block;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.bathtub.BathTubBlock;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlockEntity;
import net.luckystudio.cozyhome.block.util.interfaces.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
public class SeatEntity extends Entity {

    public SeatEntity(EntityType<? extends Entity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    protected void initDataTracker(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(CompoundTag nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(CompoundTag nbt) {

    }

    // Can player ride entity
    @Override
    protected boolean canStartRiding(Entity entity) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.getLevel();
        Entity entity = this.getFirstPassenger();
        // Delete the entity if no player is riding it
        if (entity instanceof LivingEntity livingEntity) {
            if (world.getBlockState(getBlockPos()).getBlock() instanceof BathTubBlock && world.getBlockState(getBlockPos().below()).getBlock() == Blocks.MAGMA_BLOCK) {
                livingEntity.addStatusEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0)); // 3 seconds (60 ticks), level 1
            }
            // Aiming the telescope
            if (isOffsettingBlock()) {
                if (this.getLevel().getBlockEntity(this.getBlockPos()) instanceof TelescopeBlockEntity telescopeBlockEntity) {
                    BlockState telescopeBlockState = this.getLevel().getBlockState(this.getBlockPos());
                    telescopeBlockEntity.setYRot(livingEntity.getYRot() + 90);
                    telescopeBlockEntity.setXRot(-livingEntity.getXRot());
                    TelescopeBlock.isFacingMoon(world, telescopeBlockState, getBlockPos(), livingEntity.getYRot(), -livingEntity.getXRot());
                    telescopeBlockEntity.setChanged();
                }
            }
        }
    }

    // Runs when
    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getLevel().getBlockState(pos);
        if (state.getBlock() instanceof SeatBlock) {
            passenger.setYRot(this.getYRot());
            super.addPassenger(passenger);
        }
    }
/**
     This method makes sure the dismount location is valid.
     This is the same as the pig class, maybe try and access it instead?
     Also, this method handles the despawning of the entity when the player dismounts
 */
    @Override
    public Vec3 updatePassengerForDismount(LivingEntity passenger) {
        this.remove(RemovalReason.DISCARDED);
        return super.updatePassengerForDismount(passenger);
    }

    @Override
    public void remove(RemovalReason reason) {
        Level world = this.getLevel();
        BlockPos pos = this.getBlockPos();
        if (world.getBlockState(pos).getBlock() instanceof SeatBlock) {
            BlockState state = world.getBlockState(pos);
            world.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, false), Block.UPDATE_ALL);
        }
        super.remove(reason);
    }

    @Override
    protected Vec3 getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        Vec3 attachmentPoint = super.getPassengerAttachmentPoint(passenger, dimensions, scaleFactor);
        if (passenger instanceof Player) {
            float yaw = passenger.getYRot();
            if (Float.isNaN(yaw)) yaw = 0.0F;

            float riderYaw = yaw + 180.0F;
            double radians = Math.toRadians(riderYaw);

            double offsetX = isOffsettingBlock() ? -Math.sin(radians) : 0.0;
            double offsetZ = isOffsettingBlock() ? Math.cos(radians) : 0.0;

            double yOffset = getHeightOffset();

            // Apply the offset
            return attachmentPoint.add(offsetX, yOffset - 1, offsetZ);
        }
        return super.getPassengerAttachmentPoint(passenger, dimensions, scaleFactor);
    }

    private float getHeightOffset() {
        BlockPos pos = this.getBlockPos();
        BlockState state = getWorld().getBlockState(pos);
        if (state.getBlock() instanceof SeatBlock seatBlock) {
            return seatBlock.getSeatHeight(state);
        }
        return 0f;
    }

    // This allows the seat entity to get moved by a piston
    @Override
    protected Vec3 adjustMovementForPiston(Vec3 movement) {
        return super.adjustMovementForPiston(movement);
    }

    private boolean isOffsettingBlock() {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getLevel().getBlockState(pos);
        return state.getBlock() == ModBlocks.TELESCOPE;
    }
}
