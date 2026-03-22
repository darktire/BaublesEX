package baubles.mixin.late.iceandfire;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.iceandfire.Resources;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBlindfold;
import com.github.alexthe666.iceandfire.item.ItemEarplugs;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemBlindfold.class, ItemEarplugs.class})
public abstract class MixinHeadWear extends Item implements IRenderBauble {

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this == IafItemRegistry.blindfold) return Resources.BLINDFOLD;
        else if (this == IafItemRegistry.earplugs) return Resources.EAR_PLUGS;
        return null;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.HEAD;
    }
}
