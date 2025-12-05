package baubles.client.gui.element;

import baubles.client.gui.GuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementController extends ElementBase {

    private boolean pressed;

    public ElementController(int id, GuiOverlay parentGui, int x, int y, int buttonID) {
        super(id, x, y, 8, 8, "", parentGui);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        this.pressed = super.mousePressed(mc, mouseX, mouseY);
        return this.pressed;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (this.pressed) this.pressed = false;
    }

    protected void drawButton(int controllerId, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHovered(mouseX, mouseY);
            glPush();
            mc.getTextureManager().bindTexture(GuiOverlay.BAUBLES_TEX);
            drawTexturedModalRect(this.x, this.y, 6 + (this.pressed ? 8 : 0), controllerId * 8 + 185, 8, 8);
            glPop();
        }
    }

    public static class f extends ElementController {
        public f(int id, GuiOverlay parentGui, int x, int y, int buttonID) {
            super(id, parentGui, x, y, buttonID);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            this.drawButton(0, mc, mouseX, mouseY, partialTicks);
        }
    }

    public static class e extends ElementController {
        public e(int id, GuiOverlay parentGui, int x, int y, int buttonID) {
            super(id, parentGui, x, y, buttonID);
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            super.mouseReleased(mouseX, mouseY);
            this.parentGui.wider = !this.parentGui.wider;
            this.parentGui.handleWider();
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            this.drawButton(1, mc, mouseX, mouseY, partialTicks);
        }
    }

    public static class h extends ElementController {
        public h(int id, GuiOverlay parentGui, int x, int y, int buttonID) {
            super(id, parentGui, x, y, buttonID);
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            super.mouseReleased(mouseX, mouseY);
            this.parentGui.handleHide();
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            this.drawButton(2, mc, mouseX, mouseY, partialTicks);
        }
    }
}