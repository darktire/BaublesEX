package baubles.mixin.late.enigmaticlegacy;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import keletu.enigmaticlegacy.EnigmaticLegacy;
import keletu.enigmaticlegacy.event.EnigmaticEvents;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnigmaticEvents.class, remap = false)
public class MixinEnigmaticEvents {

    @Redirect(method = "onPlayerJoin", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect1(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, EnigmaticLegacy.cursedRing);
    }

    @Redirect(method = "onPlayerJoin", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;setStackInSlot(ILnet/minecraft/item/ItemStack;)V"))
    private static void redirect1(IBaublesItemHandler instance, int i, ItemStack stack) {
        int slot = instance.indexOf(HookHelper.getMainType(stack), 0);
        instance.setStackInSlot(slot, stack);
    }
}
