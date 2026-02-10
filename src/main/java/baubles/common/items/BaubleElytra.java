package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelElytra;
import baubles.common.config.Config;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BaubleElytra extends BaubleVanilla implements IRenderBauble {
    public static BaubleElytra INSTANCE = new BaubleElytra();
    private static final Map<UUID, Integer> WEARING = new WeakHashMap<>();
    private static final List<BaubleTypeEx> TYPE = Collections.singletonList(TypeData.getTypeByName(Config.ModItems.elytraSlot));

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack stack) {
        return TYPE;
    }

    @Override
    protected Map<UUID, Integer> getEquipSlotMap() {
        return WEARING;
    }

    @Override
    protected boolean check(ItemStack stack, boolean using) {
        return stack.getItem() == Items.ELYTRA && (!using || ItemElytra.isUsable(stack));
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelElytra.INSTANCE;
    }

    public static boolean isWearing(EntityLivingBase entity) {
        return WEARING.computeIfAbsent(entity.getUniqueID(), id -> INSTANCE.update(entity)) != -1;
    }

    public static ItemStack getWearing(EntityLivingBase entity, boolean using) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        ItemStack stack = baubles.getStackInSlot(WEARING.get(entity.getUniqueID()));
        if (using && !ItemElytra.isUsable(stack)) stack = baubles.getStackInSlot(INSTANCE.update(entity, true));
        return stack;
    }
}
