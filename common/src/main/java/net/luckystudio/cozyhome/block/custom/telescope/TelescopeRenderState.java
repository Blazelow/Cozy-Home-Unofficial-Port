package net.luckystudio.cozyhome.block.custom.telescope;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.Rotation;

public class TelescopeRenderState extends BlockEntityRenderState {
    public float yaw;
    public float pitch;
    /** Rotation of the whole model around the Y axis, in degrees, from the facing of the block. */
    public float facingDegrees;
}
