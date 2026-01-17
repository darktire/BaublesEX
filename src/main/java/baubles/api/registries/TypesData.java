package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TypesData {
    private static final ResourceLocation BAUBLE_TYPE = getLoc("types", false);
    private static List<BaubleTypeEx> ORDER = new ArrayList<>();
    private static ForgeRegistry<BaubleTypeEx> REGISTRY;
    private static boolean REGISTERED = false;

    private static int sum = 7;
    private static final List<BaubleTypeEx> BAUBLE_SLOTS = new ArrayList<>();

    public static void create() {
        REGISTRY = (ForgeRegistry<BaubleTypeEx>) new RegistryBuilder<BaubleTypeEx>().setType(BaubleTypeEx.class).setName(BAUBLE_TYPE).create();
    }

    public static BaubleTypeEx registerType(String name, Integer amount, Integer priority, Collection<BaubleTypeEx> parents) {
        if (name == null) return null;
        BaubleTypeEx type = null;
        if (REGISTERED) {
            if (TypesData.hasType(name)) {
                type = TypesData.getTypeByName(name);
            }
            else {
                BaublesApi.log.error("{} is registered too late", name);
                return null;
            }
        }
        else if (BaubleTypeEx.REG_MAP.containsKey(name)) {
            type = BaubleTypeEx.REG_MAP.get(name);
        }

        if (type == null) {
            if (amount == null) amount = 0;
            if (priority == null) priority = 0;
            type = BaubleTypeEx.create(name, amount, priority);
        }
        else {
            if (amount != null) type.setAmount(amount);
            if (priority != null) type.setPriority(priority);
        }
        if (parents != null && !BaubleTypeEx.isGlobal(type)) type.setParents(parents);
        return type;
    }

    public static void registerTypes() {
        BaubleTypeEx.REG_MAP.clear();
        while (!BaubleTypeEx.REG_QUE.isEmpty()) {
            BaubleTypeEx poll = BaubleTypeEx.REG_QUE.poll();
            REGISTRY.register(poll);
        }
        REGISTERED = true;
    }

    public static int getSum() {
        return sum;
    }

    public static void initLazyList() {
        BAUBLE_SLOTS.clear();
        int pointer = 0;
        for (BaubleTypeEx type : getOrder()) {
            int amount = type.getAmount();
            for (int i = 0; i < amount; i++) {
                BAUBLE_SLOTS.add(type);
            }
            pointer += amount;
        }
        sum = pointer;
    }
    public static List<BaubleTypeEx> getLazyList() {
        return ImmutableList.copyOf(BAUBLE_SLOTS);
    }

    public static BaubleTypeEx getTypeByName(String typeName) {
        if (REGISTERED) {
            BaubleTypeEx type = REGISTRY.getValue(getLoc(typeName, false));
            if (type == null) type = REGISTRY.getValue(getLoc(typeName, true));
            return type;
        }
        else {
            typeName = typeName.substring(typeName.indexOf(":") + 1);
            return BaubleTypeEx.REG_MAP.get(typeName);
        }
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

    public static void initOrderList() {
        ORDER = new ArrayList<>(REGISTRY.getValuesCollection());
        ORDER.sort(Collections.reverseOrder());
    }
    public static List<BaubleTypeEx> getOrder() {
        return Collections.unmodifiableList(ORDER);
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

            ENUM_HELPER = new BaubleTypeEx[]{AMULET, RING, RING, BELT, HEAD, BODY, CHARM, TRINKET};
        }
        public static void init() {
        }
        public static BaubleTypeEx enumRef(String name) {
            for (BaubleTypeEx type : ENUM_HELPER) {
                if (type.getName().equals(name)) return type;
            }
            return null;
        }
        public static BaubleTypeEx enumRef(int idx) {
            return ENUM_HELPER[idx];
        }
    }
}