package net.luckystudio.cozyhome.block.util.enums;
import net.minecraft.util.StringRepresentable;

public enum TripleTallBlock implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String type;

    private TripleTallBlock(final String type) {
        this.type = type;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
