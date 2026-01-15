package baubles.api.cap;

import baubles.api.BaublesWrapper;
import baubles.api.IWrapper;
import baubles.api.registries.ItemsData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.ref.WeakReference;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider {
    private final WeakReference<ItemStack> ref;
    private final ICapabilityProvider other;
    private boolean initialized = false;
    private IWrapper wrapper;

    public BaublesCapabilityProvider(ItemStack stack, ICapabilityProvider other) {
        this.ref = new WeakReference<>(stack);
        this.other = other;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        this.initialize();
        return capability == CAPABILITY_ITEM_BAUBLE ? this.isDefined() : this.other != null && this.other.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        this.initialize();
        return capability == CAPABILITY_ITEM_BAUBLE ? (this.isDefined() ? CAPABILITY_ITEM_BAUBLE.cast(wrapper) : null) : (this.other == null ? null : this.other.getCapability(capability, facing));
    }

    private void initialize() {
        if (this.initialized) return;
        this.initialized = true;
        ItemStack stack = this.ref.get();
        this.wrapper = stack == null ? null : ItemsData.toBauble(stack);
    }

    private boolean isDefined() {
        ItemStack stack = this.ref.get();
        if (stack == null) return false;
        return ItemsData.isBauble(stack) && !BaublesWrapper.Addition.isRemoved(stack);
    }
}
