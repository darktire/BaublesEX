package baubles.common.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IBaublesSync {

    default void syncBaubles() {
        Set<EntityPlayer> receivers = null;
        EntityLivingBase entity = getEntity();
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        for (int i = 0, slots = baubles.getSlots(); i < slots; i++) {
            if (!baubles.getDirty().get(i)) continue;

            ItemStack stack = baubles.getStackInSlot(i);
            ItemStack stack1 = getBaubleStacks().get(i);

            boolean clientStackChanged = false;
            if (!ItemStack.areItemStacksEqual(stack1, stack)) {
                clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(stack1, stack);
                stack1 = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
                getBaubleStacks().set(i, stack1);
            }

            boolean v = baubles.getVisible(i);
            boolean visibilityChanged = false;
            if (getVisibilities() != null) {
                boolean v1 = getVisibilities().get(i);

                visibilityChanged = v == v1;
                if (visibilityChanged) {
                    if (v) getVisibilities().clear(i);
                    else getVisibilities().set(i);
                }
            }

            if (clientStackChanged || visibilityChanged) {
                if (receivers == null) {
                    receivers = new HashSet<>(((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity));
                    if (entity instanceof EntityPlayer) receivers.add((EntityPlayer) entity);
                }
                PacketSync pkt = PacketSync.severPack(entity, i, stack1, v);
                for (EntityPlayer receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
                }
            }

            baubles.getDirty().clear(i);
        }
    }

    EntityLivingBase getEntity();

    List<ItemStack> getBaubleStacks();

    BitSet getVisibilities();
}
