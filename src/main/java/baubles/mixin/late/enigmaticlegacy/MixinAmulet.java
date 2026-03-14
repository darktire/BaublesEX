package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelManager;
import baubles.compat.enigmaticlegacy.ModelAmulet;
import keletu.enigmaticlegacy.item.ItemAscensionAmulet;
import keletu.enigmaticlegacy.item.ItemEldritchAmulet;
import keletu.enigmaticlegacy.item.ItemEnigmaticAmulet;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemAscensionAmulet.class, ItemEldritchAmulet.class, ItemEnigmaticAmulet.class})
public class MixinAmulet implements IRenderBauble {

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, null, ModelAmulet::new);
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
