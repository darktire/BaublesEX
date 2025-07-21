package baubles.common;

import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilities.CapabilityBaubles;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.config.json.JsonHelper;
import baubles.common.extra.BaublesContent;
import baubles.common.network.PacketHandler;
import baubles.common.util.command.CommandBaubles;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Baubles.MODID,
        name = Baubles.MODNAME,
        version = Baubles.VERSION,
        guiFactory = Baubles.FACTORY)
public class Baubles {

    public static final String MODID = "baubles";
    public static final String MODNAME = "BaublesEX";
    public static final String VERSION = "2.1.5";
    public static final String FACTORY = "baubles.client.gui.config.BaublesGuiFactory";

    public static Config config;
    public static JsonHelper jsonHelper;
    public static BaublesContent baubles;

    @SidedProxy(clientSide = "baubles.client.ClientProxy", serverSide = "baubles.common.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = Baubles.MODID)
    public static Baubles instance;

    public static final Logger log = LogManager.getLogger(MODID.toUpperCase());
    public static final int GUI = 0;
    public static final int TAB = 550;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Config(event);
        jsonHelper = new JsonHelper();
        baubles = new BaublesContent();

        CapabilityManager.INSTANCE.register(
                IBaublesItemHandler.class,
                new CapabilityBaubles<>(),
                BaublesContainer::new);
        CapabilityManager.INSTANCE.register(
                IBauble.class,
                new BaublesCapabilities.CapabilityItemBaubleStorage(),
                BaubleItem::new);

        proxy.registerEventHandlers();
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.init();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBaubles());
    }
}