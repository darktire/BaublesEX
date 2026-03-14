package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.enigmaticlegacy.ModelCharm;
import keletu.enigmaticlegacy.item.ItemBerserkEmblem;
import keletu.enigmaticlegacy.item.ItemEnigmaticEye;
import keletu.enigmaticlegacy.item.ItemMiningCharm;
import keletu.enigmaticlegacy.item.ItemMonsterCharm;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemEnigmaticEye.class, ItemBerserkEmblem.class, ItemMiningCharm.class, ItemMonsterCharm.class})
public class MixinCharm implements IRenderBauble {

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelCharm.INSTANCE;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
