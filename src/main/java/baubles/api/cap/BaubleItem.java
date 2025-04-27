package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;

public class BaubleItem implements IBauble
{
	private final BaubleType baubleType;
	private final BaubleTypeEx baubleTypeEx;

	public BaubleItem(BaubleType type) {
		this.baubleType = type;
		this.baubleTypeEx = type.getBaubleTypeEx();
	}

	public BaubleItem(BaubleTypeEx type) {
		this.baubleType = type.getOldType();
		this.baubleTypeEx = type;
	}

	@Override
	public BaubleType getBaubleType() {
		return baubleType;
	}

	@Override
	public BaubleTypeEx getBaubleTypeEx() {
		return baubleTypeEx;
	}
}
