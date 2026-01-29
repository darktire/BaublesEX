package baubles.api.cap;

import baubles.api.AbstractWrapper;
import baubles.api.registries.ItemData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.ref.WeakReference;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider {
    private final WeakReference<ItemStack> ref;
    private final ICapabilityProvider other;

    public BaublesCapabilityProvider(ItemStack stack, ICapabilityProvider other) {
        this.ref = new WeakReference<>(stack);
        this.other = other;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE ? this.isDefined() : this.other != null && this.other.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE ? (this.isDefined() ? CAPABILITY_ITEM_BAUBLE.cast(ItemData.toBauble(this.ref.get())) : null) : (this.other == null ? null : this.other.getCapability(capability, facing));
    }

    private boolean isDefined() {
        ItemStack stack = this.ref.get();
        if (stack == null) return false;
        return ItemData.isBauble(stack) && !AbstractWrapper.Addition.isRemoved(stack);
    }
}
