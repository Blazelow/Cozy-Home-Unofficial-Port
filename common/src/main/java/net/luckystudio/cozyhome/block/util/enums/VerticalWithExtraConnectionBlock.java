package net.luckystudio.cozyhome.block.util.enums;


import net.minecraft.util.StringRepresentable;
public enum VerticalWithExtraConnectionBlock implements StringRepresentable {
        SINGLE("single"),
        HEAD("head"),
        MIDDLE("middle"),
        EXTENDED("extended"),
        TAIL("tail");

        private final String name;

        private VerticalWithExtraConnectionBlock(final String name) {
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
