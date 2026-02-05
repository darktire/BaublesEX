package baubles.mixin.early.vanilla;

import baubles.common.items.BaubleTotem;
import baubles.util.HookHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLiving {

    @Redirect(method = "updateElytra", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack injected(EntityLivingBase entity, EntityEquipmentSlot slot) {
        return HookHelper.universalCondition(entity, slot, true);
    }

    @Redirect(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;getHeldItem(Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack redirect(EntityLivingBase entity, EnumHand hand) {
        ItemStack stack = entity.getHeldItem(hand);

        if (hand == EnumHand.OFF_HAND) {
            if (stack.getItem() != Items.TOTEM_OF_UNDYING && BaubleTotem.isWearing(entity)) {
                return BaubleTotem.getWearing(entity);
            }
        }

        return stack;
    }
}
