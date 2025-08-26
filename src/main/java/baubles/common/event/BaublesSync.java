package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class BaublesSync {
    private final HashMap<UUID, Deque<ItemStack>> onDropping = new HashMap<>();

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                this.dropItem((EntityLivingBase) entity);
                this.syncModifier(player, Collections.singletonList(player));
            }
            if (entity instanceof EntityPlayerSP) {
                UUID uuid = entity.getUniqueID();
                Deque<ItemStack> deque = this.onDropping.get(uuid);
                if (deque != null) {
                    while (!deque.isEmpty()) {
                        ItemStack stack = deque.pop();
                        BaublesApi.toBauble(stack).onUnequipped(stack, (EntityLivingBase) entity);
                    }
                    this.onDropping.remove(uuid);
                }
            }
        }
    }

    private void dropItem(EntityLivingBase entity) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        Deque<ItemStack> deque = new ArrayDeque<>();
        while (baubles.haveDroppingItem()) {
            ItemStack stack = baubles.getDroppingItem();
            entity.entityDropItem(stack, 0);
            if (BaublesApi.isBauble(stack)) {
                BaublesApi.toBauble(stack).onUnequipped(stack, entity);
                deque.push(stack);
            }
        }
        if (!deque.isEmpty()) this.onDropping.put(entity.getUniqueID(), deque);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {}

    private void syncModifier(EntityLivingBase entity, Collection<? extends EntityPlayerMP> receivers) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        TypesData.iterator().forEachRemaining(type -> {
            String typeName = type.getTypeName();
            int modifier = baubles.getModifier(typeName);
            if (modifier != 0) {
                for (EntityPlayerMP receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(entity, typeName, modifier, 0), receiver);
                }
            }
        });
    }
}
