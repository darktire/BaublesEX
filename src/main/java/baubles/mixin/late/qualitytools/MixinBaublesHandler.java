package baubles.mixin.late.qualitytools;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.registries.TypeData;
import com.google.common.collect.Multimap;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import com.tmtravlr.qualitytools.baubles.BaublesHandler;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaublesHandler.class, remap = false)
public class MixinBaublesHandler {

    @Unique
    private static final String BS$PREFIX = "baubles_";

    @Inject(method = "applyAttributesFromBaubles", at = @At("HEAD"), cancellable = true)
    void inject1(EntityPlayer player, Multimap<String, AttributeModifier> modifiersToRemove, CallbackInfo ci) {

        BaublesApi.applyToBaubles(player, stack -> {
            if (stack.isEmpty()) return;
            QualityToolsHelper.applyAttributesForSlot(player, stack, "baubles_trinket", modifiersToRemove);
            for (BaubleTypeEx type : BaublesApi.toBauble(stack).getTypes(stack)) {
                String slotName = BS$PREFIX + type.getName();
                QualityToolsHelper.applyAttributesForSlot(player, stack, slotName, modifiersToRemove);
            }
        });
        ci.cancel();
    }

    @Inject(method = "canEquipBauble", at = @At("HEAD"), cancellable = true)
    void inject2(ItemStack stack, String slotName, CallbackInfoReturnable<Boolean> cir) {
        if (!slotName.startsWith(BS$PREFIX)) {
            cir.setReturnValue(false);
            return;
        }
        BaubleTypeEx type = TypeData.getTypeByName(slotName.substring(BS$PREFIX.length()));
        if (type == null) {
            cir.setReturnValue(false);
            return;
        }
        IBauble bauble = BaublesApi.toBauble(stack);
        if (bauble == null) {
            cir.setReturnValue(false);
            return;
        }
        if (type == TypeData.Preset.TRINKET) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(bauble.getTypes(stack).contains(type));
    }
}
