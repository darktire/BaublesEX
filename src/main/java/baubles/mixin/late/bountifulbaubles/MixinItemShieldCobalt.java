package baubles.mixin.late.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.bountifulbaubles.ModelShield;
import cursedflames.bountifulbaubles.item.ItemShieldCobalt;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemShieldCobalt.class)
public abstract class MixinItemShieldCobalt extends Item implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
    return ModelShield.INSTANCE;
}
}
