package baubles.common;

import baubles.client.gui.GuiBaublesTab;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.container.ContainerBaublesTab;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.event.EventHandlerEntity;
import baubles.common.event.EventHandlerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new GuiPlayerExpanded(player);
        }
        if (ID == Baubles.TAB) {
            return new GuiBaublesTab(player);
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Baubles.GUI) {
            return new ContainerPlayerExpanded(player.inventory, !world.isRemote, player);
        }
        if (ID == Baubles.TAB) {
            return new ContainerBaublesTab(player.inventory, !world.isRemote, player);
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());
        MinecraftForge.EVENT_BUS.register(new EventHandlerItem());
    }

    public void init() {
    }
}
