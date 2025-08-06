package baubles.common.util;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

public class FlyingHelper {
    public static ItemStack elytraInBaubles(ItemStack stack, EntityLivingBase player) {
        if (Config.ModItems.elytraBauble && !(stack.getItem() instanceof ItemElytra) && !ItemElytra.isUsable(stack)) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler(player);
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
