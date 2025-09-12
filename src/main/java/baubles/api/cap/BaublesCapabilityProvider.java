package baubles.api.cap;

import baubles.api.BaublesWrapper;
import baubles.api.registries.ItemsData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    private final BaublesWrapper wrapper;
    private final ICapabilityProvider other;

    public BaublesCapabilityProvider(ItemStack itemStack, ICapabilityProvider other) {
        this.wrapper = ItemsData.toBauble(itemStack.getItem());
        this.other = other;
    }

    public BaublesCapabilityProvider(Item item) {
        this.wrapper = ItemsData.toBauble(item);
        this.other = null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE || (this.other != null && this.other.hasCapability(capability, facing));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE ? CAPABILITY_ITEM_BAUBLE.cast(wrapper) : (this.other == null ? null : this.other.getCapability(capability, facing));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
//        if (!nbt.hasKey("type_name")) {
//            BaubleTypeEx type = wrapper.getBaubleType();
//            if (type != null) {
//                nbt.setString("type_name", type.getTypeName());
//            }
//        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {}
}
