package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesListener;
import baubles.client.gui.element.ElementController;
import baubles.client.gui.element.ElementScroller;
import baubles.client.gui.element.ElementSwitchers;
import baubles.common.config.Config;
import baubles.common.container.ContainerExpansion;
import baubles.common.container.SlotBaubleHandler;
import baubles.common.network.PacketFakeTransaction;
import baubles.common.network.PacketHandler;
import baubles.compat.jei.IArea;
import baubles.compat.jei.JeiPlugin;
import baubles.util.HookHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
@MouseTweaksDisableWheelTweak
public class GuiOverlay extends GuiContainer implements IBaublesListener, IArea {
    public static final ResourceLocation BAUBLES_TEX = new ResourceLocation(BaublesApi.MOD_ID, "textures/gui/baubles_container.png");
    protected final static boolean jeiLoaded = HookHelper.isModLoaded("jei");
    protected final ContainerExpansion containerEx = (ContainerExpansion) this.inventorySlots;
    public IBaublesItemHandler baubles = (this.containerEx).baubles;
    public int baublesAmount = (this.containerEx).baublesAmount;
    public boolean wider = Config.Gui.widerBar;
    protected int col = this.initCol();
    protected int row = this.initRow();
    protected int offset = 0;
    protected ElementScroller scroller;
    protected ElementSwitchers switchers;
    protected final List<Rectangle> extraArea = new ArrayList<>();
    private List<Rectangle> occlusion;

    private boolean isOverlay = false;

    public static GuiOverlay create(EntityLivingBase entity) {
        return new GuiOverlay(entity).startListening();
    }

    public GuiOverlay(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    private GuiOverlay(EntityLivingBase entity) {
        super(ContainerExpansion.create(entity));
        this.allowUserInput = true;
        this.isOverlay = true;
    }

    public void initScreen(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.initElements();
    }

    public void setOcclusion(List<Slot> slots, int left, int top) {
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        boolean initialized = true;
        for (Slot slot : slots) {
            if (slot instanceof SlotBaubleHandler) {
                ((SlotBaubleHandler) slot).setLocked(true);
                if (initialized) {
                    minX = slot.xPos;
                    maxX = slot.xPos;
                    minY = slot.yPos;
                    maxY = slot.yPos;
                    initialized = false;
                }
                else {
                    minX = Math.min(minX, slot.xPos);
                    maxX = Math.max(maxX, slot.xPos);
                    minY = Math.min(minY, slot.yPos);
                    maxY = Math.max(maxY, slot.yPos);
                }
            }
        }
        left = minX + left - 1;
        top = minY + top - 1;
        int width = maxX - minX + 18;
        int height = maxY - minY;
        Rectangle o1 = new Rectangle(left, top, width, height);
        Rectangle o2 = new Rectangle(left, top + height, width - 18, 18);
        this.occlusion = ImmutableList.of(o1, o2);
    }

    public void drawOcclusion() {
        this.mc.getTextureManager().bindTexture(BAUBLES_TEX);
        this.occlusion.forEach(o ->
                drawScaledCustomSizeModalRect(o.x, o.y, 24, 167, 18, 18, o.width, o.height, 256, 256)
        );
    }

    public void drawAll(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        this.drawOcclusion();
        this.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.enableLighting();
    }

    public void moveSlots(int value) {
        int offset1 = this.offset + value;
        if (offset1 > 0 || this.row < 9) value = -this.offset;
        else if (offset1 < 8 - this.row) value = 8 - this.offset - this.row;
        this.offset += value;
        this.setSlotsPos();
    }

    private void setSlotsPos() {
        for (Slot slot : this.containerEx.getBaubleSlots()) {
            ((SlotBaubleHandler) slot).setYPos(18 * this.offset);
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
        this.col = initCol();
        this.containerEx.addBaubleSlots(this.wider);
        this.row = initRow();
        this.scroller.setBarPos(this.offset);
        this.switchers.initSwitchers(true);
        this.setSlotsPos();
    }

    @Override
    public GuiOverlay startListening() {
        this.baubles.addListener(this);
        return this;
    }

    public void handleWider() {
        this.offset = 0;
        this.containerEx.clearBaubles();
        this.col = initCol();
        this.containerEx.addBaubleSlots(this.wider);
        this.row = initRow();
        this.extraArea.clear();
        this.extraArea.add(new Rectangle(this.guiLeft - 11 - 18 * this.col, this.guiTop, 10 + 18 * this.col, 166));
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
        super.initGui();
        initElements();
    }

    protected void initElements() {
        this.buttonList.clear();
        this.extraArea.clear();
        this.addButton(new ElementController.e(56, this, this.guiLeft - 24, this.guiTop + 5, 1));
        this.addButton(new ElementController.h(57, this, this.guiLeft - 14, this.guiTop + 5, 2));
        this.scroller = this.addButton(new ElementScroller(58, this, this.guiLeft - 30 - 18 * this.col, this.guiTop, Config.Gui.scrollerBar));
        this.switchers = this.addButton(new ElementSwitchers(59, this, this.guiLeft - 10, this.guiTop + 14));
        this.extraArea.add(new Rectangle(this.guiLeft - 11 - 18 * this.col, this.guiTop, 10 + 18 * this.col, 166));
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (Config.Gui.typeTip && this.mc.player.inventory.getItemStack().isEmpty()) {
            int xLoc = this.guiLeft - 7;
            if (xLoc - 18 * this.col < mouseX && mouseX <= xLoc) {
                int yLoc = this.guiTop + 15;
                if (mouseY >= yLoc && mouseY < yLoc + 18 * this.baublesAmount) {
                    int indexY = (mouseY - yLoc) / 18 - this.offset;
                    int index = indexY * this.col + (mouseX - xLoc) / 18 + this.col - 1;
                    if (index >= this.baublesAmount) return;
                    ItemStack stack = this.baubles.getStackInSlot(index);
                    if (!stack.isEmpty()) return;

                    String str = I18n.format("name." + this.baubles.getTypeInSlot(index).getName());

                    this.drawHoveringText(Collections.singletonList(str), mouseX - this.guiLeft, mouseY - this.guiTop + 7);
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        //jei compat
        handleMouseInput(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (this.isOverlay) {
            ItemStack itemstack = this.containerEx.slotClick(slotIn.slotNumber, mouseButton, type, this.mc.player);
            PacketHandler.INSTANCE.sendToServer(PacketFakeTransaction.C2SPack(slotId, mouseButton, type, itemstack));
        }
        else {
            super.handleMouseClick(slotIn, slotId, mouseButton, type);
        }
    }

    protected void handleMouseInput(int mouseX, int mouseY) {
        int xLoc = this.guiLeft - 7;
        if (xLoc - 18 * this.col < mouseX && mouseX < xLoc) {
            int yLoc = this.guiTop + 15;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * 8) {
                int dWheel;
                if (jeiLoaded) dWheel = JeiPlugin.JEI_COMPAT.getIngredientUnderMouse(this, mouseX, mouseY);
                else dWheel = Mouse.getEventDWheel();
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

        // draw baubles container
        this.mc.getTextureManager().bindTexture(BAUBLES_TEX);
        drawTexturedModalRect(left - 29, top, 18, 0, 28, 166);
        if (this.wider) {
            for (int i = 0; i < this.col; i++) {
                drawTexturedModalRect(left - 29 - i * 18, top, 18, 0, 24, 166);
            }
        }

        // draw slots
        drawBaubleSlots(left, top);
    }

    protected void drawBaubleSlots(int left, int top) {
        for (int i = 0; i < this.baublesAmount; i++) {
            if (i >= this.baublesAmount + this.offset * this.col) break;
            int j = i / this.col;
            if (j > 7) break;
            int k = i % this.col;
            drawTexturedModalRect(left - 24 + (k - this.col + 1) * 18, top + 15 + (j * 18), 6, 167, 18, 18);
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



    private int initRow() {
        int i = this.baublesAmount / this.col;
        if (this.baublesAmount % this.col != 0) i += 1;
        return i;
    }

    private int initCol() {
        return this.wider ? Config.Gui.column : 1;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getOffset() {
        return this.offset;
    }

    @Override
    public List<Rectangle> getExtraArea() {
        return this.extraArea;
    }

    public boolean isPointed(int mouseX, int mouseY) {
        int i = mouseX * this.width / this.mc.displayWidth;
        int j = this.height - mouseY * this.height / this.mc.displayHeight - 1;
        return this.extraArea.stream().anyMatch(r -> r.contains(i,  j));
    }

    @Override
    protected boolean hasClickedOutside(int mouseX, int mouseY, int p_193983_3_, int p_193983_4_) {
        return super.hasClickedOutside(mouseX, mouseY, p_193983_3_, p_193983_4_) && this.extraArea.stream().noneMatch(r -> r.contains(mouseX,  mouseY));
    }
}