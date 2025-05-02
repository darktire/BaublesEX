package baubles.api;

public class BaubleTypeEx {
    private final String typeName;
    private int amount;
    private int[] validSlots;
    private String texture;
    public BaubleTypeEx(String typeName, int amount) {
        this.typeName = typeName;
        this.amount = amount;
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

    public void setValidSlots(int[] slots) {
        this.validSlots = slots;
    }
    public int[] getValidSlots() {
        return validSlots;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getTexture() {
        return texture;
    }

    public boolean hasSlot(int slot) {
        for (int s: validSlots) {
            if (s == slot) return true;
        }
        return false;
    }
}