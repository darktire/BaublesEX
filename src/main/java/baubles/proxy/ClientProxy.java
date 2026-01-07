package baubles.proxy;

import baubles.client.render.BaublesRenderLayer;
import baubles.common.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
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
    }

    @Override
    public void init() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

        NORMAL_LAYER = addLayersToSkin(skinMap.get("default"));
        SLIM_LAYER = addLayersToSkin(skinMap.get("slim"));
    }

    private static BaublesRenderLayer addLayersToSkin(RenderPlayer renderPlayer) {
        BaublesRenderLayer layer = new BaublesRenderLayer(renderPlayer);
        renderPlayer.addLayer(layer);
        return layer;
    }
}
