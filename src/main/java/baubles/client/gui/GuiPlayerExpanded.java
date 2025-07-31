package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collections;

public class GuiPlayerExpanded extends GuiBaublesBase {
    @Deprecated
    public static final ResourceLocation background = new ResourceLocation("baubles","textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    private final IBaublesItemHandler baubles = ((ContainerPlayerExpanded) this.inventorySlots).baubles;
    private final EntityLivingBase entity;
    private float oldMouseX, oldMouseY;
    private final int finalLine = Math.min(8, baubles.getSlots());
    private int offset = 0;

    public GuiPlayerExpanded(EntityPlayer player) {
        super(new ContainerPlayerExpanded(player.inventory, !player.getEntityWorld().isRemote, player));
        this.entity = player;
        this.allowUserInput = true;
    }

    /**
     * Positive number means moving up slots.
     * @param value
     */
    public void moveBaubleSlots(int value) {
        int baublesAmount = baubles.getSlots();
        int offset1 = offset + value;
        if (offset1 > 0) value = -offset;
        if (offset1 < finalLine - baublesAmount) value = finalLine - offset - baublesAmount;
        offset += value;
        for (int i = 9; i < 9 + baublesAmount; ++i) {
            Slot slot1 = inventorySlots.inventorySlots.get(i);
            SlotBaubleHandler baubleSlots = ((SlotBaubleHandler) slot1);
            baubleSlots.incrYPos(18 * value);
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        this.baubles.setEventBlock(false);
        super.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
//        this.buttonList.add(new GUIBaublesController(56, this, guiLeft - 27, guiTop, false));
//        this.buttonList.add(new GUIBaublesController(57, this, guiLeft - 27, guiTop + 14 + getMaxY(), true));
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
            if (mouseY >= yLoc && mouseY < yLoc + 18 * finalLine) {
                int index = (mouseY - yLoc) / 18 - offset;
                BaublesContainer container = ((BaublesContainer) baubles);

                ItemStack stack = container.getStackInSlot(index);
                if (!stack.isEmpty()) return;

                FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 200);
                String str = I18n.format("name." + BaublesApi.getBaublesHandler(entity).getTypeInSlot(index).getTypeName());

                GuiUtils.drawHoveringText(Collections.singletonList(str), mouseX - this.guiLeft, mouseY - this.guiTop + 7, width, height, 300, renderer);
                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
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
            if (mouseY >= yLoc && mouseY < yLoc + 18 * finalLine) {
                int dWheel = Mouse.getEventDWheel();
                if (dWheel != 0) {
                    int value = dWheel / 120;
                    moveBaubleSlots(value);
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
        mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        // draw baubles container
        mc.getTextureManager().bindTexture(BAUBLES_TEX);
        drawTexturedModalRect(k - 29, l, 18, 0, 28, 166);

        // draw slots
        for (int i = 0; i < finalLine; i++) {
            drawTexturedModalRect(k - 24, l + 15 + (i * 18), 6, 167, 18, 18);
        }

        GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.mc.player);
    }

    @Override
    protected void drawActivePotionEffects() {
        int i = this.guiLeft;
        guiLeft -= 27;
        super.drawActivePotionEffects();
        guiLeft = i;
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
}