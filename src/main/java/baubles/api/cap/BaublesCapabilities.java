package baubles.api.cap;

import baubles.api.AbstractWrapper;
import net.minecraft.nbt.NBTBase;
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
    @CapabilityInject(AbstractWrapper.class)
    public static Capability<AbstractWrapper> CAPABILITY_ITEM_BAUBLE;

    public static class CapabilityBaubles implements IStorage<IBaublesItemHandler> {

        @Override
        public NBTBase writeNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, EnumFacing side, NBTBase nbt) {
        }
    }

    public static class CapabilityItemBaubleStorage implements IStorage<AbstractWrapper> {

        @Override
        public NBTBase writeNBT(Capability<AbstractWrapper> capability, AbstractWrapper instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<AbstractWrapper> capability, AbstractWrapper instance, EnumFacing side, NBTBase nbt) {
        }
    }
}
