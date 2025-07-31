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

	private final int defaultAmount;
    private final BaubleTypeEx baubleTypeEx;

	BaubleType(int amount) {
		this.defaultAmount = amount;
		this.baubleTypeEx = new BaubleTypeEx(this.toString().toLowerCase(), amount);
	}

	public BaubleTypeEx getNewType() {
		return baubleTypeEx;
	}

	public int getDefaultAmount() {
		return this.defaultAmount;
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
