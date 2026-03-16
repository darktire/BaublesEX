package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.Models;
import baubles.compat.enigmaticlegacy.ModelScroll;
import keletu.enigmaticlegacy.item.ItemBaseBauble;
import keletu.enigmaticlegacy.item.ItemScrollBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemScrollBauble.class)
public abstract class MixinScroll extends ItemBaseBauble implements IRenderBauble {

    public MixinScroll(String name, EnumRarity rare) {
        super(name, rare);
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return Models.getInstance(Models.wrap(stack.getItem()), k -> new ModelScroll());
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
