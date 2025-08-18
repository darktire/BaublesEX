package baubles.common;

import baubles.Baubles;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.event.BaublesSync;
import baubles.common.event.EventHandlerEntity;
import baubles.common.event.EventHandlerItem;
import baubles.common.util.IPlayerTarget;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.server.FMLServerHandler;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new GuiPlayerExpanded(player);
        }
        else if (ID == Baubles.GUI_A) {
            return new GuiPlayerExpanded(player, ((IPlayerTarget) player).getTarget());
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new ContainerPlayerExpanded(player);
        }
        else if (ID == Baubles.GUI_A) {
            return new ContainerPlayerExpanded(player, ((IPlayerTarget) player).getTarget());
        }
        return null;
    }

    public WorldServer[] getSeverWorld() {
        return FMLServerHandler.instance().getServer().worlds;
    }

    public WorldClient getClientWorld() {
        return null;
    }

    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());
        MinecraftForge.EVENT_BUS.register(new EventHandlerItem());
        MinecraftForge.EVENT_BUS.register(new BaublesSync());
    }

    public void init() {
    }
}
