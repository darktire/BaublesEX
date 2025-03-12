package baubles.common.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.BaubleContent;
import baubles.common.Baubles;
import baubles.common.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ItemRing extends Item implements IBauble {
	@GameRegistry.ObjectHolder(Baubles.MODID + ":ring")
	public static final Item ringModel = null;

	public ItemRing()
	{
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	private static final Item ring = new ItemRing().setUnlocalizedName("Ring").setRegistryName("ring");

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ring);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, 0));
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
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
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        IBauble.super.onWornTick(itemstack, player);
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
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName() + ".0";
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		updatePotionStatus(player);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		updatePotionStatus(player);
	}

	public void updatePotionStatus(EntityLivingBase player) {

        if (player instanceof EntityPlayer) {

			int level = -1;
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
			Potion potion = Potion.REGISTRY.getObject(new ResourceLocation("haste"));

			for (int i = 0; i < BaubleContent.getAmount(); i++) {
				ItemStack ring1 = baubles.getStackInSlot(i);
				if (ring1.getItem() == ring) level++;
				if (level == Config.maxLevel - 1) break;
			}

            if (potion != null) {
                PotionEffect currentEffect = player.getActivePotionEffect(potion);
                int currentLevel = currentEffect != null ? currentEffect.getAmplifier() : -1;
                if (currentLevel != level) {
                    player.removeActivePotionEffect(potion);
                    if (level != -1 && !player.world.isRemote)
                        player.addPotionEffect(new PotionEffect(MobEffects.HASTE, Integer.MAX_VALUE, level, true, true));
                }
            }
        }
    }
}