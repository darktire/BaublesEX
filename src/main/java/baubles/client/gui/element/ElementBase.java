package baubles.client.gui.element;

import baubles.client.gui.GuiExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ElementBase extends GuiButton {
    protected final GuiExpanded parentGui;

    public ElementBase(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, GuiExpanded parentGui) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.parentGui = parentGui;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && this.hovered;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {}

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}//only called by drawButton

    public void updateHovered(int mouseX, int mouseY) {
        this.hovered = getHovered(mouseX, mouseY, this.x, this.y, this.width, this.height);
    }

    public boolean getHovered(int mouseX, int mouseY, int left, int top, int width, int height) {
        return mouseX >= left && mouseY >= top && mouseX < left + width && mouseY < top + height;
    }

    public void glPush() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);
    }

    public void glPop() {
        GlStateManager.popMatrix();
    }

    public void drawTexture(int x, int y, float zLevel, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float pixelWidth = 0.00390625F;
        buffer.pos(x        , y + height, zLevel).tex( textureX          * pixelWidth, (textureY + height) * pixelWidth).endVertex();
        buffer.pos(x + width, y + height, zLevel).tex((textureX + width) * pixelWidth, (textureY + height) * pixelWidth).endVertex();
        buffer.pos(x + width, y         , zLevel).tex((textureX + width) * pixelWidth,  textureY           * pixelWidth).endVertex();
        buffer.pos(x        , y         , zLevel).tex( textureX          * pixelWidth,  textureY           * pixelWidth).endVertex();

        tessellator.draw();
    }

    public void setupBlend() {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public void drawIcon(Minecraft mc, ItemStack itemStack, int x, int y, boolean thisTab) {
        RenderItem itemRender = mc.getRenderItem();
        itemRender.zLevel = 100.0F;
        RenderHelper.enableGUIStandardItemLighting();
        itemRender.renderItemAndEffectIntoGUI(itemStack, x + 6, y + 7 + (thisTab ? 0 : 2));
        itemRender.renderItemOverlays(mc.fontRenderer, itemStack, x + 6, y + 7 + (thisTab ? 0 : 2));
        RenderHelper.disableStandardItemLighting();
        itemRender.zLevel = 0.0F;
    }

    public void drawHoveringText(Minecraft mc, String label, int x, int y) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
        int i = mc.fontRenderer.getStringWidth(label);

        int l1 = x + 12;
        int i2 = y - 12;
        int k = 8;

        if (l1 + i > mc.currentScreen.width)
        {
            l1 -= 28 + i;
        }

        if (i2 + k + 6 > mc.currentScreen.height)
        {
            i2 = mc.currentScreen.height - k - 6;
        }

        this.zLevel = 300.0F;
//        this.itemRender.zLevel = 300.0F;

        this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
        this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
        this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
        this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
        this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
        this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
        this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

        GlStateManager.disableDepth();
        mc.fontRenderer.drawStringWithShadow(label, (float)l1, (float)i2, -1);
        GlStateManager.enableDepth();

        this.zLevel = 0.0F;
//        this.itemRender.zLevel = 0.0F;

//        GlStateManager.enableLighting();
//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

    }
}