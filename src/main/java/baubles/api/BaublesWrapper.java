package baubles.api;

import baubles.api.event.BaublesEvent;
import baubles.api.event.BaublesRenderEvent;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class BaublesWrapper implements IWrapper {

    private Item item;
    private IBauble bauble;
    private IRenderBauble render;
    private BaubleTypeEx type;
    private List<BaubleTypeEx> types;
    private ResourceLocation registryName;

    public BaublesWrapper() {}

    public BaublesWrapper(Item item, IBauble bauble) {
        this.item = item;
        this.bauble = bauble;
        this.registerBauble(item, bauble);
        this.registerRender(item);
    }

    private void registerBauble(Item item, IBauble bauble) {
        if (bauble != null) {
            this.type = bauble.getBaubleType();
            this.types = bauble.getBaubleTypes();
            if (this.types == null || this.types.isEmpty()) {
                if (this.type == null) {
                    this.type = bauble.getBaubleType(new ItemStack(item)).getExpansion();
                    if (this.type == null) throw new RuntimeException(item.getRegistryName() + " have no type");
                }
                this.types = new LinkedList<>();
                this.types.add(this.type);
            } else if (this.type == null) {
                this.type = this.types.get(0);
            }
        }
    }

    public void registerRender(Object object) {
        if (object instanceof IRenderBauble) {
            this.render = (IRenderBauble) object;
        }
    }

    public Item getItem() {
        return this.item;
    }

    public IBauble getBauble() {
        return this.bauble;
    }

    public BaublesWrapper setType(BaubleTypeEx type) {
        this.type = type;
        this.types.add(type);
        return this;
    }

    public BaublesWrapper setTypes(List<BaubleTypeEx> types) {
        this.type = this.types.get(0);
        this.types = types;
        return this;
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
        BaublesEvent.WearingTick event = new BaublesEvent.WearingTick(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onWornTick(itemstack, entity);
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.Equip event = new BaublesRenderEvent.Equip(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.Unequip event = new BaublesEvent.Unequip(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
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
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.canDrop(itemstack, entity);
    }

    @Override
    public Map<ModelBauble, RenderType> getRenderMap(boolean slim) {
        if (this.render == null) return null;
        return this.render.getRenderMap(slim);
    }

    @Override
    public ModelBauble getModel(boolean slim) {
        if (this.render == null) return null;
        return this.render.getModel(slim);
    }

    @Override
    public ResourceLocation getTexture(boolean slim, EntityLivingBase entity) {
        if (this.render == null) return null;
        return this.render.getTexture(slim, entity);
    }

    @Override
    public ResourceLocation getEmissiveMap(boolean slim, EntityLivingBase entity) {
        if (this.render == null) return null;
        return this.render.getEmissiveMap(slim, entity);
    }

    @Override
    public RenderType getRenderType() {
        if (this.render == null) return null;
        return this.render.getRenderType();
    }
}
// todo commands edit type, also the data persistence
