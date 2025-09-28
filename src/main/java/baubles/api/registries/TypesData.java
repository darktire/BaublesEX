package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
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

    public static BaubleTypeEx registerType(String name, Integer amount, Integer priority, Collection<BaubleTypeEx> parents) {
        if (name == null) return null;
        BaubleTypeEx type;
        if (TypesData.hasType(name)) {
             type = TypesData.getTypeByName(name);
            if (amount != null) type.setAmount(amount);
            if (priority != null) type.setPriority(priority);
        }
        else {
            if (amount == null) amount = 0;
            if (priority == null) priority = 0;
            type = BaubleTypeEx.create(name, amount, priority);
        }
        if (!parents.isEmpty() && !BaubleTypeEx.isGlobal(type)) type.setParents(parents);
        return type;
    }

    public static void registerTypes() {
        while (!BaubleTypeEx.REG_QUE.isEmpty()) {
            BaubleTypeEx poll = BaubleTypeEx.REG_QUE.poll();
            if (poll != null && !REGISTRY.containsKey(poll.getRegistryName())) {
                REGISTRY.register(poll);
            }
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
        public static BaubleTypeEx BAUBLE, HEAD, AMULET, BODY, RING, BELT, CHARM, TRINKET, ELYTRA;
        public static BaubleTypeEx[] ENUM_HELPER;
        static {
            BAUBLE = BaubleTypeEx.getGlobal(true);
            HEAD = BaubleTypeEx.create("head", 1, 10);
            AMULET = BaubleTypeEx.create("amulet", 1, 10);
            BODY = BaubleTypeEx.create("body", 1, 10);
            RING = BaubleTypeEx.create("ring", 2, 0);
            BELT = BaubleTypeEx.create("belt", 1, 0);
            CHARM = BaubleTypeEx.create("charm", 1, 0);
            TRINKET = BaubleTypeEx.getGlobal(false);
            ELYTRA = BaubleTypeEx.create("elytra", 0, 5);

            ELYTRA.addParent(BODY);

            ENUM_HELPER = new BaubleTypeEx[]{HEAD, AMULET, BODY, RING, BELT, CHARM, TRINKET};
        }
        public static void init() {
        }
        public static BaubleTypeEx enumRef(String name) {
            for (BaubleTypeEx type : ENUM_HELPER) {
                if (type.getName().equals(name)) return type;
            }
            return null;
        }
    }
}