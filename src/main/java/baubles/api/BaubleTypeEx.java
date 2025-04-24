package baubles.api;

public class BaubleTypeEx {
    private int amount;
    private int[] validSlots;
    private final String typeName;
    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.amount = amount;
    }

    public BaubleTypeEx getType() {
        return this;
    }

    public void setAmount(int x) {
        amount = x;
    }
    public int getAmount() {
        return amount;
    }

    public void setValidSlots(int[] slots) {
        this.validSlots = slots;
    }
    public int[] getValidSlots() {
        return validSlots;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean hasSlot(int slot) {
        for (int s: validSlots) {
            if (s == slot) return true;
        }
        return false;
    }
}