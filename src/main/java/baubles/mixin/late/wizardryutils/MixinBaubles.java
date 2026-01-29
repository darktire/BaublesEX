package baubles.mixin.late.wizardryutils;

import baubles.compat.wizardry.WizardryHelper;
import baubles.util.CommonHelper;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BaublesIntegration.class, remap = false)
public class MixinBaubles {
    @Inject(method = "getEquippedArtefactStacks", at = @At("HEAD"), cancellable = true)
    private static void inject1(EntityPlayer player, Object[] types, CallbackInfoReturnable<List<ItemStack>> cir) {
        cir.setReturnValue(WizardryHelper.artefacts2Baubles(player, (ItemArtefact.Type[]) types));
    }

    @Inject(method = "setArtefactToSlot(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lelectroblob/wizardry/item/ItemArtefact$Type;)V", at = @At("HEAD"), cancellable = true)
    private static void inject2(EntityPlayer player, ItemStack stack, ItemArtefact.Type type, CallbackInfo ci) {
        CommonHelper.tryEquipping(player, stack);
        ci.cancel();
    }

    @Inject(method = "setArtefactToSlot(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lelectroblob/wizardry/item/ItemArtefact$Type;I)V", at = @At("HEAD"), cancellable = true)
    private static void inject3(EntityPlayer player, ItemStack stack, ItemArtefact.Type type, int slotId, CallbackInfo ci) {
        CommonHelper.tryEquipping(player, stack);
        ci.cancel();
    }
}
