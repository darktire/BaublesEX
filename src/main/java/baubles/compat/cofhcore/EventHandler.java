package baubles.compat.cofhcore;

import baubles.common.event.BaubleDropsEvent;
import baubles.compat.ModOnly;
import cofh.core.enchantment.EnchantmentSoulbound;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cofh.core.init.CoreEnchantments.soulbound;

@ModOnly("cofhcore")
public class EventHandler {

    @SubscribeEvent
    public static void onBaublesDrop(BaubleDropsEvent event) {
        ItemStack stack = event.getStack();
        int level = EnchantmentHelper.getEnchantmentLevel(soulbound, stack);
        if (level > 0) {
            if(!EnchantmentSoulbound.permanent) {
                if(MathHelper.RANDOM.nextInt(level + 1) == 0) {
                    ItemHelper.removeEnchantment(stack, soulbound);
                    if (level > 1) ItemHelper.addEnchantment(stack, soulbound, level - 1);
                }
            }
            event.noDrops();
        }
    }
}
