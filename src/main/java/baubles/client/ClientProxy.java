package baubles.client;

import baubles.api.render.BaublesRenderLayer;
import baubles.client.gui.GuiEvents;
import baubles.common.CommonProxy;
import baubles.common.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        new KeyBindings();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new GuiEvents());
    }

    @Override
    public WorldClient getClientWorld() {
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
