package baubles.common.event;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.util.BaublesContent;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import baubles.common.network.PacketSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class BaublesSync {

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            syncModifier(player, Collections.singletonList(player));
            syncSlots(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP) {
            syncSlots((EntityPlayer) target, Collections.singletonList(event.getEntityPlayer()));
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            if (!player.world.isRemote) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
                syncBaubles(player, baubles);
            }
        }
    }

    private void syncModifier(EntityLivingBase entity, Collection<? extends EntityPlayerMP> receivers) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        Iterator<BaubleTypeEx> iterator = BaublesContent.iterator();
        while (iterator.hasNext()) {
            String typeName = iterator.next().getTypeName();
            int modifier = baubles.getModifier(typeName);
            if (modifier == 0) continue;
            for (EntityPlayerMP receiver : receivers) {
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots((EntityPlayer) entity, typeName, modifier, false), receiver);
            }
        }
    }

    private void syncBaubles(EntityPlayer player, IBaublesItemHandler baubles) {
        Set<EntityPlayer> receivers = null;
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            IBauble bauble = BaublesApi.toBauble(stack);
            if (baubles.isChanged(i) || bauble != null && bauble.willAutoSync(stack, player)) {
                if (receivers == null) {
                    receivers = new HashSet<>(((WorldServer) player.world).getEntityTracker().getTrackingPlayers(player));
                    receivers.add(player);
                }
                syncSlot(player, i, stack, receivers);
                baubles.setChanged(i, false);
            }
        }
    }

    private void syncSlots(EntityPlayer player, Collection<? extends EntityPlayer> receivers) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            syncSlot(player, i, baubles.getStackInSlot(i), receivers);
        }
    }

    private void syncSlot(EntityPlayer player, int slot, ItemStack stack, Collection<? extends EntityPlayer> receivers) {
        PacketSync pkt = new PacketSync(player, slot, stack);
        for (EntityPlayer receiver : receivers) {
            PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
        }
    }
}
