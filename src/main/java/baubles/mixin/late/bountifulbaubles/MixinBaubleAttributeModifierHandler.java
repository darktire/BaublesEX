package baubles.mixin.late.bountifulbaubles;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import cursedflames.bountifulbaubles.baubleeffect.BaubleAttributeModifierHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BaubleAttributeModifierHandler.class, remap = false)
public class MixinBaubleAttributeModifierHandler {
    @Inject(method = "baubleModified", at = @At(value = "INVOKE", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;generateModifier(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void inject(ItemStack stack, EntityLivingBase entity, boolean equip, CallbackInfo ci) {
        if (!entity.world.isRemote) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
            int index = baubles.indexOf(stack.getItem(), 0);
            baubles.stx.markDirty(index);
        }
    }
}
