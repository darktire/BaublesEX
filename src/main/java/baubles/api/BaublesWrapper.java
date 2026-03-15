package baubles.api;

import baubles.api.event.BaublesEvent;
import baubles.api.model.ModelBauble;
import baubles.api.module.IModule;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public final class BaublesWrapper extends AbstractWrapper {
    private IBauble bauble;
    private IRenderBauble render;
    private Addition addition;

    public BaublesWrapper() {}

    public BaublesWrapper(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IBauble) {
            this.bauble = (IBauble) item;
        }
        if (item instanceof IRenderBauble) {
            this.render = (IRenderBauble) item;
        }
        this.addition = CSTMap.INSTANCE.get(stack);
    }

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
        if (this.addition != null && this.addition.types != null) return this.addition.types;
        return getBauble().getTypes(itemStack);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return getTypes(itemStack).get(0).getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.WearingTick event = new BaublesEvent.WearingTick(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        getBauble().onWornTick(itemstack, entity);
        if (this.addition == null || this.addition.effects == null) return;
        for (WornTickEffect effect : this.addition.effects) {
            effect.tick(itemstack, entity);
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Equip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        getBauble().onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Unequip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        getBauble().onUnequipped(itemstack, entity);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = getBauble().canEquip(itemstack, entity);
        BaublesEvent.Equip.Pre event = new BaublesEvent.Equip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = !EnchantmentHelper.hasBindingCurse(itemstack) && getBauble().canUnequip(itemstack, entity);
        BaublesEvent.Unequip.Pre event = new BaublesEvent.Unequip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return getBauble().canDrop(itemstack, entity);
    }

    @Override
    public List<IModule> getModules(ItemStack itemstack, EntityLivingBase entity) {
        if (this.addition != null && this.addition.modules != null) return this.addition.modules;
        return getBauble().getModules(itemstack, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<IRenderBauble> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        return render != null ? render.getSubRender(stack, entity, renderPlayer) : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        return render != null ? render.getModel(stack, entity, renderPlayer) : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        return render != null ? render.getRenderType(stack, entity, renderPlayer) : null;
    }

    private IBauble getBauble() {
        if (this.addition != null && this.addition.bauble != null) return this.addition.bauble;
        return this.bauble;
    }

    private IRenderBauble getRender() {
        if (this.addition != null && this.addition.render != null) return this.addition.render;
        return this.render;
    }
}
