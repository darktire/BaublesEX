package baubles.mixin.late.improvedbackpacks;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.poopycoders.improvedbackpacks.client.LayerBackpack;
import ru.poopycoders.improvedbackpacks.init.ModItems;

@Mixin(LayerBackpack.class)
public class MixinLayerBackpack {
    @Redirect(
            method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        ItemStack stack = HookHelper.getStack(instance, ModItems.ENDER_BACKPACK);
        if (stack.isEmpty()) HookHelper.getStack(instance, ModItems.BACKPACK);
        return stack;
    }
}
