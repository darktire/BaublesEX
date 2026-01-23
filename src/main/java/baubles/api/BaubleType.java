package baubles.api;

import baubles.api.registries.TypeData;

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
		this.baubleTypeEx = TypeData.Preset.enumRef(idx[0]);
		this.idx = idx;
    }

	public BaubleTypeEx getExpansion() {
		return baubleTypeEx;
	}

	@Deprecated
	public boolean hasSlot(int slot) {
		if (this == TRINKET) return true;
		for (int s : idx) {
			if (s == slot) return true;
		}
		return false;
	}

	@Deprecated
	public int[] getValidSlots() {
        return idx;
	}
}
