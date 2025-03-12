package baubles.api;

/**
 * Default bauble types
 **/
public enum BaubleType {

    AMULET(1),
    RING(2),
    BELT(1),
    TRINKET(6),
    HEAD(1),
    BODY(1),
    CHARM(1);

    public final String name;
    private final BaubleTypeEx baubleTypeEx;
    public final int amount;
    private final int[] validSlots;

    BaubleType(int amount) {
        this.name = this.name().toLowerCase();
        this.amount = amount;
//        this.translationKey = "name." + name.toUpperCase();
//        this.backgroundTexture = "baubles:gui/slots/" + name;
        this.baubleTypeEx = new BaubleTypeEx(name);
        this.validSlots = getValidSlots();
    }

    @Deprecated
    public boolean hasSlot(int slot) {
        return baubleTypeEx.hasSlot(slot);
    }

    @Deprecated
    public int[] getValidSlots() {
        return baubleTypeEx.getValidSlots();
    }
}
