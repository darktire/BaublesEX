package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaubleItem implements IBauble {

	private final Item item;
	private BaubleTypeEx type;

	public BaubleItem() {
		this.item = null;
		this.type = BaubleType.TRINKET.getNewType();
	}

	public BaubleItem(BaubleType type) {
		this.item = null;
		this.type = type.getNewType();
	}

	public BaubleItem(Item item, BaubleTypeEx type) {
		this.item = item;
		this.type = type;
	}

	@Override
	public BaubleType getBaubleType() {
		return type.getOldType();
	}

	/**
	 * Keep this for old api.
	 * @deprecated prefer calling {@link BaubleItem#getBaubleType()} wherever possible
	 */
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return type.getOldType();
	}

	public void setType(BaubleTypeEx type) {
		this.type = type;
	}
}