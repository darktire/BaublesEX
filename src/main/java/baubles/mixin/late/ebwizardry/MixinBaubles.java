package baubles.mixin.late.ebwizardry;

import baubles.compat.wizardry.WizardryHelper;
import electroblob.wizardry.integration.baubles.WizardryBaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = WizardryBaublesIntegration.class, remap = false)
public class MixinBaubles {
    @Inject(method = "getEquippedArtefacts", at = @At("HEAD"), cancellable = true)
    private static void inject(EntityPlayer player, ItemArtefact.Type[] types, CallbackInfoReturnable<List<ItemArtefact>> cir) {
        cir.setReturnValue((WizardryHelper.artefacts2Baubles(player, types).stream().map(stack -> ((ItemArtefact) stack.getItem()))).collect(Collectors.toList()));
    }
}
