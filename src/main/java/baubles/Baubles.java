package baubles;

import baubles.api.BaublesApi;
import baubles.common.command.BaublesCommand;
import baubles.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = BaublesApi.MOD_ID,
        name = BaublesApi.MOD_NAME,
        guiFactory = "baubles.client.gui.config.BaublesGuiFactory"
)
public class Baubles {

    @SidedProxy(clientSide = "baubles.proxy.ClientProxy", serverSide = "baubles.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = BaublesApi.MOD_ID)
    public static Baubles instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new BaublesCommand());
    }
}