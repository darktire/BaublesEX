package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelManager;
import baubles.compat.enigmaticlegacy.ModelScroll;
import keletu.enigmaticlegacy.item.ItemBaseBauble;
import keletu.enigmaticlegacy.item.ItemScrollBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemScrollBauble.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinScroll extends ItemBaseBauble {

    public MixinScroll(String name, EnumRarity rare) {
        super(name, rare);
    }

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, null, k -> new ModelScroll());
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
