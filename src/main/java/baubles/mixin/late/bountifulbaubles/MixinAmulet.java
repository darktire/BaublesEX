package baubles.mixin.late.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.Models;
import baubles.compat.bountifulbaubles.ModelAmulet;
import cursedflames.bountifulbaubles.item.ItemAmuletCross;
import cursedflames.bountifulbaubles.item.ItemAmuletSin;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemAmuletCross.class, ItemAmuletSin.class})
public abstract class MixinAmulet extends Item implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return Models.getInstance(Models.wrap(stack.getItem()), ModelAmulet::new);
    }
}
