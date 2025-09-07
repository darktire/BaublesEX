package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelTire;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class ItemTire extends Item implements IBauble, IRenderBauble {
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
	public List<BaubleTypeEx> getTypes(ItemStack stack) {
		return this.types;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
		if (entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 0.9f);
		}
		IBaublesModifiable handler = BaublesApi.getBaublesHandler(entity);
		handler.modifySlotOA("trinket", 2);
		handler.updateContainer();
		ModelTire.instance().resetAngle(entity);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
		if (entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 0.9f);
		}
		IBaublesModifiable handler = BaublesApi.getBaublesHandler(entity);
		handler.modifySlotOA("trinket", -2);
		handler.updateContainer();
	}

	@Override
	public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
		return ModelTire.instance();
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
		return ModelTire.instance().getTexture();
	}

	@Override
	public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
		return RenderType.BODY;
	}
}