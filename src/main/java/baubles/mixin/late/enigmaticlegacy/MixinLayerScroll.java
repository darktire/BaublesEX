package baubles.mixin.late.enigmaticlegacy;

import keletu.enigmaticlegacy.client.LayerScroll;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerScroll.class)
public class MixinLayerScroll {
    @Inject(method = "renderLivingPost", at = @At("HEAD"), cancellable = true, remap = false)
    public void blockRender(RenderLivingEvent.Post<EntityLivingBase> event, CallbackInfo ci) {
        ci.cancel();
    }
}
