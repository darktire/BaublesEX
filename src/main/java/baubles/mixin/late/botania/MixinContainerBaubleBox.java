package baubles.mixin.late.botania;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.gui.box.ContainerBaubleBox;

import java.util.List;

@Mixin(ContainerBaubleBox.class)
public abstract class MixinContainerBaubleBox extends Container {
    @Inject(method = "setAll", at = @At("HEAD"), cancellable = true)
    public void injected(List<ItemStack> l, CallbackInfo ci) {
        super.setAll(l);
        ci.cancel();
    }
}
