package baubles.client.gui.botton;

import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiBaublesController extends GuiButtonBase {

    protected final GuiContainer parentGui;
    private final boolean direction;
    private int ticks;

    /**
     * Add PgDn and PgUp to Bauble slots.
     * @param direction False means PgDn, True means PgUp.
     */
    public GuiBaublesController(int id, GuiContainer parentGui, int x, int y, boolean direction) {
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
            mc.getTextureManager().bindTexture(GuiPlayerExpanded.background);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 200);

            if (ticks > 0) {
                this.drawTexturedModalRect(this.x, this.y, 0, 14 + (direction ? 28 : 0) + 166, 28, 14);
                ticks--;
            }
            else {
                this.drawTexturedModalRect(this.x, this.y, 0, (direction ? 28 : 0) + 166, 28, 14);
            }

            GlStateManager.popMatrix();

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}