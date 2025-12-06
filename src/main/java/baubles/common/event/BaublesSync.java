package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.common.container.ExpansionManager;
import baubles.common.network.IBaublesSync;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import baubles.common.network.PacketSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class BaublesSync {

    @SubscribeEvent
    public static void syncBaubles(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (!event.player.world.isRemote) {
            if (event.player.openContainer instanceof IBaublesSync) {
                ((IBaublesSync) event.player.openContainer).syncBaubles();
            }
            if (ExpansionManager.getInstance().isExpanded(event.player)) {
                ExpansionManager.getInstance().getExpansion(event.player).syncBaubles();
            }
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                syncModifier(player, Collections.singletonList(player));
                syncAllSlots(player, Collections.singletonList(player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP) {
            syncAllSlots((EntityPlayer) target, Collections.singletonList(event.getEntityPlayer()));
        }
    }

    private static void syncAllSlots(EntityLivingBase entity, Collection<? extends EntityPlayer> receivers) {
        BaublesApi.applyByIndex(entity, (baubles, i) -> {
            PacketSync pkt = PacketSync.S2CPack(entity, i, baubles.getStackInSlot(i), baubles.getVisible(i) ? 1 : 0);
            for (EntityPlayer receiver : receivers) {
                PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
            }
        });
    }

    private static void syncModifier(EntityLivingBase entity, Collection<? extends EntityPlayerMP> receivers) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        TypesData.applyToTypes(type -> {
            String typeName = type.getName();
            int modifier = baubles.getModifier(typeName);
            if (modifier != 0) {
                for (EntityPlayerMP receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(entity, typeName, modifier, 1), receiver);
                }
            }
        });
    }
}
