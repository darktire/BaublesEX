package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;

public class GUIBaublesScroller extends ElementBase {
    public GUIBaublesScroller(int id, int x, int y) {
        super(id, x, y, 27, 14, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHovered(mouseX, mouseY);

            mc.getTextureManager().bindTexture(GuiBaublesBase.BAUBLES_TEX);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 200);

            drawTexture(this.x, this.y, zLevel, 0, 166, 28, 14);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {}
}
