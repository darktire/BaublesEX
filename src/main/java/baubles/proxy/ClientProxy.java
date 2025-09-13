package baubles.proxy;

import baubles.client.render.BaublesRenderLayer;
import baubles.common.config.KeyBindings;
import baubles.util.HookHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
        patchModsEvents(HookHelper.isModLoaded("RLArtifacts"), "artifacts", true);
        patchModsEvents(Loader.isModLoaded("bountifulbaubles"), "bountifulbaubles", true);
        patchModsEvents(Loader.isModLoaded("enigmaticlegacy"), "enigmaticlegacy", true);
        patchModsEvents(Loader.isModLoaded("xat"), "xat", true);
        patchModsEvents(Loader.isModLoaded("aether_legacy"), "aether_legacy", true);
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
