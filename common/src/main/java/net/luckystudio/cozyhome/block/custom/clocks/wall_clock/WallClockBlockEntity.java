package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.luckystudio.cozyhome.block.custom.clocks.ClockFunctionalityHandler;
import net.luckystudio.cozyhome.block.util.ModBlockEntityTypes;
import net.luckystudio.cozyhome.block.util.interfaces.ClockBlock;
import org.jetbrains.annotations.Nullable;
public class WallClockBlockEntity extends BlockEntity implements ClockBlock {
    public float lastHourHandAngle = 0.0f;
    public float currentHourHandAngle = 0.0f;
    public float lastMinuteHandAngle = 0.0f;
    public float currentMinuteHandAngle = 0.0f;

    private int ticks = 0;

    public WallClockBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.WALL_CLOCK_BLOCK_ENTITY, pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, WallClockBlockEntity blockEntity) {
        blockEntity.incrementTicks();
        ClockFunctionalityHandler.handleHandRotations(world, pos, state, blockEntity);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
    }

    // This Syncs the Client and Server
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

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public void incrementTicks() {
        ticks++;
    }

    @Override
    public float getCurrentHourHandAngle() {
        return currentHourHandAngle;
    }

    @Override
    public void setCurrentHourHandAngle(float angle) {
        currentHourHandAngle = angle;
    }

    @Override
    public float getLastHourHandAngle() {
        return lastHourHandAngle;
    }

    @Override
    public void setLastHourHandAngle(float angle) {
        lastHourHandAngle = angle;
    }

    @Override
    public float getCurrentMinuteHandAngle() {
        return currentMinuteHandAngle;
    }

    @Override
    public void setCurrentMinuteHandAngle(float angle) {
        currentMinuteHandAngle = angle;
    }

    @Override
    public float getLastMinuteHandAngle() {
        return lastMinuteHandAngle;
    }

    @Override
    public void setLastMinuteHandAngle(float angle) {
        lastMinuteHandAngle = angle;
    }

    @Override
    public float getCurrentPendulumAngle() {
        return 0;
    }

    @Override
    public void setCurrentPendulumAngle(float angle) {

    }

    @Override
    public float getLastPendulumAngle() {
        return 0;
    }

    @Override
    public void setLastPendulumAngle(float angle) {

    }

    @Override
    public Level getLevel() {
        return super.getLevel();
    }
}