package baubles.mixin.late.uniqueeutil;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uniquebase.utils.MiscUtil;
import uniqueeutils.UniqueEnchantmentsUtils;
import uniqueeutils.handler.UtilsHandler;

@Mixin(value = UtilsHandler.class, remap = false)
public class MixinUtilsHandler {

    @Redirect(
            method = "onPlayerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                    remap = true,
                    ordinal = 0
            )
    )
    private ItemStack redirect(EntityPlayer player, EntityEquipmentSlot slotIn) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (stack.isEmpty()) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            int slot = 0;
            while (slot < baubles.getSlots()) {
                slot = baubles.indexOf(Items.ELYTRA, slot);
                if (slot == -1) break;
                else {
                    stack = baubles.getStackInSlot(slot++);
                }
                if (MiscUtil.getEnchantmentLevel(UniqueEnchantmentsUtils.ANEMOIS_FRAGMENT, stack) > 0) {
                    break;
                }
            }
        }
        return stack;
    }
}
