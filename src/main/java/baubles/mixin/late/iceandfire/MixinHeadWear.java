package baubles.mixin.late.iceandfire;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.iceandfire.Resources;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBlindfold;
import com.github.alexthe666.iceandfire.item.ItemEarplugs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemBlindfold.class, ItemEarplugs.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinHeadWear extends Item {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return Resources.Model_HEADWEAR;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this == IafItemRegistry.blindfold) return Resources.BLINDFOLD;
        else return Resources.EAR_PLUGS;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.HEAD;
    }
}
