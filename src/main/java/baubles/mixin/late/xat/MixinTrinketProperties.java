package baubles.mixin.late.xat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xzeroair.trinkets.capabilities.CapabilityItemStackBase;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;

import javax.annotation.Nonnull;

@Mixin(value = TrinketProperties.class, remap = false)
public abstract class MixinTrinketProperties extends CapabilityItemStackBase<TrinketProperties, ItemStack> {
    public MixinTrinketProperties(ItemStack object) {
        super(object);
    }

    @Shadow public abstract NBTTagCompound saveToNBT(@Nonnull NBTTagCompound compound);

    @Shadow public abstract int getSlot();

    @Shadow public abstract void loadFromNBT(NBTTagCompound compound);

    @Inject(method = "saveToNBT", at = @At("HEAD"))
    void inject(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir) {
        ItemStack stack = this.getItemStack();
        if (!(stack.getItem() instanceof AccessoryBase)) return;
        NBTTagCompound compound1 = stack.getTagCompound();
        if (compound1 == null) return;
        loadFromNBT(compound1);
    }
}
