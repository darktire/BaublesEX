package baubles.client.gui;

import baubles.api.cap.BaublesItemHandler;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.BaubleContent;
import baubles.common.Baubles;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketIncrOffset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collections;

public class GuiPlayerExpanded extends InventoryEffectRendererEx {

    public static final ResourceLocation background = new ResourceLocation(Baubles.MODID, "textures/gui/baubles_container.png");
    private final IBaublesItemHandler baubles = ((ContainerPlayerExpanded) this.inventorySlots).baubles;
    private float oldMouseX, oldMouseY;

    public GuiPlayerExpanded(EntityPlayer player) {
        super(new ContainerPlayerExpanded(player.inventory, !player.getEntityWorld().isRemote, player));
        this.allowUserInput = true;
    }

    private void resetGuiLeft() {
        this.guiLeft = (this.width - this.xSize) / 2;
    }


    public void updateScreen() {
        ((ContainerPlayerExpanded) inventorySlots).baubles.setEventBlock(false);
        updateActivePotionEffects();
        resetGuiLeft();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.buttonList.add(new GuiBaublesFlip(56, this, guiLeft - 27, guiTop, false));
        this.buttonList.add(new GuiBaublesFlip(57, this, guiLeft - 27, guiTop + 14 + getMaxY(), true));
        resetGuiLeft();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        int xLoc = this.guiLeft - 22;
        if (mouseX > xLoc && mouseX < xLoc + 18) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + getMaxY()) {
                int index = (mouseY - yLoc) / 18;
                BaublesItemHandler container = ((BaublesItemHandler) baubles);

                ItemStack stack = container.getStackInSlot(index);
                if (!stack.isEmpty()) return;

                FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 200);
                String str = I18n.format("name." + BaubleContent.getSlots().get(container.setSlot(index)).toUpperCase());

                GuiUtils.drawHoveringText(Collections.singletonList(str), mouseX - this.guiLeft, mouseY - this.guiTop + 7, width, height, 300, renderer);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        int xLoc = this.guiLeft - 22;
        if (mouseX > xLoc && mouseX < xLoc + 18) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + getMaxY()) {
                int dWheel = Mouse.getEventDWheel();
                if (dWheel != 0) {
                    int value = -(dWheel / 120);
                    PacketHandler.INSTANCE.sendToServer(new PacketIncrOffset(value));
                    ((BaublesItemHandler) baubles).incrOffset(value);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

        int k = this.guiLeft;
        int l = this.guiTop;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int size = Math.min(7, baubles.getSlots());

        // draw slots
        for (int i = 0; i < size; i++) {
            this.mc.getTextureManager().bindTexture(background);
            this.drawTexturedModalRect(k - 27, l + 14 + (i * 18), 0, 227, 28, 18);
        }

        GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.mc.player);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
//            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.player.getStatFileWriter()));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
        }
    }

    @Override
    protected void drawActivePotionEffects() {
        super.guiLeft -= 27;
        super.drawActivePotionEffects();
        super.guiLeft += 27;
    }

    private int getMaxY() {
        return 18 * Math.min(baubles.getSlots(), 7);
    }
}