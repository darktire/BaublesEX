package baubles.common.handler;

import baubles.api.BaublesApi;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.NetworkHandler;
import baubles.common.network.PacketModifier;
import baubles.common.network.PacketSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class BaublesSync {

    @SubscribeEvent
    public static void syncBaubles(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        BaublesApi.getBaublesHandler((EntityLivingBase) event.player).updateContainer();
        if (!event.player.world.isRemote) {
            syncModifier((EntityPlayerMP) event.player);
            syncBaubles(event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP player) {
            BaublesApi.applyByIndex(player, (baubles, i) -> {
                PacketSync pkt = PacketSync.S2CPack(player, i, baubles.getStackInSlot(i), baubles.getVisible(i) ? 1 : 0);
                NetworkHandler.CHANNEL.sendTo(pkt, player);
            });
        }
    }

    public static void syncModifier(EntityPlayerMP player) {
        AttributeManager.getModified(player).forEach((type, instance) -> {
            PacketModifier message = new PacketModifier(player, type, instance.getBaseValue(), instance.getModifiers());
            for (int i = 0; i < 3; i++) {
                int modifier = (int) instance.getAnonymousModifier(i);
                if (modifier != 0) {
                    message.append(new PacketModifier(player, type, modifier, i));
                }
            }
            NetworkHandler.CHANNEL.sendTo(message, player);
            instance.callback();
        });
    }

    public static void syncBaubles(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (!baubles.canSync()) return;
        if (baubles.stx.isDirty()) {
            baubles.stx.forEach(i -> {
                ItemStack stack = baubles.getStackInSlot(i);
                PacketSync pkt = PacketSync.S2CPack(entity, i, stack, -1);
                NetworkHandler.CHANNEL.sendTo(pkt, (EntityPlayerMP) entity);
                NetworkHandler.CHANNEL.sendToAllTracking(pkt, entity);
            });
            baubles.stx.clear();
        }
        if (baubles.vis.isDirty()) {
            baubles.vis.forEach(i -> {
                PacketSync pkt = PacketSync.S2CPack(entity, i, null, baubles.getVisible(i) ? 1 : 0);
                NetworkHandler.CHANNEL.sendTo(pkt, (EntityPlayerMP) entity);
                NetworkHandler.CHANNEL.sendToAllTracking(pkt, entity);
            });
            baubles.vis.clear();
        }
    }
}
