package baubles.mixin.late.botania;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.gui.box.GuiBaubleBox;

@Mixin(GuiBaubleBox.class)
public abstract class MixinGuiBaubleBox extends GuiContainer {

    public MixinGuiBaubleBox(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    public void injected(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        for(int i1 = 0; i1 < 7; ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
            this.drawTexturedModalRect(this.guiLeft + slot.xPos, this.guiTop + slot.yPos, 200, 0, 16, 16);
        }
    }
}
