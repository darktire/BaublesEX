package baubles.client.gui.element;

import baubles.client.gui.GuiExpanded;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ElementSwitchers extends ElementBase {
    private final List<ElementSwitcher> buttonList = new ArrayList<>();

    public ElementSwitchers(int buttonId, GuiExpanded parentGui, int x, int y) {
        super(buttonId, x, y, 0, 0, "", parentGui);
        this.initSwitchers(false);
    }

    public void initSwitchers(boolean flag) {
        if (flag) this.buttonList.clear();
        int column = this.parentGui.getCol();
        for (int i = 0; i < this.parentGui.baublesAmount; i++) {
            int j = i / column;
            int k = i % column;
            this.buttonList.add(new ElementSwitcher(i, this.parentGui, this.x + (k - column + 1) * 18, this.y + (j * 18)));
        }
    }

    public void updateSwitchers() {
        for (ElementSwitcher switcher : this.buttonList) {
            switcher.y = switcher.startY + this.parentGui.getOffset() * 18;
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            for (ElementSwitcher switcher : this.buttonList) {
                switcher.mousePressed(mc, mouseX, mouseY);
            }
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            for (ElementSwitcher switcher : this.buttonList) {
                switcher.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }
    }
}
