package baubles.common.container;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class ContainerExpansionHandler {
//    @SubscribeEvent
//    public static void onContainerOpen(PlayerContainerEvent.Open event) {
//        ExpansionManager manager = ExpansionManager.getInstance();
//        if (!manager.isExpanded(event.getContainer())) return;
//
//        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
//        manager.openExpansion(player, ContainerExpansion.create(player));
//    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        ExpansionManager manager = ExpansionManager.getInstance();
        if (!manager.isMarked(event.getContainer())) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        if (manager.isExpanded(player)) manager.closeExpansion(player);
    }
}
