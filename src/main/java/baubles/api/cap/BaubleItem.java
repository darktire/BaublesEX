package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BaubleItem implements IBauble {
	private final BaubleTypeEx type;
	private final List<BaubleTypeEx> types = new LinkedList<>();

	public BaubleItem(List<BaubleTypeEx> types) {
		this.type = types.get(0);
		this.types.addAll(types);
	}

	public BaubleItem(BaubleTypeEx... types) {
        this.type = types[0];
		this.types.addAll(Arrays.asList(types));
	}

	@SuppressWarnings("unused")// for old api
	public BaubleItem(BaubleType type) {
        this.type = type.getExpansion();
	}

	@Override
	public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
		return this.types;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return type.getOldType();
	}
}