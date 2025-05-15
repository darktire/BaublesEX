package baubles.api.cap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider {
    private final Item item;

    public BaublesCapabilityProvider(ItemStack itemStack) {
        this.item = itemStack.getItem();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE
                ? CAPABILITY_ITEM_BAUBLE.cast(BaubleItemRegister.getIBauble(item))
                : null;
    }
}
