package baubles;

import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.IWrapper;
import baubles.api.cap.BaublesCapabilities.CapabilityBaubles;
import baubles.api.cap.BaublesCapabilities.CapabilityItemBaubleStorage;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.common.command.BaublesCommand;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.proxy.CommonProxy;
import baubles.util.HookHelper;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
        TypesData.Preset.init();
        Config.loadConfig(event);
        BaublesRegister.setTypes();

        CapabilityManager.INSTANCE.register(
                IBaublesItemHandler.class,
                new CapabilityBaubles(),
                BaublesContainer::new);
        CapabilityManager.INSTANCE.register(
                IWrapper.class,
                new CapabilityItemBaubleStorage(),
                BaublesWrapper::new);

        proxy.preInit();
        HookHelper.patchModsEvents(event.getAsmData());
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Config.setupBlacklist();
        proxy.postInit();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new BaublesCommand());
    }
}