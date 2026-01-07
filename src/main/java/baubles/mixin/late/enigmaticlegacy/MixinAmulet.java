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
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemAscensionAmulet.class, ItemEldritchAmulet.class, ItemEnigmaticAmulet.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinAmulet {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, null, ModelAmulet::new);
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
