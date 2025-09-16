package baubles.mixin.early.vanilla;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.util.ICapabilityModifiable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinStack {

    @Unique
    private static final int BS$PRE_INIT = LoaderState.PREINITIALIZATION.ordinal();

    @Shadow(remap = false)
    private CapabilityDispatcher capabilities;

    @Inject(method = "useItemRightClick", at = @At("RETURN"), cancellable = true)
    private void playerRightClickItem(World worldIn, EntityPlayer playerIn, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (cir.getReturnValue().getType() == EnumActionResult.SUCCESS || !Config.rightClick || playerIn.isSneaking()) return;
        ItemStack heldItem = (ItemStack) (Object) this;
        if (Config.getBlacklist().contains(heldItem.getItem()) || !BaublesApi.isBauble(heldItem)) return;
        IBauble bauble = BaublesApi.toBauble(heldItem);
        BaubleTypeEx type = bauble.getTypes(heldItem).get(0);
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (baubles.getTypeInSlot(i) != type && type != TypesData.Preset.TRINKET) continue;
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

    @Inject(method = "<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
    public void redirectBaubleCap(Item itemIn, int amount, int meta, NBTTagCompound capNBT, CallbackInfo ci) {
        if (Loader.instance().getLoaderState().ordinal() >= BS$PRE_INIT && this.capabilities != null) {
            ((ICapabilityModifiable) (Object) this.capabilities).patchCap((ItemStack) (Object) this);
        }
    }
}
