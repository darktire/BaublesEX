package baubles.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiButtonEx extends GuiButton {
    public GuiButtonEx(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    protected void drawIcon(Minecraft mc, ItemStack itemStack, int x, int y, boolean thisTab) {
        RenderItem itemRender = mc.getRenderItem();
        itemRender.zLevel = 100.0F;
        RenderHelper.enableGUIStandardItemLighting();
        itemRender.renderItemAndEffectIntoGUI(itemStack, x + 6, y + 7 + (thisTab ? 0 : 2));
        itemRender.renderItemOverlays(mc.fontRenderer, itemStack, x + 6, y + 7 + (thisTab ? 0 : 2));
        RenderHelper.disableStandardItemLighting();
        itemRender.zLevel = 0.0F;
    }

    protected void drawHoveringText(Minecraft mc, String label, int x, int y) {
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
