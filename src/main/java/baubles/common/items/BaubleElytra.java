package baubles.common.items;

import baubles.api.BaubleTypeEx;
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

import java.util.Collections;
import java.util.List;

public class BaubleElytra extends BaubleVanilla implements IRenderBauble {
    public static BaubleElytra INSTANCE = new BaubleElytra();
    private static final WearingState WEARING = new WearingState();
    private static final List<BaubleTypeEx> TYPE = Collections.singletonList(TypeData.getTypeByName(Config.ModItems.elytraSlot));

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack stack) {
        return TYPE;
    }

    @Override
    protected WearingState getWearingState() {
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
        return INSTANCE.hasWearing(entity);
    }

    public static ItemStack getWearing(EntityLivingBase entity, boolean using) {
        ItemStack stack = INSTANCE.borrow(entity);
        if (using && !ItemElytra.isUsable(stack)) {
            INSTANCE.update(entity, true);
            stack = INSTANCE.borrow(entity);
        }
        return stack;
    }
}
