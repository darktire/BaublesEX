package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.NetworkHandler;
import baubles.common.network.PacketModifier;
import baubles.common.network.PacketSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        if (!event.player.world.isRemote) {
//            if (event.player.openContainer instanceof ContainerExpansion) {
//                ((ContainerExpansion) event.player.openContainer).syncBaubles();
//            }
//            else if (event.player.openContainer != event.player.inventoryContainer && ExpansionManager.getInstance().isExpanded(event.player)) {
//                ExpansionManager.getInstance().getExpansion(event.player).syncBaubles();
//            }
            syncModifier((EntityPlayerMP) event.player);
            syncBaubles(event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP) {
            syncAllSlots((EntityPlayer) target, (EntityPlayerMP) event.getEntityPlayer());
        }
    }

    private static void syncAllSlots(EntityLivingBase entity, EntityPlayerMP player) {
        BaublesApi.applyByIndex(entity, (baubles, i) -> {
            PacketSync pkt = PacketSync.S2CPack(entity, i, baubles.getStackInSlot(i), baubles.getVisible(i) ? 1 : 0);
            NetworkHandler.CHANNEL.sendTo(pkt, player);
        });
    }

    private static void syncModifier(EntityPlayerMP player) {
        AttributeManager.getModified(player).forEach((type, instance) -> {
            NetworkHandler.CHANNEL.sendTo(new PacketModifier(player, type, instance.getBaseValue(), instance.getModifiers()), player);
            instance.isModified = false;
        });
    }

    private static void syncBaubles(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (!baubles.canSync()) return;
        if (baubles.stx.isDirty()) {
            baubles.stx.stream().forEach(i -> {
                ItemStack stack = baubles.getStackInSlot(i);
                PacketSync pkt = PacketSync.S2CPack(entity, i, stack, -1);
                NetworkHandler.CHANNEL.sendTo(pkt, (EntityPlayerMP) entity);
                NetworkHandler.CHANNEL.sendToAllTracking(pkt, entity);
            });
            baubles.stx.clear();
        }
        if (baubles.vis.isDirty()) {
            baubles.vis.stream().forEach(i -> {
                PacketSync pkt = PacketSync.S2CPack(entity, i, null, baubles.getVisible(i) ? 1 : 0);
                NetworkHandler.CHANNEL.sendTo(pkt, (EntityPlayerMP) entity);
                NetworkHandler.CHANNEL.sendToAllTracking(pkt, entity);
            });
            baubles.vis.clear();
        }
    }
}
