package baubles.client.gui.botton;

import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;
import baubles.common.network.PacketOpenBaublesTab;
import baubles.common.network.PacketOpenNormalInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import org.lwjgl.opengl.GL11;

public class GuiBaublesButton extends GuiButtonBase {

    private final GuiContainer parentGui;

    public GuiBaublesButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,  String buttonText) {
        super(buttonId, x, parentGui.getGuiTop() + y, 10, 10, buttonText);
        this.parentGui = parentGui;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (this.hovered && enabled) {
            if (parentGui instanceof GuiPlayerExpanded) {
                mc.displayGuiScreen(new GuiInventory(mc.player));
                PacketHandler.INSTANCE.sendToServer(new PacketOpenNormalInventory());
            } else if (parentGui instanceof GuiInventory) {
                PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory());
            } else if (parentGui instanceof GuiContainerCreative) {
                PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesTab());
            }
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            if (parentGui instanceof GuiContainerCreative && ((GuiContainerCreative) parentGui).getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex()) return;
            int x = this.x + this.parentGui.getGuiLeft();

            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiPlayerExpanded.background);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 200);

            if (this.hovered) {
                this.drawTexturedModalRect(x, this.y, 200, 48, 10, 10);
                this.drawCenteredString(fontrenderer, I18n.format(this.displayString), x + 5, this.y + this.height, 0xffffff);
            }
            else {
                this.drawTexturedModalRect(x, this.y, 210, 48, 10, 10);
            }

            GlStateManager.popMatrix();
        }
    }
}