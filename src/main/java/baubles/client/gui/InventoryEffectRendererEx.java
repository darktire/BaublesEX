package baubles.client.gui;

import baubles.client.ClientProxy;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collection;

@SideOnly(Side.CLIENT)
public abstract class InventoryEffectRendererEx extends GuiContainer {
    protected boolean hasActivePotionEffects;

    public InventoryEffectRendererEx(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public void initGui() {
        super.initGui();
        this.updateActivePotionEffects();
    }

    protected void updateActivePotionEffects() {
        boolean hasVisibleEffect = false;
        for (PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
            Potion potion = potioneffect.getPotion();
            if (potion.shouldRender(potioneffect)) {
                hasVisibleEffect = true;
                break;
            }
        }
        if (this.mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        } else {
            this.hasActivePotionEffects = true;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    @Override
    protected void keyTyped(char par1, int keyCode) throws IOException {
        if (keyCode == ClientProxy.KEY_BAUBLES.getKeyCode()) {
            this.mc.player.closeScreen();
        }
        else if (keyCode == ClientProxy.KEY_BAUBLES_TAB.getKeyCode()) {
            this.mc.player.closeScreen();
        }
        else super.keyTyped(par1, keyCode);
    }

    protected void drawActivePotionEffects() {
        int i = this.guiLeft - 124;
        int j = this.guiTop;
        int k = 166;
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();

        if (!collection.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();

            for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
                Potion potion = potioneffect.getPotion();
                if (!potion.shouldRender(potioneffect)) continue;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
                this.drawTexturedModalRect(i, j, 141, 166, 24, 24);

                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 3, j + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }
            }
        }
    }
}
