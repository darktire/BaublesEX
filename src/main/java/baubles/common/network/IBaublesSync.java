package baubles.common.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

import java.util.*;

public interface IBaublesSync {

    default void syncBaubles() {
        syncBaubles(getEntity(), getBaubleStacks(), getVisibilities());
    }
    
    static void syncBaubles(EntityLivingBase entity, List<ItemStack> stacks, BitSet vis) {
        Set<EntityPlayer> receivers = null;
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (!baubles.getDirty().get(i)) continue;

            ItemStack stack = baubles.getStackInSlot(i);
            ItemStack stack1 = stacks.get(i);

            boolean clientStackChanged = false;
            if (!ItemStack.areItemStacksEqual(stack1, stack)) {
                clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(stack1, stack);
                stack1 = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
                stacks.set(i, stack1);
            }

            boolean v = baubles.getVisible(i);
            boolean visibilityChanged = false;
            if (vis != null) {
                boolean v1 = vis.get(i);

                visibilityChanged = v == v1;
                if (visibilityChanged) {
                    if (v) vis.clear(i);
                    else vis.set(i);
                }
            }

            if (clientStackChanged || visibilityChanged) {
                if (receivers == null) receivers = getReceivers(entity);
                PacketSync pkt = PacketSync.S2CPack(entity, i, stack1, visibilityChanged ? (v ? 1 : 0) : -1);
                for (EntityPlayer receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
                }
            }

            baubles.getDirty().clear(i);
        }
    }

    EntityLivingBase getEntity();

    default List<ItemStack> getBaubleStacks() {
        return new ArrayList<>();
    }

    default BitSet getVisibilities() {
        return new BitSet();
    }

    static HashSet<EntityPlayer> getReceivers(EntityLivingBase entity) {
        HashSet<EntityPlayer> receivers = new HashSet<>(((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity));
        if (entity instanceof EntityPlayer) receivers.add((EntityPlayer) entity);
        return receivers;
    }

    static void forceSync(EntityPlayer player) {
        Set<EntityPlayer> receivers = null;
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (!baubles.getDirty().get(i)) continue;

            ItemStack stack = baubles.getStackInSlot(i);

            if (receivers == null) receivers = getReceivers(player);
            PacketSync pkt = PacketSync.S2CPack(player, i, stack, baubles.getVisible(i) ? 1 : 0);
            for (EntityPlayer receiver : receivers) {
                PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
            }

            baubles.getDirty().clear(i);
        }
    }
}
