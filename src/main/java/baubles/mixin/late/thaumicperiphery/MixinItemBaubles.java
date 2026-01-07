package baubles.mixin.late.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelManager;
import baubles.compat.thaumicperiphery.ModelAmulet;
import baubles.compat.thaumicperiphery.ModelBelt;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.baubles.ItemBaubles;
import thaumcraft.common.items.casters.ItemFocusPouch;
import thaumicperiphery.items.ItemVisPhylactery;

@Mixin({ItemBaubles.class, ItemFocusPouch.class, ItemAmuletVis.class, ItemVisPhylactery.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemBaubles extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        int meta = stack.getMetadata();
        boolean flag = this == ItemsTC.focusPouch || (this == ItemsTC.baubles && (meta == 2 || meta == 6));
        return ModelManager.getInstance(stack, null, flag ? ModelBelt::new : ModelAmulet::new);
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
