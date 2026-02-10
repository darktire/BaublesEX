package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelTotem;
import baubles.common.config.Config;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BaubleTotem extends BaubleVanilla implements IRenderBauble {
    public static BaubleTotem INSTANCE = new BaubleTotem();
    private static final Map<UUID, Integer> WEARING = new WeakHashMap<>();
    private static final List<BaubleTypeEx> TYPE = Collections.singletonList(TypeData.getTypeByName(Config.ModItems.totemSlot));

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
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelTotem.INSTANCE;
    }

    public static boolean isWearing(EntityLivingBase entity) {
        return WEARING.computeIfAbsent(entity.getUniqueID(), id -> INSTANCE.update(entity)) != -1;
    }

    public static ItemStack getWearing(EntityLivingBase entity) {
        return BaublesApi.getBaublesHandler(entity).getStackInSlot(WEARING.get(entity.getUniqueID()));
    }
}
