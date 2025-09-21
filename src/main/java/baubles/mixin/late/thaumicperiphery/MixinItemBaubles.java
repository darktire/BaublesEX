package baubles.mixin.late.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.thaumicperiphery.ModelAmulet;
import baubles.compat.thaumicperiphery.ModelBelt;
import baubles.compat.thaumicperiphery.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.baubles.ItemBaubles;
import thaumcraft.common.items.casters.ItemFocusPouch;
import thaumicperiphery.ModContent;
import thaumicperiphery.items.ItemVisPhylactery;

@Mixin({ItemBaubles.class, ItemFocusPouch.class, ItemAmuletVis.class, ItemVisPhylactery.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemBaubles extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        int meta = stack.getMetadata();
        if (this == ItemsTC.focusPouch) return ModelBelt.instance(this);
        else if (this == ItemsTC.amuletVis) {
            if (meta == 0) return ModelAmulet.instance(this, false);
            else if (meta == 1) return ModelAmulet.instance(this, true);
        }
        else if (this == ItemsTC.baubles) {
            if (meta == 2 || meta == 6) return ModelBelt.instance(this);
            else if (meta == 0 || meta == 4) return ModelAmulet.instance(this, false);
        }
        else if (this == ModContent.vis_phylactery) return ModelAmulet.instance(this, false);
        return null;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        int meta = stack.getMetadata();
        if (this == ItemsTC.focusPouch) return Resources.FOCUS_POUCH;
        else if (this == ItemsTC.amuletVis) {
            if (meta == 0) return Resources.AMULET_VIS_STONE;
            else if (meta == 1) return Resources.AMULET_VIS;
        }
        else if (this == ItemsTC.baubles) {
            if (meta == 0) return Resources.AMULET_MUNDANE;
            else if (meta == 2) return Resources.GIRDLE_MUNDANE;
            else if (meta == 4) return Resources.AMULET_FANCY;
            else if (meta == 6) return Resources.GIRDLE_FANCY;
        }
        else if (this == ModContent.vis_phylactery) return Resources.VIS_PHYLACTERY;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
