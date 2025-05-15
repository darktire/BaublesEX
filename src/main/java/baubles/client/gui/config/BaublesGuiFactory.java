package baubles.client.gui.config;

import baubles.common.Baubles;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

@SuppressWarnings("unused") // gets used by Forge annotations
public class BaublesGuiFactory extends DefaultGuiFactory {

    public BaublesGuiFactory() {
        super(Baubles.MODID, Baubles.MODNAME);
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new BaublesConfigGui(parent);
    }
}
