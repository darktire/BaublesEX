package baubles.client.gui;

import baubles.common.Baubles;
import baubles.common.config.KeyBindings;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public abstract class GuiBaublesBase extends InventoryEffectRenderer {
    public static final ResourceLocation BAUBLES_TEX = new ResourceLocation(Baubles.MODID, "textures/gui/baubles_container.png");

    public GuiBaublesBase(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    protected void resetGuiLeft() {
        this.guiLeft = (this.width - this.xSize) / 2;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        resetGuiLeft();
    }

    @Override
    public void initGui() {
        super.initGui();
        resetGuiLeft();
    }

    @Override
    protected void keyTyped(char par1, int keyCode) throws IOException {
        if (keyCode == KeyBindings.KEY_BAUBLES.getKeyCode()) {
            this.mc.player.closeScreen();
        }
        else super.keyTyped(par1, keyCode);
    }
}