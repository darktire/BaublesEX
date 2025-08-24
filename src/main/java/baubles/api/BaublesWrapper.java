package baubles.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public final class BaublesWrapper implements IBauble {

    private Item item;
    private IBauble bauble;
    private BaubleTypeEx type;
    private List<BaubleTypeEx> types;
    private ResourceLocation registryName;

    public BaublesWrapper() {}

    public BaublesWrapper(Item item, IBauble bauble) {
        this.item = item;
        this.bauble = bauble;
        if (bauble != null) {
            this.type = bauble.getBaubleType();
            this.types = bauble.getBaubleTypes();
            if (this.types == null || this.types.isEmpty()) {
                if (this.type == null) {
                    this.type = bauble.getBaubleType(
                            (bauble instanceof Item) ? new ItemStack((Item) bauble) : ItemStack.EMPTY
                    ).getExpansion();
                    if (this.type == null) throw new RuntimeException(item.getRegistryName() + " have no type");
                }
                this.types = new LinkedList<>();
                this.types.add(this.type);
            } else if (this.type == null) {
                this.type = this.types.get(0);
            }
        }
    }

    public Item getItem() {
        return this.item;
    }

    public IBauble getBauble() {
        return this.bauble;
    }

    public void setType(BaubleTypeEx type) {
        this.type = type;
    }

    public void setTypes(List<BaubleTypeEx> types) {
        this.type = this.types.get(0);
        this.types = types;
    }

    public void addTypes(List<BaubleTypeEx> types) {
        this.types.addAll(types);
    }

    public boolean isCopy() {
        if (this.bauble instanceof Item) return !(this.item == this.bauble);
        return false;
    }

    @Override
    public BaubleTypeEx getBaubleType() {
        return this.type;
    }

    @Override
    public List<BaubleTypeEx> getBaubleTypes() {
        return this.types;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return this.type.getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        this.bauble.onWornTick(itemstack, entity);
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        this.bauble.onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        this.bauble.onUnequipped(itemstack, entity);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.canEquip(itemstack, entity);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        return !EnchantmentHelper.hasBindingCurse(itemstack) && this.bauble.canUnequip(itemstack, entity);
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.willAutoSync(itemstack, entity);
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.canDrop(itemstack, entity);
    }
}
// todo commands edit type, also the data persistence
