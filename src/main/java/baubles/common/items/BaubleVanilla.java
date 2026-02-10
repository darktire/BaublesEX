package baubles.common.items;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.UUID;

public abstract class BaubleVanilla implements IBauble {

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    protected abstract Map<UUID, Integer> getEquipSlotMap();

    protected int update(EntityLivingBase entity) {
        return this.update(entity, false);
    }

    protected int update(EntityLivingBase entity, boolean using) {
        Map<UUID, Integer> map = this.getEquipSlotMap();
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return -1;

        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack1 = baubles.getStackInSlot(i);
            if (check(stack1, using)) {
                map.put(entity.getUniqueID(), i);
                return i;
            }
        }
        map.put(entity.getUniqueID(), -1);
        return -1;
    }

    protected abstract boolean check(ItemStack stack, boolean using);
}
