package baubles.mixin.late.uniquee;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.compat.uniquee.Util;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uniquebase.utils.MiscUtil;
import uniquee.UniqueEnchantments;
import uniquee.handler.EntityEvents;

@Mixin(value = EntityEvents.class, remap = false)
public class MixinEntityEvents {

    @Redirect(
            method = "onEntityDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;shrink(I)V",
                    remap = true
            )
    )
    private void redirect(ItemStack stack, int amount, LivingDamageEvent event) {
        if (MiscUtil.getEnchantmentLevel(UniqueEnchantments.PHOENIX_BLESSING, stack) > 0) {
            stack.shrink(amount);
            return;
        }

        EntityLivingBase entity = event.getEntityLiving();
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(entity);
        if (handler == null) return;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack1 = handler.getStackInSlot(i);
            if (stack1.isEmpty()) continue;
            if (MiscUtil.getEnchantmentLevel(UniqueEnchantments.PHOENIX_BLESSING, stack1) > 0) {
                stack1.shrink(amount);
                return;
            }
        }
    }

    @Redirect(
            method = "onPlayerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                    remap = true,
                    ordinal = 1
            )
    )
    private ItemStack redirect0(EntityPlayer player, EntityEquipmentSlot slotIn) {
        return Util.getIcarus(player);
    }

    @Redirect(
            method = "onEntityDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                    remap = true,
                    ordinal = 0
            )
    )
    private ItemStack redirect1(EntityLivingBase entity, EntityEquipmentSlot slot) {
        return Util.getIcarus(entity);
    }

    @Redirect(
            method = "onLivingFall",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                    remap = true
            )
    )
    private ItemStack redirect2(EntityLivingBase entity, EntityEquipmentSlot slot) {
        return Util.getIcarus(entity);
    }

}
