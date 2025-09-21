package baubles.api.registries;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TypesData {
    private static final ResourceLocation BAUBLE_TYPE = getLoc("types", false);
    private static ForgeRegistry<BaubleTypeEx> REGISTRY;

    private static int sum = 7;
    private static final List<BaubleTypeEx> BAUBLE_SLOTS = new ArrayList<>();

    public static void create() {
        REGISTRY = (ForgeRegistry<BaubleTypeEx>) new RegistryBuilder<BaubleTypeEx>().setType(BaubleTypeEx.class).allowModification().setName(BAUBLE_TYPE).create();
    }

    public static void registerBauble(String typeName, int amount, int priority) {
        if (amount < 0) return;
        registerBauble(new BaubleTypeEx(typeName, amount).setPriority(priority));
    }

    public static void registerBauble(String typeName, int amount) {
        if (amount < 0) return;
        registerBauble(new BaubleTypeEx(typeName, amount));
    }

    public static void registerBauble(BaubleTypeEx type) {
        BaubleTypeEx get = REGISTRY.getValue(type.getRegistryName());
        if (get != null) {
            get.setAmount(type.getAmount());
            get.setPriority(type.getPriority());
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
        BaubleTypeEx type = REGISTRY.getValue(getLoc(typeName, false));
        if (type == null) type = REGISTRY.getValue(getLoc(typeName, true));
        return type;
    }
    public static BaubleTypeEx getTypeById(int id) {
        return REGISTRY.getValue(id);
    }
    public static int getId(BaubleTypeEx type) {
        return REGISTRY.getID(type);
    }

    public static boolean hasType(String typeName) {
        boolean contained = REGISTRY.containsKey(getLoc(typeName, false));
        if (!contained) contained = REGISTRY.containsKey(getLoc(typeName, true));
        return contained;
    }

    public static List<BaubleTypeEx> getList() {
        return new ArrayList<>(REGISTRY.getValuesCollection());
    }

    public static void applyToTypes(Consumer<BaubleTypeEx> c) {
        REGISTRY.iterator().forEachRemaining(c);
    }

    private static ResourceLocation getLoc(String path, boolean withId) {
        if (withId) return new ResourceLocation(path);
        return new ResourceLocation(BaublesApi.MOD_ID, path);
    }


    public static class Preset {
        public static BaubleTypeEx HEAD = BaubleType.HEAD.getExpansion();
        public static BaubleTypeEx AMULET = BaubleType.AMULET.getExpansion();
        public static BaubleTypeEx BODY = BaubleType.BODY.getExpansion();
        public static BaubleTypeEx RING = BaubleType.RING.getExpansion();
        public static BaubleTypeEx BELT = BaubleType.BELT.getExpansion();
        public static BaubleTypeEx CHARM = BaubleType.CHARM.getExpansion();
        public static BaubleTypeEx TRINKET = BaubleType.TRINKET.getExpansion();
        public static BaubleTypeEx ELYTRA = BaubleType.ELYTRA.getExpansion();
    }
}