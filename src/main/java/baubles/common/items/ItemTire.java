package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.LinkedList;
import java.util.List;

public class ItemTire extends Item implements IBauble {
	private final String[] typesName = {"head", "amulet", "body", "charm"};
	private final List<BaubleTypeEx> types = new LinkedList<>();

	public ItemTire() {
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setTranslationKey("Tire");
		this.setTypes();
	}

	private void setTypes() {
		for (String name: typesName) {
			types.add(TypesData.getTypeByName(name));
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, 0));
		}
	}

	@Override
	public List<BaubleTypeEx> getBaubleTypes() {
		return this.types;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
		if (entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		}
		IBaublesModifiable handler = BaublesApi.getBaublesHandler(entity);
		handler.modifySlotOA("charm", 2);
		handler.updateContainer();
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
		if (entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		}
		IBaublesModifiable handler = BaublesApi.getBaublesHandler(entity);
		handler.modifySlotOA("charm", -2);
		handler.updateContainer();
	}
}