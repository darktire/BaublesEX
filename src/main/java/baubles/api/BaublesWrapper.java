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
import java.util.Set;
import java.util.function.Supplier;

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
        return getValue(() -> this.addition.types,() -> getBauble().getTypes(itemStack));
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
    public Set<IModule> getModules(ItemStack itemstack, EntityLivingBase entity) {
        return getValue(() -> this.addition.module,() -> getBauble().getModules(itemstack, entity));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public <T extends IRenderBauble> List<T> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        if (render == null) return null;
        return this.render.getSubRender(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        if (render == null) return null;
        return render.getModel(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        IRenderBauble render = getRender();
        if (render == null) return null;
        return this.render.getRenderType(stack, entity, renderPlayer);
    }

    private <T> T getValue(Supplier<T> edited, Supplier<T> original) {
        if (this.addition == null) return original.get();
        T get = edited.get();
        return get != null ? get : original.get();
    }

    private IBauble getBauble() {
        return getValue(() -> this.addition.bauble,() -> this.bauble);
    }

    private IRenderBauble getRender() {
        return getValue(() -> this.addition.render,() -> this.render);
    }
}
