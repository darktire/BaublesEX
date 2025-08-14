package baubles.mixin;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.Baubles;
import baubles.common.util.BaublesRegistry;
import net.minecraft.entity.EntityLivingBase;
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
    public void playerRightClickItem(World worldIn, EntityPlayer playerIn, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (playerIn.isSneaking()) return;
        if (cir.getReturnValue().getType() == EnumActionResult.SUCCESS) return;
        ItemStack heldItem = (ItemStack) (Object) this;
        if (Baubles.config.getBlacklist().contains(heldItem.getItem())) return;
        if (!BaublesApi.isBauble(heldItem)) return;
        IBauble bauble = BaublesApi.toBauble(heldItem);
        BaubleTypeEx type = bauble.getBaubleTypeEx();
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (baubles.getTypeInSlot(i) != type && type != BaublesRegistry.TRINKET) continue;
            if (baubles.getStackInSlot(i).isEmpty()) {
                ItemStack itemStack = heldItem.copy();
                baubles.setStackInSlot(i, itemStack);
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                }
                bauble.onEquipped(itemStack, playerIn);
                break;
            }
        }
        cir.setReturnValue(new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand)));
    }
}
