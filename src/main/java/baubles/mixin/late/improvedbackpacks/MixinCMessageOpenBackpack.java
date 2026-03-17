package baubles.mixin.late.improvedbackpacks;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.poopycoders.improvedbackpacks.init.ModItems;
import ru.poopycoders.improvedbackpacks.network.client.CMessageOpenBackpack;

@Mixin(value = CMessageOpenBackpack.Handler.class, remap = false)
public class MixinCMessageOpenBackpack {
    @Redirect(
            method = "lambda$onMessage$0",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        ItemStack stack = HookHelper.getStack(instance, ModItems.ENDER_BACKPACK);
        if (stack.isEmpty()) HookHelper.getStack(instance, ModItems.BACKPACK);
        return stack;
    }
}
