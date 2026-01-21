package baubles.api;

import baubles.api.cap.BaubleItem;
import baubles.api.event.BaublesEvent;
import baubles.api.model.ModelBauble;
import baubles.api.module.IModule;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public final class BaublesWrapper extends AbstractWrapper {
    private ItemStack stack;
    private IBauble bauble;
    private IRenderBauble render;
    private boolean edited = false;
    private Addition addition;

    public BaublesWrapper() {}

    public BaublesWrapper(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if (item instanceof IBauble) {
            this.bauble = (IBauble) item;
        }
        if (item instanceof IRenderBauble) {
            this.render = (IRenderBauble) item;
        }
        this.addition = CSTMap.INSTANCE.get(stack);
        if (this.addition != null) {
            this.addition.addListener(this);
        }
        this.syncChanges();
    }

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
        if (this.edited) return this.addition.types;
        return this.bauble.getTypes(itemStack);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        if (this.edited) return this.addition.types.get(0).getOldType();
        return this.bauble.getTypes(itemStack).get(0).getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.WearingTick event = new BaublesEvent.WearingTick(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onWornTick(itemstack, entity);
        if (this.bauble instanceof BaubleItem && entity instanceof EntityPlayer) {
            this.stack.getItem().onArmorTick(entity.world, (EntityPlayer) entity, this.stack);
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Equip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Unequip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onUnequipped(itemstack, entity);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = this.bauble.canEquip(itemstack, entity);
        BaublesEvent.Equip.Pre event = new BaublesEvent.Equip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = !EnchantmentHelper.hasBindingCurse(itemstack) && this.bauble.canUnequip(itemstack, entity);
        BaublesEvent.Unequip.Pre event = new BaublesEvent.Unequip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.canDrop(itemstack, entity);
    }

    @Override
    public Set<IModule> getModules(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.getModules(itemstack, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public <T extends IRenderBauble> List<T> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getSubRender(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getModel(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getRenderType(stack, entity, renderPlayer);
    }

    @Override
    public void syncChanges() {
        if (this.addition != null) {
            if (this.addition.bauble != null) {
                this.bauble = this.addition.bauble;
            }
            if (this.addition.render != null) {
                this.render = this.addition.render;
            }
            if (this.addition.types != null) this.edited = true;
        }
    }

}
