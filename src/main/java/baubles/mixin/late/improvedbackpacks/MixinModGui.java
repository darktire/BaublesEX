package baubles.mixin.late.improvedbackpacks;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.poopycoders.improvedbackpacks.init.ModGui;
import ru.poopycoders.improvedbackpacks.init.ModItems;

@Mixin(value = ModGui.class, remap = false)
public class MixinModGui {
    @Redirect(
            method = "getClientGuiElement",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack redirect0(IBaublesItemHandler instance, int i) {
        ItemStack stack = HookHelper.getStack(instance, ModItems.ENDER_BACKPACK);
        if (stack.isEmpty()) HookHelper.getStack(instance, ModItems.BACKPACK);
        return stack;
    }

    @Redirect(
            method = "getServerGuiElement",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack redirect1(IBaublesItemHandler instance, int i) {
        ItemStack stack = HookHelper.getStack(instance, ModItems.ENDER_BACKPACK);
        if (stack.isEmpty()) HookHelper.getStack(instance, ModItems.BACKPACK);
        return stack;
    }
}
