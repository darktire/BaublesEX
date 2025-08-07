package baubles.api.util;

import baubles.api.BaubleTypeEx;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.Iterator;

public class TypesData {
    private static final String BAUBLES_ID = "baubles";
    private static final ResourceLocation BAUBLE_TYPE = new ResourceLocation(BAUBLES_ID, "type");
    private static final ForgeRegistry<BaubleTypeEx> Registry = (ForgeRegistry<BaubleTypeEx>) new RegistryBuilder<BaubleTypeEx>().setType(BaubleTypeEx.class).allowModification().setName(BAUBLE_TYPE).create();
    /**
     * Simply summarise.
     */
    private static int sum = 7;
    public static boolean changed = false;
    /**
     * Set the lazy list of slots.
     */
    private static final ArrayList<BaubleTypeEx> BAUBLE_SLOTS = new ArrayList<>();

    public static void registerBauble(BaubleTypeEx type, int amount) {
        if (amount < 0) amount = 0;
        type.setAmount(amount);
        registerBauble(type);
    }

    public static void registerBauble(String typeName, int amount) {
        if (amount < 0) amount = 0;
        registerBauble(new BaubleTypeEx(typeName, amount));
    }

    public static void registerBauble(BaubleTypeEx type) {
        Registry.register(type);
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
    public static ArrayList<BaubleTypeEx> getLazyList() {
        return BAUBLE_SLOTS;
    }

    public static BaubleTypeEx getTypeByName(String typeName) {
        return Registry.getValue(new ResourceLocation(BAUBLES_ID, typeName));
    }
    public static BaubleTypeEx getTypeById(int id) {
        return Registry.getValue(id);
    }
    public static int getId(BaubleTypeEx type) {
        return Registry.getID(type);
    }

    public static boolean hasType(String typeName) {
        return Registry.containsKey(new ResourceLocation(BAUBLES_ID, typeName));
    }

    public static Iterator<BaubleTypeEx> iterator() {
        return Registry.iterator();
    }
}