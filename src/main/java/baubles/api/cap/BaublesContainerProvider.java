package baubles.api.cap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_BAUBLES;

public class BaublesContainerProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private final BaublesContainer container;

    public BaublesContainerProvider(EntityLivingBase entity) {
        this.container = new BaublesContainer(entity);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_BAUBLES;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CAPABILITY_BAUBLES ? CAPABILITY_BAUBLES.cast(this.container) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.container.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.container.deserializeNBT(nbt);
    }
}
