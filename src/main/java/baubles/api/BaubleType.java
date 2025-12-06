package baubles.api;

import baubles.api.registries.TypesData;

/**
 * Default bauble types
 **/
public enum BaubleType {

	AMULET(0),
	RING(1,2),
	BELT(3),
	TRINKET(7),
	HEAD(4),
	BODY(5),
	CHARM(6);

    private final BaubleTypeEx baubleTypeEx;
	private final int[] idx;

	BaubleType(int ... idx) {
		this.baubleTypeEx = TypesData.Preset.enumRef(idx[0]);
		this.idx = idx;
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
        return idx;
	}
}
