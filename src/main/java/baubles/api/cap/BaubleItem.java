package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaubleItem implements IBauble {
	private final Item item;
	private BaubleTypeEx type;

	public BaubleItem(Item item, BaubleTypeEx type) {
        this.item = item;
        this.type = type;
	}

	@SuppressWarnings("unused")// for old api
	public BaubleItem(BaubleType type) {
        this.item = null;
        this.type = type.getNewType();
	}

	public Item getItem() {
		return item;
	}

	public void setType(BaubleTypeEx type) {
		this.type = type;
	}

	@Override
	public BaubleTypeEx getBaubleTypeEx() {
		return type;
	}

	@Override
	public BaubleType getBaubleType() {
		return type.getOldType();
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return type.getOldType();
	}
}