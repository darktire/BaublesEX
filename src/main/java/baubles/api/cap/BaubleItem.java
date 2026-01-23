package baubles.api.cap;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BaubleItem implements IBauble {
	private final List<BaubleTypeEx> types = new ArrayList<>();

	public BaubleItem(Collection<BaubleTypeEx> types) {
		this.types.addAll(types);
	}

	public BaubleItem(BaubleTypeEx... types) {
		this(Arrays.asList(types));
	}

	@SuppressWarnings("unused")// for old api
	public BaubleItem(BaubleType type) {
        this.types.add(type.getExpansion());
	}

	@Override
	public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
		return this.types;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return this.types.get(0).getOldType();
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
		if (itemstack.getItem() instanceof ItemArmor && entity instanceof EntityPlayer) {
			itemstack.getItem().onArmorTick(entity.world, (EntityPlayer) entity, itemstack);
		}
	}
}