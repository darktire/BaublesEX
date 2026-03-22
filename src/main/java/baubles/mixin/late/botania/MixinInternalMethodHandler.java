package baubles.mixin.late.botania;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.NetworkHandler;
import baubles.common.network.PacketSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.common.core.handler.InternalMethodHandler;

@Mixin(value = InternalMethodHandler.class, remap = false)
public abstract class MixinInternalMethodHandler extends DummyMethodHandler {
    @Inject(method = "sendBaubleUpdatePacket", at = @At("HEAD"), cancellable = true)
    void inject(EntityPlayer player, int slot, CallbackInfo ci) {
        if (player instanceof EntityPlayerMP) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            NetworkHandler.CHANNEL.sendTo(PacketSync.S2CPack(player, slot, baubles.getStackInSlot(slot), -1), (EntityPlayerMP) player);
        }
        ci.cancel();
    }
}
