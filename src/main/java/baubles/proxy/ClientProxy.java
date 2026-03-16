package baubles.proxy;

import baubles.client.gui.GuiPlayerExpanded;
import baubles.client.render.BaublesRenderLayer;
import baubles.common.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static BaublesRenderLayer NORMAL_LAYER, SLIM_LAYER;

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI) {
            return new GuiPlayerExpanded(player, player);
        }
        return null;
    }

    @Override
    public WorldClient getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        KeyBindings.register();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

        NORMAL_LAYER = addLayersToSkin(skinMap.get("default"));
        SLIM_LAYER = addLayersToSkin(skinMap.get("slim"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    private static BaublesRenderLayer addLayersToSkin(RenderPlayer renderPlayer) {
        BaublesRenderLayer layer = new BaublesRenderLayer(renderPlayer);
        renderPlayer.addLayer(layer);
        return layer;
    }
}
