package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import baubles.common.network.PacketSync;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class BaublesSync {
    private static final HashMap<UUID, Deque<ItemStack>> onDropping = new HashMap<>();

    @SubscribeEvent
    public static void syncBaubles(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (!event.player.world.isRemote && event.player.openContainer instanceof ContainerPlayerExpanded) {
            ((ContainerPlayerExpanded) event.player.openContainer).syncBaubles();
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                dropItem((EntityLivingBase) entity);
                syncModifier(player, Collections.singletonList(player));
                syncAllSlots(player, Collections.singletonList(player));
            }
            if (entity instanceof EntityPlayerSP) {
                UUID uuid = entity.getUniqueID();
                Deque<ItemStack> deque = onDropping.get(uuid);
                if (deque != null) {
                    while (!deque.isEmpty()) {
                        ItemStack stack = deque.pop();
                        BaublesApi.toBauble(stack).onUnequipped(stack, (EntityLivingBase) entity);
                    }
                    onDropping.remove(uuid);
                }
            }
        }
    }

    private static void dropItem(EntityLivingBase entity) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        Deque<ItemStack> deque = new ArrayDeque<>();
        while (baubles.haveDroppingItem()) {
            ItemStack stack = baubles.getDroppingItem();
            EntityItem ei = entity.entityDropItem(stack, 0F);
            if (ei != null) {
                ei.setNoDespawn();
                ei.setNoPickupDelay();
//                    boolean ok = entity.world.spawnEntity(ei);
//                    Baubles.log.warn("spawnEntity result = " + ok);
            }
            if (BaublesApi.isBauble(stack)) {
                BaublesApi.toBauble(stack).onUnequipped(stack, entity);
            }
            deque.push(stack);
        }
        if (!deque.isEmpty()) onDropping.put(entity.getUniqueID(), deque);
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
            PacketSync pkt = PacketSync.severPack(entity, i, baubles.getStackInSlot(i), baubles.getVisible(i));
            for (EntityPlayer receiver : receivers) {
                PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
            }
        });
    }

    private static void syncModifier(EntityLivingBase entity, Collection<? extends EntityPlayerMP> receivers) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        TypesData.applyToTypes(type -> {
            String typeName = type.getTypeName();
            int modifier = baubles.getModifier(typeName);
            if (modifier != 0) {
                for (EntityPlayerMP receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(entity, typeName, modifier, 1), receiver);
                }
            }
        });
    }
}
