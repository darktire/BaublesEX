package baubles.mixin.early.vanilla;

import baubles.api.BaublesApi;
import baubles.common.config.Config;
import baubles.util.CommonHelper;
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

@Mixin(ItemStack.class)
public abstract class MixinStack {

    @Inject(method = "useItemRightClick", at = @At("RETURN"), cancellable = true)
    private void playerRightClickItem(World worldIn, EntityPlayer playerIn, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (!Config.rightClick) return;
        if (cir.getReturnValue().getType() != EnumActionResult.SUCCESS) {
            ItemStack heldItem = (ItemStack) (Object) this;
            if (Config.getBlacklist().contains(heldItem.getItem()) || !BaublesApi.isBauble(heldItem)) return;
            if (CommonHelper.tryEquipping(playerIn, hand, heldItem)) {
                cir.setReturnValue(new ActionResult<>(EnumActionResult.SUCCESS, heldItem));
            }
        }
    }
}
