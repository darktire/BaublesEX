package baubles.api;

/**
 * Default bauble types
 **/
public enum BaubleType {

	AMULET(1),
	RING(2),
	BELT(1),
	TRINKET(0),
	HEAD(1),
	BODY(1),
	CHARM(1);

	private final int defaultAmount;
	private final String typeName;
	private final BaubleTypeEx baubleTypeEx;

	BaubleType(int amount) {
		this.typeName = this.toString().toLowerCase();
		this.defaultAmount = amount;
		this.baubleTypeEx = new BaubleTypeEx(typeName, amount);
	}

	public BaubleTypeEx getNewType() {
		return baubleTypeEx;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getDefaultAmount() {
		return this.defaultAmount;
	}

	@Deprecated
	public boolean hasSlot(int slot) {
		return baubleTypeEx.hasSlot(slot);
	}

	@Deprecated
	public int[] getValidSlots() {
        return baubleTypeEx.getValidSlots().stream().mapToInt(Integer::intValue).toArray();
	}
}
