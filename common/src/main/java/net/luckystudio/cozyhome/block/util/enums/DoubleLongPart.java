package net.luckystudio.cozyhome.block.util.enums;
import net.minecraft.util.StringRepresentable;

public enum DoubleLongPart implements StringRepresentable {
        FRONT("front"),
        BACK("back");

        private final String name;

        private DoubleLongPart(final String name) {
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
