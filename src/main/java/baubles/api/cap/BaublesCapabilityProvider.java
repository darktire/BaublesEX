package baubles.api.cap;

import baubles.api.IWrapper;
import baubles.api.registries.ItemsData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider {
    private final IWrapper wrapper;
    private final ICapabilityProvider other;

    public BaublesCapabilityProvider(ItemStack stack, ICapabilityProvider other) {
        this.wrapper = ItemsData.toBauble(stack);
        this.other = other;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE || (this.other != null && this.other.hasCapability(capability, facing));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE ? CAPABILITY_ITEM_BAUBLE.cast(wrapper) : (this.other == null ? null : this.other.getCapability(capability, facing));
    }
}
