package baubles.api;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;

public class BaubleTypeEx extends IForgeRegistryEntry.Impl<BaubleTypeEx> {
    private final String typeName;
    private int amount;
    private final ArrayList<Integer> oriSlots;

    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.setRegistryName(typeName);
        this.amount = amount;
        this.oriSlots = new ArrayList<>();
    }

    public BaubleType getOldType() {
        return BaubleType.valueOf(typeName.toUpperCase());
    }

    public void setAmount(int x) {
        amount = x;
    }
    public int getAmount() {
        return amount;
    }

    public void addOriSlots(int slots) {
        oriSlots.add(slots);
    }
    public void addOriSlots(BaubleTypeEx type) {
        oriSlots.addAll(type.getOriSlots());
    }
    public ArrayList<Integer> getOriSlots() {
        return oriSlots;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getTexture() {
        return "gui/slots/" + typeName;
    }

    public boolean hasSlot(int slot) {
        return oriSlots.contains(slot);
    }
}