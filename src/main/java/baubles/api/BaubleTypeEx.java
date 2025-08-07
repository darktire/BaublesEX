package baubles.api;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;

public class BaubleTypeEx extends IForgeRegistryEntry.Impl<BaubleTypeEx> {
    private final String typeName;
    private static int START_ID = 0;
    private final int id;
    private int amount;
    private final ArrayList<Integer> oriSlots;
    private final String texture;

    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.id = START_ID++;
        this.amount = amount;
        this.oriSlots = new ArrayList<>();
        this.texture = "gui/slots/" + typeName;
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
    public void addOriSlots(ArrayList<Integer> slots) {
        oriSlots.addAll(slots);
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
    public int getId() {
        return id;
    }
    public String getTexture() {
        return texture;
    }

    public boolean hasSlot(int slot) {
        return oriSlots.contains(slot);
    }
}