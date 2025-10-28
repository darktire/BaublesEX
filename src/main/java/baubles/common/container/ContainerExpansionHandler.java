package baubles.common.container;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class ContainerExpansionHandler {

    @SubscribeEvent
    public void onOriginalContainerClose(PlayerContainerEvent.Close event) {
        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        ExpansionManager manager = ExpansionManager.getInstance();
        if (manager.hasOpenExpansion(player)) manager.closeExpansion(player);
    }
}
