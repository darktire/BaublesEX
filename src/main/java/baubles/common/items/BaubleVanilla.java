package baubles.common.items;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public abstract class BaubleVanilla implements IBauble {

    protected static final class WearingState {
        private final Map<UUID, Integer> client = new WeakHashMap<>();
        private final Map<UUID, Integer> server = new WeakHashMap<>();

        private Map<UUID, Integer> values(EntityLivingBase entity) {
            return entity.world.isRemote ? this.client : this.server;
        }

        private Integer get(EntityLivingBase entity) {
            return this.values(entity).get(entity.getUniqueID());
        }

        private void put(EntityLivingBase entity, int slot) {
            this.values(entity).put(entity.getUniqueID(), slot);
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    protected abstract WearingState getWearingState();

    protected final boolean hasWearing(EntityLivingBase entity) {
        Integer slot = this.getWearingState().get(entity);
        return (slot != null ? slot : this.update(entity)) != -1;
    }

    protected final ItemStack borrow(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return ItemStack.EMPTY;

        int slot = this.getWearingSlot(entity, baubles);
        return slot != -1 ? baubles.getStackInSlot(slot) : ItemStack.EMPTY;
    }

    protected final ItemStack take(EntityLivingBase entity, int amount) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return ItemStack.EMPTY;

        int slot = this.getWearingSlot(entity, baubles);
        if (slot == -1) return ItemStack.EMPTY;

        ItemStack stack = baubles.extractItem(slot, amount, false);
        this.update(entity, baubles, false);
        return stack;
    }

    private int getWearingSlot(EntityLivingBase entity, IBaublesItemHandler baubles) {
        Integer slot = this.getWearingState().get(entity);
        return slot != null ? slot : this.update(entity, baubles, false);
    }

    protected int update(EntityLivingBase entity) {
        return this.update(entity, false);
    }

    protected int update(EntityLivingBase entity, boolean using) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) {
            this.getWearingState().put(entity, -1);
            return -1;
        }

        return this.update(entity, baubles, using);
    }

    private int update(EntityLivingBase entity, IBaublesItemHandler baubles, boolean using) {
        WearingState wearing = this.getWearingState();

        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack1 = baubles.getStackInSlot(i);
            if (check(stack1, using)) {
                wearing.put(entity, i);
                return i;
            }
        }
        wearing.put(entity, -1);
        return -1;
    }

    protected abstract boolean check(ItemStack stack, boolean using);
}
