package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.registries.TypeData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Arrays;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_BAUBLES;

public class BaublesContainerProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private final BaublesContainer container;
    private final AbstractAttributeMap attrMap;

    public BaublesContainerProvider(EntityLivingBase entity) {
        this.container = new BaublesContainer(entity);
        AttributeManager.attachAttributes(entity, this.container);
        this.attrMap = entity.getAttributeMap();
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
        NBTTagCompound nbt = this.container.serializeNBT();
        // modifier persistence
        NBTTagCompound modifier = new NBTTagCompound();
        for (BaubleTypeEx type : TypeData.sortedList()) {
            AdvancedInstance instance = AttributeManager.getInstance(attrMap, type);
            int[] intArr = {
                    (int) instance.getAnonymousModifier(0),
                    (int) instance.getAnonymousModifier(1),
                    (int) instance.getAnonymousModifier(2)
            };
            if (!Arrays.stream(intArr).allMatch(num -> num == 0)) {
                modifier.setIntArray(type.getName(), intArr);
            }
        }
        nbt.setTag("Anonymous", modifier);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Anonymous")) {
            NBTTagCompound modifier = nbt.getCompoundTag("Anonymous");
            for (String typeName : modifier.getKeySet()) {
                BaubleTypeEx type = TypeData.getTypeByName(typeName);
                if (type == null) continue;
                NBTBase base = modifier.getTag(typeName);
                if (base instanceof NBTTagIntArray) {
                    int[] input = ((NBTTagIntArray) base).getIntArray();
                    for (int i = 0; i < 3; i++) {
                        if (input[i] == 0) continue;
                        AttributeManager.getInstance(attrMap, type).applyAnonymousModifier(i, input[i]);
                    }
                }
            }
        }
        this.container.setRespawnTask(() -> {
            this.container.deserializeNBT(nbt);
            this.container.dropItems();
        });
    }
}
