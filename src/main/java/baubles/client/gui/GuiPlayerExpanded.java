package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.client.gui.element.ElementButton;
import baubles.common.container.ContainerPlayerExpanded;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPlayerExpanded extends GuiExpanded {
    @Deprecated public static final ResourceLocation background = new ResourceLocation(BaublesApi.MOD_ID,"textures/gui/expanded_inventory.png");//used by 'Trinkets and Baubles'
    private final EntityPlayer player;

    public static GuiExpanded create(EntityPlayer player, EntityLivingBase entity) {
        return new GuiPlayerExpanded(player, entity).startListening();
    }

    private GuiPlayerExpanded(EntityPlayer player, EntityLivingBase entity) {
        super(ContainerPlayerExpanded.create(player, entity));
        this.player = player;
        this.allowUserInput = true;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void initElements() {
        super.initElements();
        this.addButton(new ElementButton(55, this, 64, 9, I18n.format("button.normal")));
    }

    /**
     * Draw the foreground layer for the GuiContainer (things in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
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

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // draw player
        GuiInventory.drawEntityOnScreen(left + 51, top + 75, 30, (left + 51) - mouseX, (top + 75 - 50) - mouseY, this.player);
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
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public void displayNormalInventory() {
        GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
    }
}