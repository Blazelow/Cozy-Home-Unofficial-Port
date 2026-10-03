package net.luckystudio.cozyhome.block.custom.drawers;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import net.luckystudio.cozyhome.CozyHome;
public class DrawerScreen extends AbstractContainerScreen<DrawerScreenHandler> {
    private static final Identifier TEXTURE = CozyHome.id("textures/gui/container/drawer.png");

    public DrawerScreen(DrawerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void init() {
        super.init();
        // Move the title 36 pixels down
        titleLabelX = 8; // Left alignment, same as "Container"
        titleLabelY = 6 + 36; // Default titleY is 6, so add 36 to move it down
    }
}