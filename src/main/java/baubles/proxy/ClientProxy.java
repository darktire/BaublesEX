package baubles.proxy;

import baubles.client.render.BaublesRenderLayer;
import baubles.common.config.KeyBindings;
import baubles.util.HookHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static BaublesRenderLayer NORMAL_LAYER, SLIM_LAYER;

    @Override
    public WorldClient getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    public void preInit() {
        super.preInit();
        KeyBindings.register();
        if (HookHelper.isModLoaded("RLArtifacts")) MinecraftForge.EVENT_BUS.register(baubles.compat.artifacts.EventHandler.class);
        if (Loader.isModLoaded("bountifulbaubles")) MinecraftForge.EVENT_BUS.register(baubles.compat.bountifulbaubles.EventHandler.class);
        if (Loader.isModLoaded("enigmaticlegacy")) MinecraftForge.EVENT_BUS.register(baubles.compat.enigmaticlegacy.EventHandler.class);
        if (Loader.isModLoaded("xat")) MinecraftForge.EVENT_BUS.register(baubles.compat.xat.EventHandler.class);
    }

    @Override
    public void init() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

        NORMAL_LAYER = addLayersToSkin(skinMap.get("default"), false);
        SLIM_LAYER = addLayersToSkin(skinMap.get("slim"), true);
    }

    private static BaublesRenderLayer addLayersToSkin(RenderPlayer renderPlayer, boolean slim) {
        BaublesRenderLayer layer = new BaublesRenderLayer(renderPlayer, slim);
        renderPlayer.addLayer(layer);
        return layer;
    }
}
