package baubles.mixin.late.uniquee;

import baubles.compat.uniquee.Util;
import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uniquebase.utils.EnchantmentContainer;

@Mixin(value = EnchantmentContainer.class, remap = false)
public class MixinEnchantmentContainer {

    @Shadow
    Object2IntLinkedOpenHashMap<Enchantment> combinedEnchantments;
    @Shadow private EntityLivingBase base;
    @Unique
    private Object2IntOpenHashMap<Enchantment> bs_enchantments;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void triggerBaubles(EntityLivingBase base, CallbackInfo ci) {
        bs_enchantments = Util.triggerBaubles(base);
    }

    @Inject(method = "triggerAll", at = @At("TAIL"))
    private void triggerBaubles(CallbackInfo ci) {
        if (combinedEnchantments == null) return;

        for (Object2IntMap.Entry<Enchantment> e : bs_enchantments.object2IntEntrySet()) {
            combinedEnchantments.addTo(e.getKey(), e.getIntValue());
        }
    }

    @Inject(method = "getEnchantment", at = @At("RETURN"), cancellable = true)
    private void getEnchantment(Enchantment ench, EntityEquipmentSlot slot, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 0) return;

        int level = bs_enchantments.getInt(ench);

//        if (ench == UniqueEnchantments.ICARUS_AEGIS && level > 0) {
//            tagCompound.setBoolean(tag, flying);
//            return;
//        }

        if (level > 0) {
            cir.setReturnValue(level);
        }
    }

    @Inject(method = "getEnchantedItem", at = @At("RETURN"), cancellable = true)
    private void getEnchantedItem(Enchantment ench, CallbackInfoReturnable<Object2IntMap.Entry<EntityEquipmentSlot>> cir) {
        if (cir.getReturnValue().getIntValue() > 0) return;

        int level = bs_enchantments.getInt(ench);
        if (level > 0) {
            cir.setReturnValue(new AbstractObject2IntMap.BasicEntry<>(EntityEquipmentSlot.OFFHAND, level));
        }
    }
}
