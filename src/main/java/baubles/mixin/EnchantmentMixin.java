package baubles.mixin;

import baubles.api.registries.ItemsData;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.enchantment.EnumEnchantmentType$12")
public class EnchantmentMixin{

    @Inject(method = "canEnchantItem", at = @At("HEAD"), cancellable = true)
    private void canEnchantBaubles(Item itemIn, CallbackInfoReturnable<Boolean> cir) {
        boolean isPumpkin = itemIn instanceof ItemBlock && ((ItemBlock)itemIn).getBlock() instanceof BlockPumpkin;
        boolean flag = itemIn instanceof ItemArmor || itemIn instanceof ItemElytra || itemIn instanceof ItemSkull || isPumpkin;
        boolean isBauble = ItemsData.isBauble(itemIn);
        cir.setReturnValue(flag || isBauble);
    }
}
