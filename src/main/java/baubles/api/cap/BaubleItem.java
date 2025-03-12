package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

public class BaubleItem implements IBauble {
	private BaubleType baubleType;
    private BaubleTypeEx baubleTypeEx;

    public BaubleItem(BaubleType type) {
        baubleType = type;
    }

    public BaubleItem(BaubleTypeEx typeEx) {
        baubleTypeEx = typeEx;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return baubleType;
    }

    @Override
    public BaubleTypeEx getBaubleTypeEx(ItemStack itemStack) {
        return baubleTypeEx;
    }
}
