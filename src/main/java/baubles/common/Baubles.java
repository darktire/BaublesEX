package baubles.common;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities.CapabilityBaubles;
import baubles.api.cap.BaublesCapabilities.CapabilityItemBaubleStorage;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.network.PacketHandler;
import baubles.common.util.BaublesRegistry;
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
    public static final String VERSION = "2.2.1";
    public static final String FACTORY = "baubles.client.gui.config.BaublesGuiFactory";

    public static Config config;
    public static BaublesRegistry registry;

    @SidedProxy(clientSide = "baubles.client.ClientProxy", serverSide = "baubles.common.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = Baubles.MODID)
    public static Baubles instance;

    public static final Logger log = LogManager.getLogger(MODID.toUpperCase());
    public static final int GUI = 0;
    public static final int GUI_A = 1;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Config(event);
        registry = new BaublesRegistry();

        CapabilityManager.INSTANCE.register(
                IBaublesModifiable.class,
                new CapabilityBaubles(),
                BaublesContainer::new);
        CapabilityManager.INSTANCE.register(
                IBauble.class,
                new CapabilityItemBaubleStorage(),
                BaublesWrapper::new);

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