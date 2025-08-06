package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.util.BaubleItemsContent;
import baubles.api.util.BaublesWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

public class BaublesCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    public static final String BAUBLE_TYPE = "Type";
    private final ItemStack stack;
    private final BaublesWrapper wrapper;

    public BaublesCapabilityProvider(ItemStack itemStack) {
        this.stack = itemStack;
        this.wrapper = BaubleItemsContent.toBauble(itemStack.getItem());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_ITEM_BAUBLE ? CAPABILITY_ITEM_BAUBLE.cast(wrapper) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (!nbt.hasKey(BAUBLE_TYPE)) {
            BaubleTypeEx type = wrapper.getBaubleTypeEx();
            if (type != null) {
                nbt.setString(BAUBLE_TYPE, type.getTypeName());
            }
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {}
}
