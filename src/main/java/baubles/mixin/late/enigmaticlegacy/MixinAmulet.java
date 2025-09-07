package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.enigmaticlegacy.ModelAmulet;
import keletu.enigmaticlegacy.item.ItemAscensionAmulet;
import keletu.enigmaticlegacy.item.ItemEldritchAmulet;
import keletu.enigmaticlegacy.item.ItemEnigmaticAmulet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemAscensionAmulet.class, ItemEldritchAmulet.class, ItemEnigmaticAmulet.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinAmulet {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelAmulet.instance((Item) (Object) this, stack.getMetadata());
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelAmulet.instance((Item) (Object) this, stack.getMetadata()).getTexture();
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
