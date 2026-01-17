package baubles.mixin.late.bountifulbaubles;

import baubles.api.BaublesApi;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import cursedflames.bountifulbaubles.baubleeffect.BaubleAttributeModifierHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BaubleAttributeModifierHandler.class, remap = false)
public class MixinBaubleAttributeModifierHandler {
    @Inject(method = "baubleModified", at = @At(value = "INVOKE", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;generateModifier(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void inject(ItemStack stack, EntityLivingBase entity, boolean equip, CallbackInfo ci) {
        if (!entity.world.isRemote) {
            int index = BaublesApi.getBaublesHandler(entity).indexOf(stack.getItem(), 0);
            PacketSync pkt = PacketSync.S2CPack(entity, index, stack, -1);
            PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) entity);
        }
    }
}
