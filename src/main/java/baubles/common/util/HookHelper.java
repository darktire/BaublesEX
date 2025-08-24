package baubles.common.util;

import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.ItemsData;
import baubles.common.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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

    public static ICapabilityProvider redirectBaublesCap(ICapabilityProvider provider, ItemStack stack) {
        IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        if (!(bauble instanceof BaublesWrapper)) {
            if (!ItemsData.isBauble(stack.getItem())) {
                ItemsData.registerBauble(stack.getItem(), bauble.getBaubleType(stack).getExpansion());
            }
            return BaublesCapabilityProvider.getProvider(stack);
        }
        return provider;
    }
}
