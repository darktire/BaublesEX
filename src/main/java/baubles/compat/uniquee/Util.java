package baubles.compat.uniquee;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import uniquebase.utils.MiscUtil;
import uniquee.UniqueEnchantments;

public class Util {
    public static ItemStack getIcarus(EntityLivingBase entity) {
        ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (MiscUtil.getEnchantmentLevel(UniqueEnchantments.ICARUS_AEGIS, stack) == 0) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
            if (baubles == null) return stack;
            for (int i = 0; i < baubles.getSlots(); i++) {
                stack = baubles.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                if (MiscUtil.getEnchantmentLevel(UniqueEnchantments.ICARUS_AEGIS, stack) > 0) {
                    return stack;
                }
            }
        }
        return stack;
    }

    public static Object2IntOpenHashMap<Enchantment> triggerBaubles(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return new Object2IntOpenHashMap<>();

        Object2IntOpenHashMap<Enchantment> merged = new Object2IntOpenHashMap<>();
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            for (Object2IntMap.Entry<Enchantment> e : MiscUtil.getEnchantments(stack).object2IntEntrySet()) {
                merged.addTo(e.getKey(), e.getIntValue());
            }
        }
        merged.defaultReturnValue(0);
        return merged;
    }
}
