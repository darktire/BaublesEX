package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BaublesCapabilities {
    /**
     * Access to the player's baubles' capability.
     */
    @CapabilityInject(IBaublesItemHandler.class)
    public static Capability<IBaublesItemHandler> CAPABILITY_BAUBLES;

    /**
     * Access to the bauble items capability.
     **/
    @CapabilityInject(IBauble.class)
    public static Capability<IBauble> CAPABILITY_ITEM_BAUBLE;

    public static class CapabilityBaubles<T extends IBaublesItemHandler> implements IStorage<IBaublesItemHandler> {

        @Override
        public NBTBase writeNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, EnumFacing side, NBTBase nbt) {
        }
    }

    public static class CapabilityItemBaubleStorage implements IStorage<IBauble> {

        @Override
        public NBTBase writeNBT(Capability<IBauble> capability, IBauble instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            if (instance != null) {
                BaubleTypeEx baubleTypeEx = instance.getBaubleTypeEx();
                if (baubleTypeEx != null) {
                    compound.setString("bauble", baubleTypeEx.getTypeName());
                } else {
                    compound.setString("bauble", instance.getBaubleType().getTypeName());
                }
            }
            return compound;
        }

        @Override
        public void readNBT(Capability<IBauble> capability, IBauble instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            String typeName = compound.getString("type");
            instance.setType(typeName);
        }
    }
}
