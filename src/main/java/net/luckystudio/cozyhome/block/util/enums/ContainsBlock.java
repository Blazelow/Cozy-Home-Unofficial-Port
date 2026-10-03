package net.luckystudio.cozyhome.block.util.enums;


import net.minecraft.util.StringRepresentable;
public enum ContainsBlock implements StringRepresentable {
    NONE("none"),
    WATER("water"),
    LAVA("lava");

    private final String name;

    ContainsBlock(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
