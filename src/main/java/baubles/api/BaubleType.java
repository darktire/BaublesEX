package baubles.api;

/**
 * Default bauble types
 **/
public enum BaubleType {

	HEAD(1, 10),
	AMULET(1, 10),
	BODY(1, 10),
	RING(2, 0),
	BELT(1, 0),
	CHARM(1, 0),
	TRINKET(0, -1),
	ELYTRA(0, 5);

	public final int amount;
    private final BaubleTypeEx baubleTypeEx;

	BaubleType(int amount, int priority) {
		this.amount = amount;
		this.baubleTypeEx = new BaubleTypeEx(this.toString().toLowerCase(), amount).setPriority(priority);
    }

	public BaubleTypeEx getExpansion() {
		return baubleTypeEx;
	}

	@Deprecated
	public boolean hasSlot(int slot) {
		return baubleTypeEx.hasSlot(slot);
	}

	@Deprecated
	public int[] getValidSlots() {
        return baubleTypeEx.getOriSlots().stream().mapToInt(Integer::intValue).toArray();
	}
}
