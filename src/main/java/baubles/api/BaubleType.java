package baubles.api;

import baubles.api.registries.TypesData;

/**
 * Default bauble types
 **/
public enum BaubleType {

	HEAD, AMULET, BODY, RING, BELT, CHARM, TRINKET;

    private final BaubleTypeEx baubleTypeEx;
	private final static int[] EMPTY = new int[0];

	BaubleType() {
		this.baubleTypeEx = TypesData.Preset.enumRef(this.toString().toLowerCase());
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
