package baubles.api.cap;

import baubles.api.IBauble;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BaublesCapabilities {
    /**
     * Access to the player's baubles' capability.
     */
    @CapabilityInject(IBaublesModifiable.class)
    public static Capability<IBaublesModifiable> CAPABILITY_BAUBLES;

    /**
     * Access to the bauble items capability.
     **/
    @CapabilityInject(IBauble.class)
    public static Capability<IBauble> CAPABILITY_ITEM_BAUBLE;

    public static class CapabilityBaubles implements IStorage<IBaublesModifiable> {

        @Override
        public NBTBase writeNBT(Capability<IBaublesModifiable> capability, IBaublesModifiable instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IBaublesModifiable> capability, IBaublesModifiable instance, EnumFacing side, NBTBase nbt) {
        }
    }

    public static class CapabilityItemBaubleStorage implements IStorage<IBauble> {

        @Override
        public NBTBase writeNBT(Capability<IBauble> capability, IBauble instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IBauble> capability, IBauble instance, EnumFacing side, NBTBase nbt) {
        }
    }
}
