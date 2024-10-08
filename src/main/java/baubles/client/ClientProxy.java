package baubles.client;

import baubles.client.gui.GuiBaublesTab;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.Baubles;
import baubles.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.Map;

public class ClientProxy extends CommonProxy {

    public static final KeyBinding KEY_BAUBLES = new KeyBinding("keybind.baublesinventory", Keyboard.KEY_B, "key.categories.inventory");
    public static final KeyBinding KEY_BAUBLES_TAB = new KeyBinding("keybind.baublestab", 0,"key.categories.inventory");

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
        ClientRegistry.registerKeyBinding(KEY_BAUBLES_TAB);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
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
