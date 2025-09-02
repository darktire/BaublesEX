package baubles.mixin.late.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.bountifulbaubles.ModelAmulet;
import cursedflames.bountifulbaubles.item.ItemAmuletCross;
import cursedflames.bountifulbaubles.item.ItemAmuletSin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemAmuletCross.class, ItemAmuletSin.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinAmulet {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        return ModelAmulet.instance((Item) (Object) this);
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        return ModelAmulet.instance((Item) (Object) this).getTexture();
    }
}
