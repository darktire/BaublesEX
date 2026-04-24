package baubles.mixin.late.thaumicaugmentation;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thecodex6824.thaumicaugmentation.api.augment.CapabilityAugmentableItem;
import thecodex6824.thaumicaugmentation.client.internal.TAHooksClient;
import thecodex6824.thaumicaugmentation.common.item.trait.IElytraCompat;

@Mixin(value = TAHooksClient.class, remap = false)
public class MixinTAHooksClient {
    @Redirect(method = {"checkElytra", "shouldRenderCape"}, at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect0(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, IElytraCompat.class);
    }


    @Redirect(method =  "checkPlayerSprintState", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect1(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, stack -> stack.hasCapability(CapabilityAugmentableItem.AUGMENTABLE_ITEM, null));
    }
}
