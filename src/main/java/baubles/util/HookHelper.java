package baubles.util;

import baubles.common.config.Config;
import baubles.common.items.BaubleElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

public class HookHelper {

    public static ItemStack universalCondition(EntityLivingBase entity, EntityEquipmentSlot slot, boolean using) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        boolean unusable = using && stack.getItem() instanceof ItemElytra && !ItemElytra.isUsable(stack);
        boolean toFind = !(stack.getItem() instanceof ItemElytra) || unusable;
        if (Config.ModItems.elytraBauble && toFind && BaubleElytra.isWearing(entity, using)) {
            return BaubleElytra.getWearing(entity, using);
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

    public static boolean checkResource() {
        try {
            Class.forName("com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
