package baubles.mixin.late.aether_legacy;

import baubles.api.BaublesApi;
import com.gildedgames.the_aether.containers.inventory.InventoryAccessories;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = InventoryAccessories.class, remap = false)
public class MixinInventoryAccessories {
    @Shadow
    public EntityPlayer player;

    @Inject(method = "wearingAccessory", at = @At(value ="RETURN", ordinal = 1), cancellable = true)
    public void injected(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(BaublesApi.getIndexInBaubles(this.player, stack.getItem(), 0) != -1);
    }
}
