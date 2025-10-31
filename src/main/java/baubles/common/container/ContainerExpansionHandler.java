package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.util.HookHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.client.gui.box.ContainerBaubleBox;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class ContainerExpansionHandler {
    private static final boolean BOTANIA = HookHelper.isModLoaded("botania");

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        if (!isTarget(event.getContainer())) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        ExpansionManager manager = ExpansionManager.getInstance();
        manager.openExpansion(player, ContainerExpanded.create(player));
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        if (!isTarget(event.getContainer())) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        ExpansionManager manager = ExpansionManager.getInstance();
        if (manager.hasOpenExpansion(player)) manager.closeExpansion(player);
    }

    private static boolean isTarget(Container container) {
        if (BOTANIA) {
            return container instanceof ContainerBaubleBox;
        }
        return false;
    }
}
