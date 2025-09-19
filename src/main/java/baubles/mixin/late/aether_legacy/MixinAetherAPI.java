package baubles.mixin.late.aether_legacy;

import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.api.accessories.AetherAccessory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AetherAPI.class, remap = false)
public class MixinAetherAPI {
    @Shadow
    private static IForgeRegistry<AetherAccessory> iAccessoryRegistry;

    @Inject(method = "isAccessory", at = @At("HEAD"), cancellable = true)
    public void fix(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {//has potential risks, crash with tcon
        cir.setReturnValue(iAccessoryRegistry.containsKey(new ResourceLocation(stack.getItem().getRegistryName() + "_meta_" + (stack.isItemStackDamageable() ? 0 : stack.getMetadata()))));
    }
}
