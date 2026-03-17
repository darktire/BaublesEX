package baubles.proxy;

import baubles.Baubles;
import baubles.BaublesRegister;
import baubles.api.AbstractWrapper;
import baubles.api.BaublesWrapper;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import baubles.common.config.Config;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.network.NetworkHandler;
import baubles.common.network.PacketPool;
import baubles.util.HookHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler {

    public static final int GUI = 0;

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI) {
            return new ContainerPlayerExpanded(player, player);
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void preInit(FMLPreInitializationEvent event) {
        TypeData.Preset.init();
        Config.loadConfig();
        BaublesRegister.setTypes();

        CapabilityManager.INSTANCE.register(
                IBaublesItemHandler.class,
                new BaublesCapabilities.CapabilityBaubles(),
                BaublesContainer::new);
        CapabilityManager.INSTANCE.register(
                AbstractWrapper.class,
                new BaublesCapabilities.CapabilityItemBaubleStorage(),
                BaublesWrapper::new);

        HookHelper.patchModsEvents(event.getAsmData());
        NetworkHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        Config.setupBlacklist();
        NetworkRegistry.INSTANCE.registerGuiHandler(Baubles.instance, this);
    }

    public void postInit(FMLPostInitializationEvent event) {
        PacketPool.warmup();
    }
}
