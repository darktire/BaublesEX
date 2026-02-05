package baubles.common.items;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Map;

public abstract class BaubleVanilla implements IBauble {

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        update(entity);
    }

    protected abstract Map<EntityLivingBase, Integer> getEquipSlotMap();

    protected int update(EntityLivingBase entity) {
        return this.update(entity, false);
    }

    protected int update(EntityLivingBase entity, boolean using) {
        Map<EntityLivingBase, Integer> map = this.getEquipSlotMap();
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return -1;

        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack1 = baubles.getStackInSlot(i);
            if (check(stack1, using)) {
                map.put(entity, i);
                return i;
            }
        }
        map.put(entity, -1);
        return -1;
    }

    protected abstract boolean check(ItemStack stack, boolean using);
}
