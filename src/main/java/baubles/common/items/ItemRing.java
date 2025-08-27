package baubles.common.items;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.Config;
import baubles.util.BaublesRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemRing extends Item implements IBauble {

	public ItemRing() {
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setTranslationKey("Ring");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, 0));
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.RING;
	}

/*	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++)
				if((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
					baubles.setStackInSlot(i, player.getHeldItem(hand).copy());
					if(!player.capabilities.isCreativeMode){
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					}
					onEquipped(player.getHeldItem(hand), player);
					break;
				}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}*/

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
    }

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		return super.getTranslationKey() + "." + par1ItemStack.getItemDamage();
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		}
		updatePotionStatus(entity);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		}
		updatePotionStatus(entity);
	}

	public void updatePotionStatus(EntityLivingBase entity) {
		int level = -1;
		IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
		Potion potion = Potion.REGISTRY.getObject(new ResourceLocation("haste"));

		BaubleTypeEx target = TypesData.getTypeByName("ring");
		for (int i = 0; i < baubles.getSlots(); i++) {
			if (baubles.getTypeInSlot(i) != target) continue;
			ItemStack ring1 = baubles.getStackInSlot(i);
			if (level >= Config.maxLevel - 1) break;
			if (ring1.getItem() == BaublesRegistry.ModItems.Ring) level++;
		}
		if (potion != null) {
			PotionEffect currentEffect = entity.getActivePotionEffect(potion);
			int currentLevel = currentEffect != null ? currentEffect.getAmplifier() : -1;
			if (currentLevel != level) {
				entity.removeActivePotionEffect(potion);
				if (level != -1 && !entity.world.isRemote)
					entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, Integer.MAX_VALUE, level, true, true));

			}
		}
    }
}