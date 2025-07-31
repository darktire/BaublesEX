package baubles.api.util;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaublesWrapper implements IBauble {

    private final Item item;
    private final IBauble bauble;
    private BaubleTypeEx type;
    private BaubleTypeEx oldType;
    private boolean modified = false;

    public BaublesWrapper(Item item) {
        this.item = item;
        this.bauble = (IBauble) item;
        this.type = bauble.getBaubleTypeEx();
        if (type == null) this.type = bauble.getBaubleType().getNewType();
        if (type == null) this.type = new BaubleTypeEx("NO_TYPE", 0);
    }

    public BaublesWrapper(BaubleItem bauble) {
        this.item = bauble.getItem();
        this.bauble = bauble;
    }

    public void setType(BaubleTypeEx type) {
        if (oldType == null) {
            oldType = this.type;
            modified = true;
        }
        if (oldType == type) modified = false;
        this.type = type;
    }

    @Override
    public BaubleTypeEx getBaubleTypeEx() {
        return type;
    }

    @Override
    public BaubleType getBaubleType() {
        return type.getOldType();
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return type.getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        bauble.onWornTick(itemstack, player);
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        bauble.onEquipped(itemstack, player);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        bauble.onUnequipped(itemstack, player);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return bauble.canEquip(itemstack, player);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return bauble.canUnequip(itemstack, player);
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return bauble.willAutoSync(itemstack, player);
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase player) {
        return bauble.canDrop(itemstack, player);
    }
}
