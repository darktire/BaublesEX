package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

public class BaubleItem implements IBauble {

	private BaubleType type;

	public BaubleItem() {
		this.type = BaubleType.TRINKET;
	}

	public BaubleItem(BaubleType type) {
		this.type = type;
	}

	@Override
	public BaubleType getBaubleType() {
		return type;
	}

	/**
	 * Keep this for old api.
	 * @deprecated prefer calling {@link BaubleItem#getBaubleType()} wherever possible
	 */
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return type;
	}

	public void setType(BaubleType type) {
		this.type = type;
	}
}