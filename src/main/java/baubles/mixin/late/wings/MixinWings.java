package baubles.mixin.late.wings;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import me.paulf.wings.server.apparatus.FlightApparatus;
import me.paulf.wings.server.config.WingsConfig;
import me.paulf.wings.util.CapabilityHolder;
import me.paulf.wings.util.Util;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(targets = "me.paulf.wings.server.apparatus.FlightApparatuses$WingedPresentState", remap = false)
public abstract class MixinWings extends CapabilityHolder.PresentState<ItemStack, FlightApparatus> {
    protected MixinWings(Capability<FlightApparatus> capability) {
        super(capability);
    }

    @Inject(method = "find", at = @At("HEAD"), cancellable = true)
    private void findWings(EntityLivingBase player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (this.has(stack, null)) {
            cir.setReturnValue(stack);
            return;
        }
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        if (baubles != null) {
            for (int i = 0; i < baubles.getSlots(); i++) {
                ItemStack stack1 = baubles.getStackInSlot(i);
                if (this.has(stack1, null)) {
                    cir.setReturnValue(stack1);
                    return;
                }
                if (!stack1.isEmpty() && Arrays.asList(WingsConfig.wearObstructions).contains(Util.getName(stack1.getItem()).toString())) {
                    break;
                }
            }
        }
        cir.setReturnValue(ItemStack.EMPTY);
    }
}
