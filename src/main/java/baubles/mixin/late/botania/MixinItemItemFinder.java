package baubles.mixin.late.botania;

import baubles.api.BaublesApi;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.item.equipment.bauble.ItemItemFinder;

@Mixin(value = ItemItemFinder.class, remap = false)
public class MixinItemItemFinder {
    @Inject(method = "onWornTick", at = @At("TAIL"))
    void inject(ItemStack stack, EntityLivingBase entity, CallbackInfo ci) {
        if (!entity.world.isRemote && entity instanceof EntityPlayerMP) {
            PacketSync pkt = PacketSync.S2CPack(entity, BaublesApi.getIndexInBaubles(entity, stack, 0), stack, -1);
            PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) entity);
        }
    }
}
