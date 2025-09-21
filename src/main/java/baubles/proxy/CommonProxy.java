package baubles.proxy;

import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.container.ContainerPlayerExpanded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    public static final int GUI = 0;

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI) {
            return new GuiPlayerExpanded(player);
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI) {
            return new ContainerPlayerExpanded(player);
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void preInit() {}

    public void init() {}
}
