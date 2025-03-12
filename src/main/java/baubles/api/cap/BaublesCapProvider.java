package baubles.api.cap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class BaublesCapProvider implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {

    private final BaublesItemHandler baubles;

    public BaublesCapProvider(BaublesItemHandler baubles) {
        this.baubles = baubles;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == BaublesCapabilities.CAPABILITY_BAUBLES;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == BaublesCapabilities.CAPABILITY_BAUBLES) return (T) this.baubles;
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.baubles.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.baubles.deserializeNBT(nbt);
    }
}
