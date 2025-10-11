package baubles.mixin.late.aether_legacy;

import com.gildedgames.the_aether.items.accessories.ItemAccessory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemAccessory.class, remap = false)
public class MixinItemAccessory {
    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    void injected(World worldIn, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        cir.setReturnValue(new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand)));
    }
}
