package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleAmulet;
import baubles.api.BaubleType;
import baubles.util.HookHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BaubleAmulet.class, remap = false)
public abstract class MixinSacrificialAmulet {
    @Redirect(method = "onLivingDeath", at = @At(value = "INVOKE", target = "Lbaubles/api/BaubleType;getValidSlots()[I"))
    private static int[] redirect(BaubleType instance, @Local(ordinal = 0) EntityPlayer player) {
        return HookHelper.getValidSlots(ModItems.SACRIFICIAL_AMULET, player);
    }

}
