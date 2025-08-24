package baubles.api;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class BaubleTypeEx extends IForgeRegistryEntry.Impl<BaubleTypeEx> implements Comparable<BaubleTypeEx> {
    private final String typeName;
    private int amount;
    private int priority;
    private final List<Integer> oriSlots;

    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.setRegistryName(typeName);
        this.amount = amount;
        this.priority = 0;
        this.oriSlots = new ArrayList<>();
    }

    public BaubleType getOldType() {
        return BaubleType.valueOf(typeName.toUpperCase());
    }

    public BaubleTypeEx setAmount(int x) {
        amount = x;
        return this;
    }
    public int getAmount() {
        return amount;
    }

    public BaubleTypeEx setPriority(int priority) {
        this.priority = priority;
        return this;
    }
    public int getPriority() {
        return this.priority;
    }

    public void addOriSlots(int slots) {
        oriSlots.add(slots);
    }
    public void addOriSlots(BaubleTypeEx type) {
        oriSlots.addAll(type.getOriSlots());
    }
    public List<Integer> getOriSlots() {
        return oriSlots;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getTexture() {
        return "baubles:gui/slots/" + typeName;
    }

    public String getTranslateKey() {
        return "name." + typeName;
    }

    public boolean hasSlot(int slot) {
        return oriSlots.contains(slot);
    }

    @Override
    public int compareTo(BaubleTypeEx to) {
        return Integer.compare(this.priority, to.getPriority());
    }
}