package baubles.mixin.late.ancientspellcraft;

import baubles.api.cap.IBaublesItemHandler;
import baubles.compat.wizardry.WizardryHelper;
import baubles.util.HookHelper;
import com.windanesz.ancientspellcraft.integration.baubles.ASBaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ASBaublesIntegration.class, remap = false)
public class MixinBaubles {
    @Inject(method = "getEquippedArtefactStacks", at = @At("HEAD"), cancellable = true)
    private static void inject1(EntityPlayer player, ItemArtefact.Type[] types, CallbackInfoReturnable<List<ItemStack>> cir) {
        cir.setReturnValue(WizardryHelper.artefacts2Baubles(player, types));
    }

    @Redirect(method = "getBeltSlotItemStack", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemArtefact.class);
    }

    @Inject(method = "setArtefactToSlot(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lelectroblob/wizardry/item/ItemArtefact$Type;)V", at = @At("HEAD"), cancellable = true)
    private static void inject2(EntityPlayer player, ItemStack stack, ItemArtefact.Type type, CallbackInfo ci) {
        HookHelper.tryEquipping(player, stack);
        ci.cancel();
    }

    @Inject(method = "setArtefactToSlot(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lelectroblob/wizardry/item/ItemArtefact$Type;I)V", at = @At("HEAD"), cancellable = true)
    private static void inject3(EntityPlayer player, ItemStack stack, ItemArtefact.Type type, int slotId, CallbackInfo ci) {
        HookHelper.tryEquipping(player, stack);
        ci.cancel();
    }
}
