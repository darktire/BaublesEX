package baubles.mixin.late.atum;

import baubles.api.BaublesApi;
import baubles.util.HookHelper;
import com.teammetallurgy.atum.items.ItemAmulet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemAmulet.class, remap = false)
public class MixinItemAmulet {
    @Inject(method = "getAmulet", at = @At("HEAD"), cancellable = true)
    private static void inject(EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(HookHelper.getStack(BaublesApi.getBaublesHandler((EntityLivingBase) player), ItemAmulet.class));
    }
}
