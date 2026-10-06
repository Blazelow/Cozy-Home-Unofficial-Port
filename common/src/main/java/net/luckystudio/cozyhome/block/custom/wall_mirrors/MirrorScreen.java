package net.luckystudio.cozyhome.block.custom.wall_mirrors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class MirrorScreen extends Screen {
    private final Player player;
    private final boolean face;

    public MirrorScreen(Player player, boolean face) {
        super(Component.translatable("cozyhome."));
        this.player = player;
        this.face = face;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        int heightOffset = face ? 0 : 125;
        // Changing the size of the model based on whether it is a face or full body mirror
        float percentage = face ? .4f : .8f;
        int size = (int) (Math.min(this.width, this.height) * percentage);

        // The entity is drawn centered in this box and clipped to it, so the box grows downwards
        // instead of being moved: the model sits lower without its head getting cut off at the top
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, 0, 0, this.width, this.height + heightOffset * 2, size, 0.0625F, mouseX, mouseY, this.player);
    }

    // So when the player presses any button the screen closes
    @Override
    public boolean keyPressed(KeyEvent event) {
        this.onClose();
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().player.closeContainer();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
