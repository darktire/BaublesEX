package baubles.mixin.late.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelManager;
import baubles.compat.bountifulbaubles.ModelAmulet;
import cursedflames.bountifulbaubles.item.ItemAmuletCross;
import cursedflames.bountifulbaubles.item.ItemAmuletSin;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemAmuletCross.class, ItemAmuletSin.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinAmulet extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, null, ModelAmulet::new);
    }
}
