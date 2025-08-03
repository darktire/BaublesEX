package baubles.client.gui;

import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesModifiable;
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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collections;

public class GuiPlayerExpanded extends GuiBaublesBase {
    @Deprecated public static final ResourceLocation background = new ResourceLocation("baubles","textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    public final IBaublesModifiable baubles = ((ContainerPlayerExpanded) this.inventorySlots).baubles;//container in sever
    public int finalLine = Math.min(8, baubles.getSlots());//todo need updater
    public int offset = 0;
    private GUIBaublesScroller scroller;

    public GuiPlayerExpanded(EntityPlayer player) {
        super(new ContainerPlayerExpanded(player.inventory, !player.getEntityWorld().isRemote, player));
        this.allowUserInput = true;
    }

    /**
     * Positive number means moving up slots.
     * @param value
     */
    public void moveBaubleSlots(int value) {
        if (value == 0) return;
        int baublesAmount = baubles.getSlots();
        int offset1 = offset + value;
        if (offset1 > 0) value = -offset;
        if (offset1 < finalLine - baublesAmount) value = finalLine - offset - baublesAmount;
        offset += value;
        for (int i = 46; i < 46 + baublesAmount; ++i) {
            Slot slot1 = inventorySlots.inventorySlots.get(i);
            SlotBaubleHandler baubleSlots = ((SlotBaubleHandler) slot1);
            baubleSlots.incrYPos(18 * value);
        }
    }

    public GUIBaublesScroller getScroller() {
        return scroller;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        baubles.setEventBlock(false);
        super.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
        buttonList.add(new GUIBaublesController(56, this, guiLeft - 24, guiTop + 5, 1));
        buttonList.add(new GUIBaublesController(57, this, guiLeft - 14, guiTop + 5, 2));
        scroller = new GUIBaublesScroller(58, this, guiLeft - 48, guiTop);
        buttonList.add(scroller);
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        int xLoc = guiLeft - 22;
        if (mouseX > xLoc && mouseX < xLoc + 18) {
            int yLoc = guiTop + 14;
            if (mouseY >= yLoc && mouseY < yLoc + 18 * finalLine) {
                int index = (mouseY - yLoc) / 18 - offset;
                BaublesContainer container = (BaublesContainer) baubles;
                ItemStack stack = container.getStackInSlot(index);
                if (!stack.isEmpty()) return;

                FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 200);
                String str = I18n.format("name." + baubles.getTypeInSlot(index).getTypeName());

                GuiUtils.drawHoveringText(Collections.singletonList(str), mouseX - guiLeft, mouseY - guiTop + 7, width, height, 300, renderer);
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
                    moveBaubleSlots(value);
                    scroller.moveScrollerBar(value);
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
        int k = guiLeft;
        int l = guiTop;
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // draw inventory
        mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        // draw baubles container
        mc.getTextureManager().bindTexture(BAUBLES_TEX);
        drawTexturedModalRect(k - 29, l, 18, 0, 28, 166);

        // draw slots
        for (int i = 0; i < finalLine; i++) {
            drawTexturedModalRect(k - 24, l + 15 + (i * 18), 6, 167, 18, 18);
        }

        GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (k + 51) - mouseX, (l + 75 - 50) - mouseY, mc.player);
    }

    @Override
    protected void drawActivePotionEffects() {
        if (!scroller.visible) {
            guiLeft -= 27;
            super.drawActivePotionEffects();
            guiLeft += 27;
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