package baubles.util;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.items.BaubleElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

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

    public static boolean tryEquipping(EntityPlayer playerIn, ItemStack stack) {
        IBauble bauble = BaublesApi.toBauble(stack);
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        for (BaubleTypeEx type : bauble.getTypes(stack)) {
            for (int i = 0, s = baubles.getSlots(); i < s; i++) {
                boolean match = baubles.getTypeInSlot(i) == type || type == TypesData.Preset.TRINKET;
                if (match && baubles.getStackInSlot(i).isEmpty()) {
                    baubles.setStackInSlot(i, stack.copy());
                    if (!playerIn.capabilities.isCreativeMode) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                    }
                    bauble.onEquipped(stack, playerIn);
                    return true;
                }
            }
        }
        return false;
    }
}
