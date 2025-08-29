package baubles.client.gui.config;

import baubles.Baubles;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

@SuppressWarnings("unused") // gets used by Forge annotations
public class BaublesGuiFactory extends DefaultGuiFactory {

    public BaublesGuiFactory() {
        super(Baubles.MOD_ID, Baubles.MOD_NAME);
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new BaublesConfigGui(parent);
    }
}
