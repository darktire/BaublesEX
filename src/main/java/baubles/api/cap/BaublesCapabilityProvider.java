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
    public static final String BAUBLE_KEY = "Bauble";
    private final ItemStack stack;
    private final BaublesWrapper wrapper;

    public BaublesCapabilityProvider(ItemStack itemStack) {
        this.stack = itemStack;
        this.wrapper = BaubleItemsContent.itemToBauble(itemStack.getItem());
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
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) nbt = new NBTTagCompound();
        if (!nbt.hasKey(BAUBLE_KEY)) {
            BaubleTypeEx type = wrapper.getBaubleTypeEx();
            if (type != null) {
                nbt.setString(BAUBLE_KEY, type.getTypeName());
            }
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        stack.setTagCompound(compound);
    }
}
