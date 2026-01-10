package baubles.mixin.late.backpacked;

import baubles.api.IBauble;
import com.mrcrayfish.backpacked.item.BaubleBackpackItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaubleBackpackItem.class, remap = false)
@Implements(@Interface(iface = IBauble.class, prefix = "bs$"))
public class MixinBaubleBackpackItem {

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    void inject(World worldIn, EntityPlayer playerIn, EnumHand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        cir.setReturnValue(new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn)));
    }

    @Unique
    public boolean bs$canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return false;
    }

}
