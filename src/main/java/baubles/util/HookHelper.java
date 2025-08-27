package baubles.util;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

public class HookHelper {
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

    private static final Map<String, Boolean> isModLoaded = new HashMap<>();
    public static boolean isModLoaded(String modName) {
        Boolean flag = isModLoaded.get(modName);
        if (flag == null) {
            flag = Loader.instance().getModList().stream().anyMatch(mod -> mod.getName().equals(modName));
            isModLoaded.put(modName, flag);
        }
        return flag;
    }
}
