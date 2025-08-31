package baubles.client.gui.element;

import baubles.client.gui.GuiBase;
import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementSwitcher extends ElementBase {
    private boolean on = true;
    public final int startY;

    public ElementSwitcher(int buttonId, GuiPlayerExpanded parentGui, int x, int y) {
        super(buttonId, x, y, 5, 5, "", parentGui);
        this.startY = y;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean pressed = super.mousePressed(mc, mouseX, mouseY);
            if (pressed) this.on = !this.on;
            return pressed;
        }
        return false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.visible = this.parentGui.getGuiTop() < this.y && this.y < this.parentGui.getGuiTop() + 144;
        if (this.visible) {
            updateHovered(mouseX, mouseY);

            glPush();
            mc.getTextureManager().bindTexture(GuiBase.BAUBLES_TEX);
            drawTexturedModalRect(this.x, this.y, this.on ? 6 : 14, 209, 5, 5);
            glPop();
        }
    }
}
