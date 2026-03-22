package baubles.mixin.late.uniquee;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uniquebase.utils.MiscUtil;

@Mixin(value = MiscUtil.class, remap = false)
public class MixinMiscUtil {

    @Inject(
        method = "getEnchantedItem(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/EntityLivingBase;)Lit/unimi/dsi/fastutil/objects/Object2IntMap$Entry;",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void getEnchantedItem(Enchantment enchantment, EntityLivingBase base, CallbackInfoReturnable<Object2IntMap.Entry<EntityEquipmentSlot>> cir) {
        if (cir.getReturnValue().getIntValue() > 0) return;

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(base);
        if (baubles == null) return;

        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            int level = MiscUtil.getEnchantmentLevel(enchantment, stack);
            if (level > 0) {
                cir.setReturnValue(new AbstractObject2IntMap.BasicEntry<>(EntityEquipmentSlot.OFFHAND, level));
                return;
            }
        }
    }
}
