package net.luckystudio.cozyhome.block.custom.wall_mirrors;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
public class MirrorScreen extends Screen {
    private final Player player;
    private static boolean face;
    public MirrorScreen(Player player, boolean face) {
        super(Component.translatable("cozyhome."));
        this.player = player;
        this.face = face;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int heightOffset = face ? 0 : 125;
        // Get the center of the screen
        int x = this.width / 2; // Center horizontally
        int y = this.height / 2 + heightOffset; // Center vertically

        // Set the size of the player model (adjustable as needed)
        float percentage = face ? .4f : .8f; // Changing the percentage of the model based on whether its a face of full body mirror
        int size = (int) (Math.min(this.width, this.height) * percentage);  // 20% of the smaller dimension

        // Render the player model at the center of the screen
        renderPlayerModel(context, x, y, size, mouseX, mouseY, this.minecraft.player);
    }

    public void renderPlayerModel(GuiGraphics context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        // Calculate the rotation based on the mouse position relative to the center of the screen
        float centerX = this.width / 2.0F;
        float centerY = this.height / 2.0F;

        // Calculate the mouse's offset from the center
        float i = (float) Math.atan((centerX - mouseX) / 40.0F);
        float j = (float) Math.atan((centerY - mouseY) / 40.0F);

        // Apply rotation transformations
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(j * 20.0F * (float) (Math.PI / 180.0));
        quaternionf.mul(quaternionf2);

        // Store original body and head yaw and pitch to reset after rendering
        float k = entity.yBodyRot;
        float l = entity.getYRot();
        float m = entity.getXRot();
        float n = entity.yHeadRotO;
        float o = entity.yHeadRot;

        // Update entity rotation based on the mouse position
        entity.yBodyRot = 180.0F + i * 20.0F;
        entity.setYRot(180.0F + i * 40.0F);
        entity.setXRot(-j * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        // Apply scale based on the size and entity's scale factor
        float p = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getBbHeight() / 2.0F, 0.0F);
        float q = (float) size / p;

        // Render the entity at the specified screen position
        drawEntity(context, x, y, q, vector3f, quaternionf, quaternionf2, entity);

        // Reset entity rotation after rendering
        entity.yBodyRot = k;
        entity.setYRot(l);
        entity.setXRot(m);
        entity.yHeadRotO = n;
        entity.yHeadRot = o;
    }

    public static void drawEntity(
            GuiGraphics context, float x, float y, float size, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity
    ) {
        context.pose().pushPose();
        context.pose().translate((double) x, (double) y, 50.0);
        context.pose().scale(size, size, -size);
        context.pose().translate(vector3f.x, vector3f.y, vector3f.z);
        context.pose().mulPose(quaternionf);

        Lighting.setupForEntityInInventory();

        // Render the entity using the EntityRenderDispatcher
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            entityRenderDispatcher.overrideCameraOrientation(quaternionf2.conjugate(new Quaternionf()).rotateY((float) Math.PI));
        }

        entityRenderDispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.pose(), context.bufferSource(), 15728880));
        context.flush();
        entityRenderDispatcher.setRenderShadow(true);

        context.pose().popPose();
        Lighting.setupFor3DItems();
    }


    // So when the player presses any button the screen closes
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.onClose();
        return super.keyPressed(keyCode, scanCode, modifiers);
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
