package baubles.api;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BaubleTypeEx extends IForgeRegistryEntry.Impl<BaubleTypeEx> implements Comparable<BaubleTypeEx> {
    private final String name;
    private int amount;
    private int priority;
    private final List<Integer> oriSlots = new ArrayList<>();
    private final Set<BaubleTypeEx> parents = new HashSet<>();

    public static final Queue<BaubleTypeEx> REG_QUE = new ConcurrentLinkedQueue<>();
    public static final Map<String, BaubleTypeEx> REG_MAP = new ConcurrentHashMap<>();

    private static final BaubleTypeEx GLOBAL_P, GLOBAL_C;
    static {
        GLOBAL_P = new BaubleTypeEx("bauble", false);
        GLOBAL_C = new BaubleTypeEx("trinket", true);
    }

    private BaubleTypeEx(String name, int amount, int priority) {
        this.name = name;
        this.setRegistryName(BaublesApi.MOD_ID, name);
        this.amount = amount;
        this.priority = priority;
    }

    private BaubleTypeEx(String name, boolean flag) {
        this(name, 0, 0);
        if (flag) this.parents.add(GLOBAL_P);
    }

    public static BaubleTypeEx create(String name, int amount, int priority) {
        BaubleTypeEx type = new BaubleTypeEx(name, amount, priority);
        type.parents.add(GLOBAL_P);
        GLOBAL_C.parents.add(type);
        return push(type);
    }
    public static boolean isGlobal(BaubleTypeEx type) {
        return type == GLOBAL_P || type == GLOBAL_C;
    }
    public static BaubleTypeEx getGlobal(boolean g) {
        BaubleTypeEx type = g ? GLOBAL_P : GLOBAL_C;
        return push(type);
    }

    private static BaubleTypeEx push(BaubleTypeEx type) {
        REG_QUE.offer(type);
        REG_MAP.put(type.name, type);
        return type;
    }

    public BaubleType getOldType() {
        return BaubleType.valueOf(this.name.toUpperCase());
    }

    public void setAmount(int x) {
        amount = x;
    }
    public int getAmount() {
        return amount;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return this.priority;
    }

    public String getName() {
        return name;
    }
    public String getTexture() {
        return "baubles:gui/slots/" + name;
    }
    public String getTranslateKey() {
        return "baubles." + name;
    }

    public void addParent(BaubleTypeEx parent) {
        if (parent == null || parent == this || parent == GLOBAL_C || this.parents.contains(parent)) return;
        if (this.isAncestor(parent)) throw new IllegalArgumentException(String.format("%s is ancestor of %s", this.name, parent.name));
        this.parents.add(parent);
    }

    public void setParents(Collection<BaubleTypeEx> parents) {
        this.parents.clear();
        this.parents.add(GLOBAL_P);
        parents.forEach(this::addParent);
    }

    public Set<BaubleTypeEx> getParents() {
        return Collections.unmodifiableSet(this.parents);
    }

    private boolean isAncestor(BaubleTypeEx other) {
        if (other == GLOBAL_C) return true;
        if (other == null || other == this || other == GLOBAL_P) return false;

        Queue<BaubleTypeEx> queue = new LinkedList<>(other.parents);
        Set<BaubleTypeEx> visited = new HashSet<>(other.parents);

        while (!queue.isEmpty()) {
            BaubleTypeEx current = queue.poll();
            if (current == this) {
                return true;
            }
            for (BaubleTypeEx p : current.parents) {
                if (!visited.contains(p)) {
                    visited.add(p);
                    queue.add(p);
                }
            }
        }
        return false;
    }

    public boolean contains(BaubleTypeEx... others) {
        return this.contains(new HashSet<>(Arrays.asList(others)));
    }

    public boolean contains(Collection<BaubleTypeEx> others) {
        for (BaubleTypeEx t : others) {
            if (t == this || this.isAncestor(t)) return true;
        }
        return false;
    }
    
    @Override
    public int compareTo(BaubleTypeEx to) {
        return Integer.compare(this.priority, to.getPriority());
    }
}