package baubles.client.gui;

import baubles.Baubles;
import baubles.api.cap.BaublesContainer;
import baubles.client.gui.element.GUIBaublesButton;
import baubles.client.gui.element.GUIBaublesController;
import baubles.client.gui.element.GUIBaublesScroller;
import baubles.common.Config;
import baubles.common.compat.JeiPlugin;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.container.SlotBaubleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiPlayerExpanded extends GuiBaublesBase {
    @Deprecated public static final ResourceLocation background = new ResourceLocation(Baubles.MODID,"textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    private final EntityPlayer player;
    private final EntityLivingBase entity;
    private final boolean jeiLoaded;
    public ContainerPlayerExpanded containerEx = (ContainerPlayerExpanded) this.inventorySlots;
    public BaublesContainer baubles = (BaublesContainer) (this.containerEx).baubles;//container in sever
    public int baublesAmount = this.baubles.getSlots();
    public boolean wider = Config.Gui.widerBar;
    public int column = this.wider ? Config.Gui.column : 1;
    public int offset = 0;
    public boolean updated = true;
    private int preOffset = 0;
    public GUIBaublesScroller scroller;
    private final List<Rectangle> extraArea = new LinkedList<>();
    private final Map<Integer, Boolean> BAUBLE_RENDER = new HashMap<>();

    public GuiPlayerExpanded(EntityPlayer player) {
        this(player, player);
    }

    public GuiPlayerExpanded(EntityPlayer player, EntityLivingBase entity) {
        super(new ContainerPlayerExpanded(player, entity));
        this.player = player;
        this.entity = entity;
        this.allowUserInput = true;
        this.jeiLoaded = Loader.isModLoaded("jei");
    }

    public void modifyOffset(int value) {
        if (value == 0) return;
        int offset1 = this.offset + value;
        if (offset1 > 0) value = -this.offset;
        int allRow = this.baublesAmount / this.column;
        if (this.baublesAmount % this.column != 0) allRow += 1;
        if (allRow < 9) value = 0;
        else if (offset1 < 8 - allRow) value = 8 - this.offset - allRow;
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
        if (!this.baubles.guiUpdated || !this.updated) {
            this.baublesAmount = this.baubles.getSlots();
            this.containerEx.removeBauble();
            if (this.wider) {
                this.column = Config.Gui.column;
                this.containerEx.addWideBauble();
            }
            else {
                this.column = 1;
                this.containerEx.addSlimBauble();
            }
            this.moveSlots();
            if (!this.updated) {
                this.extraArea.clear();
                int xx = 18 * this.column;
                this.extraArea.add(new Rectangle(this.guiLeft - 11 - xx, this.guiTop, 10 + xx, 166));
                this.buttonList.remove(this.scroller);
                boolean v = this.scroller.visible;
                this.scroller = new GUIBaublesScroller(58, this, this.guiLeft - 30 - xx, this.guiTop, v);
                this.buttonList.add(this.scroller);
                this.updated = true;
            }
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
        if (this.entity instanceof EntityPlayer) this.buttonList.add(new GUIBaublesButton(55, this, 64, 9, I18n.format("button.normal")));
        this.buttonList.add(new GUIBaublesController(56, this, this.guiLeft - 24, this.guiTop + 5, 1));
        this.buttonList.add(new GUIBaublesController(57, this, this.guiLeft - 14, this.guiTop + 5, 2));
        this.scroller = new GUIBaublesScroller(58, this, this.guiLeft - 30 - 18 * this.column, this.guiTop, Config.Gui.scrollerBar);
        this.buttonList.add(this.scroller);
        this.extraArea.add(new Rectangle(this.guiLeft - 29, this.guiTop, 10 + 18 * this.column, 166));
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        int xLoc = this.guiLeft - 4;
        if (xLoc - 18 * this.column < mouseX && mouseX < xLoc) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * this.baublesAmount) {
                int indexY = (mouseY - yLoc) / 18 - this.offset;
                int index = indexY * this.column + (mouseX - xLoc) / 18 + this.column - 1;
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
        //jei compat
        handleMouse(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    private void handleMouse(int mouseX, int mouseY) {
        int xLoc = this.guiLeft - 4;
        if (xLoc - 18 * this.column < mouseX && mouseX < xLoc) {
            int yLoc = this.guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * 8) {
                int dWheel;
                if (this.jeiLoaded) dWheel = JeiPlugin.JEI_COMPAT.getIngredientUnderMouse(this, mouseX, mouseY);
                else dWheel = Mouse.getDWheel();
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
    }

    public List<Rectangle> getExtraArea() {
        return this.extraArea;
    }
}