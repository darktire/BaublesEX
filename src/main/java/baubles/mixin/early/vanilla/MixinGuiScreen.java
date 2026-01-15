package baubles.mixin.early.vanilla;

import baubles.client.gui.event.GuiCloseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinGuiScreen {
    @Shadow public GuiScreen currentScreen;

    @Inject(method = "displayGuiScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;onGuiClosed()V", shift = At.Shift.AFTER))
    void injected(GuiScreen guiScreenIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new GuiCloseEvent(this.currentScreen));
    }
}
