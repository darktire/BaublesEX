package baubles.api;

/**
 * Default bauble types
 **/
public enum BaubleType {

	AMULET(1),
	RING(2),
	BELT(1),
	HEAD(1),
	BODY(1),
	CHARM(1),
	TRINKET(7);

	private final int defaultAmount;
	private final String typeName;
	private final BaubleTypeEx baubleTypeEx;
	private final int[] validSlots;

	BaubleType(int amount) {
		this.typeName = this.name().toLowerCase();
		this.defaultAmount = amount;
		this.baubleTypeEx = new BaubleTypeEx(typeName, amount);
		this.validSlots = getValidSlots();
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

	public boolean hasSlot(int slot) {
		return baubleTypeEx.hasSlot(slot);
	}

	public int[] getValidSlots() {
		return baubleTypeEx.getValidSlots();
	}
}
