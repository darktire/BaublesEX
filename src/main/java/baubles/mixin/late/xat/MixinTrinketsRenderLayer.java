package baubles.mixin.late.xat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xzeroair.trinkets.client.renderLayers.TrinketsRenderLayer;

import java.util.function.Consumer;

@Mixin(value = TrinketsRenderLayer.class, remap = false)
public class MixinTrinketsRenderLayer {
    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lxzeroair/trinkets/api/TrinketHelper;applyToAccessories(Lnet/minecraft/entity/EntityLivingBase;Ljava/util/function/Consumer;)V"))
    public void injected(EntityLivingBase entity, Consumer<ItemStack> consumer) {}
}
