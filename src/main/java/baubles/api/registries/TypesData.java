package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypesData {
    private static final String DATA_ID = "baubles";
    private static final ResourceLocation BAUBLE_TYPE = new ResourceLocation(DATA_ID, "types");
    private static final ForgeRegistry<BaubleTypeEx> REGISTRY = (ForgeRegistry<BaubleTypeEx>) new RegistryBuilder<BaubleTypeEx>().setType(BaubleTypeEx.class).allowModification().setName(BAUBLE_TYPE).create();

    private static int sum = 7;
    private static final List<BaubleTypeEx> BAUBLE_SLOTS = new ArrayList<>();

    public static void registerBauble(BaubleTypeEx type, int amount) {
        if (amount < 0) return;
        type.setAmount(amount);
        registerBauble(type);
    }

    public static void registerBauble(String typeName, int amount) {
        if (amount < 0) return;
        registerBauble(new BaubleTypeEx(typeName, amount));
    }

    public static void registerBauble(BaubleTypeEx type) {
        if (REGISTRY.getValue(type.getRegistryName()) != null) {
            REGISTRY.getValue(type.getRegistryName()).setAmount(type.getAmount());
        }
        else {
            REGISTRY.register(type);
        }
    }

    public static void setSum(int value) {
        sum = value;
    }
    public static int getSum() {
        return sum;
    }

    public static void initLazyList() {
        if(!BAUBLE_SLOTS.isEmpty()) BAUBLE_SLOTS.clear();
    }
    public static void addLazySlots(BaubleTypeEx type) {
        BAUBLE_SLOTS.add(type);
    }
    public static List<BaubleTypeEx> getLazyList() {
        return ImmutableList.copyOf(BAUBLE_SLOTS);
    }

    public static BaubleTypeEx getTypeByName(String typeName) {
        BaubleTypeEx type = REGISTRY.getValue(new ResourceLocation(DATA_ID, typeName));
        if (type == null) type = REGISTRY.getValue(new ResourceLocation(typeName));
        return type;
    }
    public static BaubleTypeEx getTypeById(int id) {
        return REGISTRY.getValue(id);
    }
    public static int getId(BaubleTypeEx type) {
        return REGISTRY.getID(type);
    }

    public static boolean hasType(String typeName) {
        boolean contained = REGISTRY.containsKey(new ResourceLocation(DATA_ID, typeName));
        if (!contained) contained = REGISTRY.containsKey(new ResourceLocation(typeName));
        return contained;
    }

    public static Iterator<BaubleTypeEx> iterator() {
        return REGISTRY.iterator();
    }
}