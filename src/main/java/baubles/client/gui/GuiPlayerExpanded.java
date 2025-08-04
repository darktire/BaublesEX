package baubles.client.gui;

import baubles.api.cap.BaublesContainer;
import baubles.client.gui.element.GUIBaublesController;
import baubles.client.gui.element.GUIBaublesScroller;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.container.SlotBaubleHandler;
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

public class GuiPlayerExpanded extends GuiBaublesBase {
    @Deprecated public static final ResourceLocation background = new ResourceLocation("baubles","textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    private final EntityPlayer player;
    public ContainerPlayerExpanded containerEx = (ContainerPlayerExpanded) this.inventorySlots;
    public BaublesContainer baubles = (BaublesContainer) (this.containerEx).baubles;//container in sever
    public int baublesAmount = this.baubles.getSlots();
    public int finalLine = Math.min(8, baublesAmount);//todo need updater
    public int offset = 0;
    private int preOffset = 0;
    public GUIBaublesScroller scroller;

    public GuiPlayerExpanded(EntityPlayer player) {
        super(new ContainerPlayerExpanded(player.inventory, !player.getEntityWorld().isRemote, player));
        this.player = player;
        this.allowUserInput = true;
    }

    public void modifyOffset(int value) {
        if (value == 0) return;
        int offset1 = this.offset + value;
        if (offset1 > 0) value = -this.offset;
        if (offset1 < this.finalLine - this.baublesAmount) value = this.finalLine - this.offset - this.baublesAmount;
        this.offset += value;
    }

    public boolean needMoveSlots() {
        return this.preOffset != this.offset;
    }

    public void moveSlots() {
        for (int i = 46; i < 46 + this.baublesAmount; i++) {
            SlotBaubleHandler baubleSlots = ((SlotBaubleHandler) this.inventorySlots.inventorySlots.get(i));
            baubleSlots.setYPos(18 * this.offset);
        }
        this.preOffset = this.offset;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        if (!this.baubles.guiUpdated) {
            this.containerEx.removeBaubleSlots();
            this.containerEx.addBaubleSlots(this.player);
            this.baublesAmount = this.baubles.getSlots();
            this.finalLine = Math.min(8, baublesAmount);
            this.moveSlots();
            this.scroller.setBarPos(this.offset);
            this.baubles.guiUpdated = true;
        }
//        baubles.setEventBlock(false);
        super.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.buttonList.add(new GUIBaublesController(56, this, this.guiLeft - 24, this.guiTop + 5, 0));
        this.buttonList.add(new GUIBaublesController(57, this, this.guiLeft - 14, this.guiTop + 5, 2));
        this.scroller = new GUIBaublesScroller(58, this, this.guiLeft - 48, this.guiTop);
        this.buttonList.add(this.scroller);
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        int xLoc = this.guiLeft - 22;
        if (mouseX > xLoc && mouseX < xLoc + 18) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * this.finalLine) {
                int index = (mouseY - yLoc) / 18 - this.offset;
                if (index >= this.baublesAmount) return;
                BaublesContainer container = this.baubles;
                ItemStack stack = container.getStackInSlot(index);
                if (!stack.isEmpty()) return;

                FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 200);
                String str = I18n.format("name." + this.baubles.getTypeInSlot(index).getTypeName());

                GuiUtils.drawHoveringText(Collections.singletonList(str), mouseX - this.guiLeft, mouseY - this.guiTop + 7, this.width, this.height, 300, renderer);
                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        int xLoc = this.guiLeft - 22;
        if (mouseX > xLoc && mouseX < xLoc + 18) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * finalLine) {
                int dWheel = Mouse.getEventDWheel();
                if (dWheel != 0) {
                    int value = dWheel / 120;
                    this.modifyOffset(value);
                    if (this.needMoveSlots()) this.moveSlots();
                    this.scroller.moveScrollerBar(value);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the GuiContainer.
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int k = this.guiLeft;
        int l = this.guiTop;
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // draw inventory
        this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        // draw baubles container
        this.mc.getTextureManager().bindTexture(BAUBLES_TEX);
        drawTexturedModalRect(k - 29, l, 18, 0, 28, 166);

        // draw slots
        for (int i = 0; i < this.finalLine; i++) {
            if (i >= this.baublesAmount + this.offset) break;
            drawTexturedModalRect(k - 24, l + 15 + (i * 18), 6, 167, 18, 18);
        }

        GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (k + 51) - mouseX, (l + 75 - 50) - mouseY, mc.player);
    }

    @Override
    protected void drawActivePotionEffects() {
        if (!this.scroller.visible) {
            this.guiLeft -= 27;
            super.drawActivePotionEffects();
            this.guiLeft += 27;
        }
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

    public void displayNormalInventory() {
        GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
        //todo uncheck
    }
}