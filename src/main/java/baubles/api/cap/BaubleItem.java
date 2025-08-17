package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

public class BaubleItem implements IBauble {
	private final BaubleTypeEx type;

	public BaubleItem(BaubleTypeEx type) {
        this.type = type;
	}

	@SuppressWarnings("unused")// for old api
	public BaubleItem(BaubleType type) {
        this.type = type.getNewType();
	}

	@Override
	public BaubleTypeEx getBaubleType() {
		return type;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return type.getOldType();
	}
}