package baubles.common.event;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class BaublesSync {

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            syncModifier(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    private void syncModifier(EntityLivingBase entity, Collection<? extends EntityPlayerMP> receivers) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        Iterator<BaubleTypeEx> iterator = TypesData.iterator();
        while (iterator.hasNext()) {
            String typeName = iterator.next().getTypeName();
            int modifier = baubles.getModifier(typeName);
            if (modifier == 0) continue;
            for (EntityPlayerMP receiver : receivers) {
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots((EntityPlayer) entity, typeName, modifier, 0), receiver);
            }
        }
    }
}
