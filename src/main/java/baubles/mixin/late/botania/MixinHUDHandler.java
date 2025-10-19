package baubles.mixin.late.botania;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

@Mixin(value = HUDHandler.class, remap = false)
public class MixinHUDHandler {
    @Inject(method = "onDrawScreenPre", at = @At("HEAD"), cancellable = true)
    private static void injected(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            IItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) mc.player);
            int index = BaublesApi.getIndexInBaubles(mc.player, ModItems.flightTiara, 0);
            if (index != -1) {
                ItemFlightTiara.renderHUD(event.getResolution(), mc.player, baubles.getStackInSlot(index));
            }

            index = BaublesApi.getIndexInBaubles(mc.player, ModItems.dodgeRing, 0);
            if (index != -1) {
                ItemDodgeRing.renderHUD(event.getResolution(), mc.player, baubles.getStackInSlot(index), event.getPartialTicks());
            }
        }
        ci.cancel();
    }
}
