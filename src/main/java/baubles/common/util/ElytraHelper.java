package baubles.common.util;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

public class ElytraHelper {
    public static ItemStack elytraInBaubles(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (Config.ModItems.elytraBauble && entity instanceof EntityPlayer && ((!(stack.getItem() instanceof ItemElytra) || stack.getItem() instanceof ItemElytra && !ItemElytra.isUsable(stack)))) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
            if (baubles != null) {
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack stack1 = baubles.getStackInSlot(i);
                    if (stack1.getItem() instanceof ItemElytra && ItemElytra.isUsable(stack1)) return stack1;
                }
            }
        }
        return stack;
    }
}
