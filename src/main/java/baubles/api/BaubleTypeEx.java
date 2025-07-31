package baubles.api;

import java.util.ArrayList;

public class BaubleTypeEx {
    private final String typeName;
    private int amount;
    private final ArrayList<Integer> validSlots;
    private final String texture;

    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.amount = amount;
        this.validSlots = new ArrayList<>();
        this.texture = "gui/slots/" + typeName;
    }

    public BaubleTypeEx getType() {
        return this;
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

    public void addValidSlots(int slots) {
        validSlots.add(slots);
    }
    public void addValidSlots(ArrayList<Integer> slots) {
        validSlots.addAll(slots);
    }
    public void addValidSlots(BaubleTypeEx type) {
        validSlots.addAll(type.getValidSlots());
    }
    public ArrayList<Integer> getValidSlots() {
        return validSlots;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getTexture() {
        return texture;
    }

    public boolean hasSlot(int slot) {
        return validSlots.contains(slot);
    }
}