package net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

public class GrandfatherClockRenderState extends BlockEntityRenderState {
    /** Angles in radians. */
    public float hourHandAngle;
    public float minuteHandAngle;
    public float pendulumAngle;
    public boolean top;
    public float rotationDegrees;
    public Identifier texture;
}
