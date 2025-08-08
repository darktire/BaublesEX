package baubles.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class BaublesWrapper implements IBauble {

    private Item item;
    private IBauble bauble;
    private BaubleTypeEx mainType;
    private ArrayList<BaubleTypeEx> type;// todo types
    private ResourceLocation registryName;

    public BaublesWrapper() {}

    public BaublesWrapper(Item item) {
        this(item, (IBauble) item);
    }

    public BaublesWrapper(Item item, IBauble bauble) {
        this.item = item;
        this.bauble = bauble;
        this.mainType = bauble.getBaubleTypeEx();
        if (mainType == null) this.mainType = bauble.getBaubleType(ItemStack.EMPTY).getNewType();
        if (mainType == null) this.mainType = new BaubleTypeEx("NO_TYPE", 0);
    }

    public Item getItem() {
        return this.item;
    }

    public void setType(BaubleTypeEx type) {
        this.mainType = type;
    }

    @Override
    public BaubleTypeEx getBaubleTypeEx() {
        return mainType;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return mainType.getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        bauble.onWornTick(itemstack, entity);
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        bauble.onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        bauble.onUnequipped(itemstack, entity);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        return bauble.canEquip(itemstack, entity);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        return !EnchantmentHelper.hasBindingCurse(itemstack) && bauble.canUnequip(itemstack, entity);
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase entity) {
        return bauble.willAutoSync(itemstack, entity);
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return bauble.canDrop(itemstack, entity);
    }
}
