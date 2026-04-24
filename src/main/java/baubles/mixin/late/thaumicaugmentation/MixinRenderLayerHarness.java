package baubles.mixin.late.thaumicaugmentation;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thecodex6824.thaumicaugmentation.api.TAItems;
import thecodex6824.thaumicaugmentation.client.renderer.layer.RenderLayerHarness;

@Mixin(value = RenderLayerHarness.class, remap = false)
public class MixinRenderLayerHarness {
    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, stack -> stack.getItem() == TAItems.THAUMOSTATIC_HARNESS || stack.getItem() == TAItems.ELYTRA_HARNESS);
    }
}
