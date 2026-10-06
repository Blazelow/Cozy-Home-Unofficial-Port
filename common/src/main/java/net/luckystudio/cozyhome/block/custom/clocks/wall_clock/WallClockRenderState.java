package net.luckystudio.cozyhome.block.custom.clocks.wall_clock;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

public class WallClockRenderState extends BlockEntityRenderState {
    /** Angles in radians. */
    public float hourHandAngle;
    public float minuteHandAngle;
    public float rotationDegrees;
    /** Quartz clocks have a block model body, only the hands are drawn by the renderer. */
    public boolean handsOnly;
    public Identifier texture;
}
