package baubles.common;

import baubles.Baubles;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.util.IHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new GuiPlayerExpanded(player);
        }
        else if (ID == Baubles.GUI_A) {
            return new GuiPlayerExpanded(player, ((IHelper) player).getTarget());
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new ContainerPlayerExpanded(player);
        }
        else if (ID == Baubles.GUI_A) {
            return new ContainerPlayerExpanded(player, ((IHelper) player).getTarget());
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void preInit() {}

    public void init() {}
}
