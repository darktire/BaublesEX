package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesListener;
import baubles.client.gui.element.ElementButton;
import baubles.client.gui.element.ElementController;
import baubles.client.gui.element.ElementScroller;
import baubles.client.gui.element.ElementSwitchers;
import baubles.common.config.Config;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.container.SlotBaubleHandler;
import baubles.compat.jei.JeiPlugin;
import baubles.util.HookHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPlayerExpanded extends GuiBase implements IBaublesListener<GuiPlayerExpanded> {
    @Deprecated public static final ResourceLocation background = new ResourceLocation(BaublesApi.MOD_ID,"textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    private final EntityPlayer player;
    private final EntityLivingBase entity;
    private final static boolean jeiLoaded = HookHelper.isModLoaded("jei");
    private final ContainerPlayerExpanded containerEx = (ContainerPlayerExpanded) this.inventorySlots;
    public IBaublesItemHandler baubles = (this.containerEx).baubles;
    public int baublesAmount = (this.containerEx).baublesAmount;
    public boolean wider = Config.Gui.widerBar;
    private int column = this.wider ? Config.Gui.column : 1;
    private int row = this.initRow();
    private int offset = 0;
    private ElementScroller scroller;
    private ElementSwitchers switchers;
    private final List<Rectangle> extraArea = new ArrayList<>();

    public GuiPlayerExpanded(EntityPlayer player) {
        this(player, player);
    }

    public GuiPlayerExpanded(EntityPlayer player, EntityLivingBase entity) {
        super(new ContainerPlayerExpanded(player, entity));
        this.player = player;
        this.entity = entity;
        this.allowUserInput = true;
    }

    private int initRow() {
        int i = this.baublesAmount / this.column;
        if (this.baublesAmount % this.column != 0) i += 1;
        return i;
    }

    public void moveSlots(int value) {
        int offset1 = this.offset + value;
        if (offset1 > 0 || this.row < 9) value = -this.offset;
        else if (offset1 < 8 - this.row) value = 8 - this.offset - this.row;
        this.offset += value;
        this.setSlotsPos();
    }

    private void setSlotsPos() {
        for (int i = 46; i < 46 + this.baublesAmount; i++) {
            SlotBaubleHandler baubleSlots = ((SlotBaubleHandler) this.inventorySlots.inventorySlots.get(i));
            baubleSlots.setYPos(18 * this.offset);
        }
        this.switchers.updateSwitchers();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void updateBaubles() {
        this.baublesAmount = this.baubles.getSlots();
        if (this.wider) {
            this.column = Config.Gui.column;
            this.containerEx.addWideBaubles();
        }
        else {
            this.column = 1;
            this.containerEx.addSlimBaubles();
        }
        this.row = initRow();
        this.scroller.setBarPos(this.offset);
        this.switchers.initSwitchers(true);
        this.setSlotsPos();
    }

    @Override
    public GuiPlayerExpanded startListening() {
        this.baubles.addListener(this);
        return this;
    }

    public void handleWider() {
        this.offset = 0;
        this.containerEx.clearBaubles();
        if (this.wider) {
            this.column = Config.Gui.column;
            this.containerEx.addWideBaubles();
        }
        else {
            this.column = 1;
            this.containerEx.addSlimBaubles();
        }
        this.row = initRow();
        this.extraArea.clear();
        int x = 18 * this.column;
        this.extraArea.add(new Rectangle(this.guiLeft - 11 - x, this.guiTop, 10 + x, 166));
        this.scroller.handleWider();
        this.switchers.initSwitchers(true);
    }

    public void handleHide() {
        this.scroller.switchVisible();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.addButton(new ElementButton(55, this, 64, 9, I18n.format("button.normal")));
        this.addButton(new ElementController.e(56, this, this.guiLeft - 24, this.guiTop + 5, 1));
        this.addButton(new ElementController.h(57, this, this.guiLeft - 14, this.guiTop + 5, 2));
        this.scroller = this.addButton(new ElementScroller(58, this, this.guiLeft - 30 - 18 * this.column, this.guiTop, Config.Gui.scrollerBar));
        this.switchers = this.addButton(new ElementSwitchers(59, this, this.guiLeft - 10, this.guiTop + 14));
        this.extraArea.add(new Rectangle(this.guiLeft - 11 - 18 * this.column, this.guiTop, 10 + 18 * this.column, 166));
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        int xLoc = this.guiLeft - 7;
        if (xLoc - 18 * this.column < mouseX && mouseX <= xLoc) {
            int yLoc = this.guiTop + 15;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * this.baublesAmount) {
                drawHoveringText(mouseX, mouseY, yLoc, xLoc);
            }
        }
    }

    private void drawHoveringText(int mouseX, int mouseY, int yLoc, int xLoc) {
        int indexY = (mouseY - yLoc) / 18 - this.offset;
        int index = indexY * this.column + (mouseX - xLoc) / 18 + this.column - 1;
        if (index >= this.baublesAmount) return;
        ItemStack stack = this.baubles.getStackInSlot(index);
        if (!stack.isEmpty()) return;

        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);
        String str = I18n.format("name." + this.baubles.getTypeInSlot(index).getName());

        GuiUtils.drawHoveringText(Collections.singletonList(str), mouseX - this.guiLeft, mouseY - this.guiTop + 7, this.width, this.height, 300, renderer);
        GlStateManager.popMatrix();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        //jei compat
        handleMouseInput(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    private void handleMouseInput(int mouseX, int mouseY) {
        int xLoc = this.guiLeft - 7;
        if (xLoc - 18 * this.column < mouseX && mouseX < xLoc) {
            int yLoc = this.guiTop + 15;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * 8) {
                int dWheel;
                if (jeiLoaded) dWheel = JeiPlugin.JEI_COMPAT.getIngredientUnderMouse(this, mouseX, mouseY);
                else dWheel = Mouse.getDWheel();
                int value = dWheel / 120;
                if (value != 0) {
                    this.moveSlots(value);
                    this.scroller.moveScrollerBar(value);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Draws the GuiContainer.
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = this.guiLeft;
        int top = this.guiTop;
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // draw inventory
        this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

        // draw baubles container
        this.mc.getTextureManager().bindTexture(BAUBLES_TEX);
        drawTexturedModalRect(left - 29, top, 18, 0, 28, 166);
        if (this.wider) {
            for (int i = 0; i < this.column; i++) {
                drawTexturedModalRect(left - 29 - i * 18, top, 18, 0, 24, 166);
            }
        }
        // draw slots
        if (this.wider) drawWideSlots(left, top);
        else drawSlimSlots(left, top);
        // draw player
        GuiInventory.drawEntityOnScreen(left + 51, top + 75, 30, (left + 51) - mouseX, (top + 75 - 50) - mouseY, this.player);
    }

    private void drawSlimSlots(int left, int top) {
        for (int i = 0; i < this.baublesAmount; i++) {
            if (i > 7) return;
            if (i >= this.baublesAmount + this.offset) break;
            drawTexturedModalRect(left - 24, top + 15 + (i * 18), 6, 167, 18, 18);
        }
    }

    private void drawWideSlots(int left, int top) {
        for (int i = 0; i < this.baublesAmount; i++) {
            if (i >= this.baublesAmount + this.offset * this.column) break;
            int j = i / this.column;
            if (j > 7) break;
            int k = i % this.column;
            drawTexturedModalRect(left - 24 + (k - this.column + 1) * 18, top + 15 + (j * 18), 6, 167, 18, 18);
        }
    }

//    @Override
//    protected void drawActivePotionEffects() {
//        if (!this.scroller.visible && !this.wider) {
//            this.guiLeft -= 27;
//            super.drawActivePotionEffects();
//            this.guiLeft += 27;
//        }
//    }

    @Override
    protected void actionPerformed(net.minecraft.client.gui.GuiButton button) {
        if (button.id == 0) {
//            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.player.getStatFileWriter()));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
        }
    }

    @Override
    protected boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        if (Config.Gui.visibleSwitchers && slotIn instanceof SlotBaubleHandler) {
            int pointX = mouseX - this.guiLeft;
            int pointY = mouseY - this.guiTop;
            int rectX = slotIn.xPos;
            int rectY = slotIn.yPos;
            boolean flag = pointX >= rectX - 1 && pointX < rectX + 17 && pointY >= rectY - 1 && pointY < rectY + 17;
            if (flag) {
                flag = !(pointX > rectX + 12 && pointY < rectY + 3);
            }
            return flag;
        }
        return super.isMouseOverSlot(slotIn, mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.baubles.removeListener(this);
    }

    public void displayNormalInventory() {
        GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
    }





    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int getOffset() {
        return this.offset;
    }

    public List<Rectangle> getExtraArea() {
        return this.extraArea;
    }
}