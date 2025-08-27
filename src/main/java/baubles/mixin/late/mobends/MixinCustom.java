package baubles.mixin.late.mobends;

import baubles.util.HookHelper;
import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomCape;
import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomElytra;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({LayerCustomElytra.class, LayerCustomCape.class})
public abstract class MixinCustom {
    @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack injected(AbstractClientPlayer entity, EntityEquipmentSlot slot) {
        return HookHelper.elytraInBaubles(entity, slot);
    }
}
