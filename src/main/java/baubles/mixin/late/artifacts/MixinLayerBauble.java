package baubles.mixin.late.artifacts;

import artifacts.client.model.layer.LayerBauble;
import baubles.util.HookHelper;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

@Mixin(value = LayerBauble.class, remap = false)
public abstract class MixinLayerBauble {

    @Shadow
    protected RenderPlayer renderPlayer;

    @Unique
    private static final Field bs$MODEL_FIELD = HookHelper.getField("artifacts.client.model.layer.LayerBauble", "model");

    @Shadow
    protected abstract void renderLayer(@Nonnull EntityPlayer entityPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6);

    @Inject(method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void Injected(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) throws IllegalAccessException {
        if (player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) return;

        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();

        ModelPlayer model = (ModelPlayer) bs$MODEL_FIELD.get(this);
        model.setModelAttributes(this.renderPlayer.getMainModel());
        model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player);

        GlStateManager.pushMatrix();
        this.renderLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();
        ci.cancel();
    }
}
