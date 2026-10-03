package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

public class WallClockRenderState extends BlockEntityRenderState {
    /** Angles in radians. */
    public float hourHandAngle;
    public float minuteHandAngle;
    public float rotationDegrees;
    public Identifier texture;
}
