package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelTire;
import com.google.common.collect.ImmutableList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTire extends Item implements IBauble, IRenderBauble {
	private final List<BaubleTypeEx> types = ImmutableList.<BaubleTypeEx>builder()
			.add(TypesData.Preset.HEAD, TypesData.Preset.BODY, TypesData.Preset.BELT, TypesData.Preset.CHARM).build();

	public ItemTire() {
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setTranslationKey("Tire");
		this.addPropertyOverride(new ResourceLocation("meta"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (stack.getMetadata() == 1) {
					return 1F;
				} else if (stack.getMetadata() == 2) {
					return 2F;
				} else if (stack.getMetadata() == 3) {
					return 3F;
				} else return 0;
			}
		});
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, 0));
			list.add(new ItemStack(this, 1, 1));
			list.add(new ItemStack(this, 1, 2));
			list.add(new ItemStack(this, 1, 3));
		}
	}

	@Override
	public List<BaubleTypeEx> getTypes(ItemStack stack) {
		return this.types;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
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
		return ModelTire.getTexture();
	}

	@Override
	public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
		return RenderType.BODY;
	}
}