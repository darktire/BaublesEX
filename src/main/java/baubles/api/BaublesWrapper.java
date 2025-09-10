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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public boolean haSub = false;// todo solve metadata

    public BaublesWrapper() {}

    public BaublesWrapper(Item item, IBauble bauble) {
        this.item = item;
        this.bauble = bauble;
        if (item.getHasSubtypes()) {
            this.haSub = true;
//            for (int meta = 0;; meta++) {
//                if (!item.getHasSubtypes()) break;
//            }
        }
        this.wrapBauble(item, bauble);
        this.registerRender(item);
    }

    private void wrapBauble(Item item, IBauble bauble) {
        if (bauble != null) {
            ItemStack stack = new ItemStack(item);
            this.type = bauble.getType(stack);
            this.types = bauble.getTypes(stack);
            if (this.types == null || this.types.isEmpty()) {
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
    public BaubleTypeEx getType(ItemStack itemStack) {
        return this.type;
    }

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
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
    @SideOnly(Side.CLIENT)
    public Map<ModelBauble, RenderType> getRenderMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getRenderMap(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getModel(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getTexture(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getEmissiveMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getEmissiveMap(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getRenderType(stack, entity, slim);
    }
}
// todo commands edit type, also the data persistence
