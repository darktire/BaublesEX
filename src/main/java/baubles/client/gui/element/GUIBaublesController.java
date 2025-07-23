package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GUIBaublesController extends ElementBase {

    protected final GuiContainer parentGui;
    private final boolean direction;
    private int ticks;

    /**
     * Add PgDn and PgUp to Bauble slots.
     * @param direction False means PgDn, True means PgUp.
     */
    public GUIBaublesController(int id, GuiContainer parentGui, int x, int y, boolean direction) {
        super(id, x, y, 27, 14, "");
        this.parentGui = parentGui;
        this.direction = direction;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if (pressed) {
            int value = direction ? 1 : -1;
            ticks = 10;
            GuiPlayerExpanded gui = (GuiPlayerExpanded) parentGui;
            gui.moveBaubleSlots(value);
        }
        return pressed;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHovered(mouseX, mouseY);

            mc.getTextureManager().bindTexture(GuiBaublesBase.BAUBLES_TEX);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 200);

            if (ticks > 0) {
                drawTexture(this.x, this.y, zLevel, 0, 14 + (direction ? 28 : 0) + 166, 28, 14);
                ticks--;
            }
            else {
                drawTexture(this.x, this.y, zLevel, 0, (direction ? 28 : 0) + 166, 28, 14);
            }

            GlStateManager.popMatrix();
        }
    }
}