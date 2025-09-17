package baubles.proxy;

import baubles.Baubles;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.container.ContainerPlayerExpanded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new GuiPlayerExpanded(player);
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new ContainerPlayerExpanded(player);
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void preInit() {
        patchModsEvents(Loader.isModLoaded("aether_legacy"), "aether_legacy", false);
        patchModsEvents(Loader.isModLoaded("crafttweaker"), "crt", false);
    }

    public void init() {}

    protected static void patchModsEvents(boolean flag, String name, boolean client) {
        if (flag) {
            try { MinecraftForge.EVENT_BUS.register(Class.forName("baubles.compat." + name + "." + (client ? "Client" : "") + "EventHandler")); }
            catch (ClassNotFoundException ignored) {}
        }
    }
}
