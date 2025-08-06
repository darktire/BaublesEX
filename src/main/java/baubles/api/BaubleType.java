package baubles.api;

/**
 * Default bauble types
 **/
public enum BaubleType {

	HEAD(1),
	AMULET(1),
	BODY(1),
	RING(2),
	BELT(1),
	CHARM(1),
	TRINKET(0);

	public final int amount;
    private final BaubleTypeEx baubleTypeEx;

	BaubleType(int amount) {
		this.amount = amount;
		this.baubleTypeEx = new BaubleTypeEx(this.toString().toLowerCase(), amount);
	}

	public BaubleTypeEx getNewType() {
		return baubleTypeEx;
	}

	@Deprecated
	public boolean hasSlot(int slot) {
		return baubleTypeEx.hasSlot(slot);
	}

	@Deprecated // artifact incompatible
	public int[] getValidSlots() {
        return baubleTypeEx.getOriSlots().stream().mapToInt(Integer::intValue).toArray();
	}
}
