package net.luckystudio.cozyhome.entity.custom;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import net.luckystudio.cozyhome.block.ModBlocks;
import net.luckystudio.cozyhome.block.custom.water_holding_blocks.bathtub.BathTubBlock;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlock;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeBlockEntity;
import net.luckystudio.cozyhome.block.util.interfaces.SeatBlock;
public class SeatEntity extends Entity {

    public SeatEntity(EntityType<? extends Entity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        return InteractionResult.PASS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    // Can player ride entity
    @Override
    protected boolean canRide(Entity entity) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        Entity entity = this.getFirstPassenger();
        // Delete the entity if no player is riding it
        if (entity instanceof LivingEntity livingEntity) {
            if (world.getBlockState(blockPosition()).getBlock() instanceof BathTubBlock && world.getBlockState(blockPosition().below()).getBlock() == Blocks.MAGMA_BLOCK) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0)); // 3 seconds (60 ticks), level 1
            }
            // Aiming the telescope
            if (isOffsettingBlock()) {
                if (this.level().getBlockEntity(this.blockPosition()) instanceof TelescopeBlockEntity telescopeBlockEntity) {
                    BlockState telescopeBlockState = this.level().getBlockState(this.blockPosition());
                    telescopeBlockEntity.setYaw(livingEntity.getYRot() + 90);
                    telescopeBlockEntity.setPitch(-livingEntity.getXRot());
                    TelescopeBlock.isFacingMoon(world, telescopeBlockState, blockPosition(), livingEntity.getYRot(), -livingEntity.getXRot());
                    telescopeBlockEntity.setChanged();
                }
            }
        }
    }

    // Runs when
    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = this.blockPosition();
        BlockState state = this.level().getBlockState(pos);
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
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        this.remove(RemovalReason.DISCARDED);
        return super.getDismountLocationForPassenger(passenger);
    }

    @Override
    public void remove(RemovalReason reason) {
        Level world = this.level();
        BlockPos pos = this.blockPosition();
        if (world.getBlockState(pos).getBlock() instanceof SeatBlock) {
            BlockState state = world.getBlockState(pos);
            world.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, false), Block.UPDATE_ALL);
        }
        super.remove(reason);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
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
        BlockPos pos = this.blockPosition();
        BlockState state = level().getBlockState(pos);
        if (state.getBlock() instanceof SeatBlock seatBlock) {
            return seatBlock.getSeatHeight(state);
        }
        return 0f;
    }

    // This allows the seat entity to get moved by a piston
    @Override
    protected Vec3 limitPistonMovement(Vec3 movement) {
        return super.limitPistonMovement(movement);
    }

    private boolean isOffsettingBlock() {
        BlockPos pos = this.blockPosition();
        BlockState state = this.level().getBlockState(pos);
        return state.getBlock() == ModBlocks.TELESCOPE;
    }
}
