package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelTotem;
import baubles.common.config.Config;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class BaubleTotem extends BaubleVanilla implements IRenderBauble {
    public static BaubleTotem INSTANCE = new BaubleTotem();
    private static final WearingState WEARING = new WearingState();
    private static final List<BaubleTypeEx> TYPE = Collections.singletonList(TypeData.getTypeByName(Config.ModItems.totemSlot));

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
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelTotem.INSTANCE;
    }

    @Override
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return RenderType.BODY;
    }

    public static boolean isWearing(EntityLivingBase entity) {
        return INSTANCE.hasWearing(entity);
    }

    public static ItemStack takeWearing(EntityLivingBase entity) {
        return INSTANCE.take(entity, 1);
    }
}
