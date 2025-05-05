package baubles.client;

import baubles.client.gui.GuiBaublesTab;
import baubles.client.gui.GuiEvents;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.Baubles;
import baubles.common.CommonProxy;
import baubles.common.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Map;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        new KeyBindings();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new GuiEvents());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldClient) {
            if (ID == Baubles.GUI) {
                return new GuiPlayerExpanded(player);
            }
            if (ID == Baubles.TAB) {
                return new GuiBaublesTab(player);
            }
        }
        return null;
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    public void init() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        RenderPlayer render;
        render = skinMap.get("default");
        render.addLayer(new BaublesRenderLayer());

        render = skinMap.get("slim");
        render.addLayer(new BaublesRenderLayer());
    }
}
