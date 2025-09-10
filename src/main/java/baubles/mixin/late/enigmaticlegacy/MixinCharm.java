package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.enigmaticlegacy.ModelCharm;
import keletu.enigmaticlegacy.item.ItemBerserkEmblem;
import keletu.enigmaticlegacy.item.ItemEnigmaticEye;
import keletu.enigmaticlegacy.item.ItemMiningCharm;
import keletu.enigmaticlegacy.item.ItemMonsterCharm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemEnigmaticEye.class, ItemBerserkEmblem.class, ItemMiningCharm.class, ItemMonsterCharm.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinCharm {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelCharm.instance((Item) (Object) this, stack.getMetadata());
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelCharm.getTexture();
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
