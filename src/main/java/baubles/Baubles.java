package baubles;

import baubles.api.BaublesWrapper;
import baubles.api.IWrapper;
import baubles.api.cap.BaublesCapabilities.CapabilityBaubles;
import baubles.api.cap.BaublesCapabilities.CapabilityItemBaubleStorage;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.command.BaublesCommand;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.proxy.CommonProxy;
import baubles.util.ModsHelper;
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
        modid = Baubles.MOD_ID,
        name = Baubles.MOD_NAME,
        guiFactory = Baubles.FACTORY
)
public class Baubles {

    public static final String MOD_ID = "baubles";
    public static final String MOD_NAME = "BaublesEX";
    public static final String FACTORY = "baubles.client.gui.config.BaublesGuiFactory";

    @SidedProxy(clientSide = "baubles.proxy.ClientProxy", serverSide = "baubles.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = Baubles.MOD_ID)
    public static Baubles instance;

    public static final Logger log = LogManager.getLogger(MOD_ID.toUpperCase());
    public static final int GUI = 0;
    public static final int GUI_A = 1;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event);
        BaublesRegister.registerBaubles();
        BaublesRegister.loadValidSlots();

        CapabilityManager.INSTANCE.register(
                IBaublesModifiable.class,
                new CapabilityBaubles(),
                BaublesContainer::new);
        CapabilityManager.INSTANCE.register(
                IWrapper.class,
                new CapabilityItemBaubleStorage(),
                BaublesWrapper::new);

        proxy.preInit();
        ModsHelper.patchModsEvents(event.getAsmData());
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.init();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new BaublesCommand());
    }
}