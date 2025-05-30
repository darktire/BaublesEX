package baubles.client;

import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.client.gui.GuiBaublesTab;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.Baubles;
import baubles.common.config.KeyBindings;
import baubles.common.items.ItemRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.Iterator;

public class ClientEventHandler {

    @SubscribeEvent
    public void registerItemModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ItemRing.ringModel, 0, new ModelResourceLocation("baubles:ring", "inventory"));
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
            IBauble bauble = event.getItemStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
            if (bauble != null) {
                String bt = "name." + bauble.getBaubleType().name();
                event.getToolTip().add(TextFormatting.GOLD + I18n.format(bt));
            }
        }
    }

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        Iterator<BaubleTypeEx> types = Baubles.baubles.iterator();
        types.forEachRemaining((type)->registerTexture(map, type));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        if (KeyBindings.KEY_BAUBLES.isPressed()) {
            mc.displayGuiScreen(new GuiPlayerExpanded(player));
        }
        if (KeyBindings.KEY_BAUBLES_TAB.isPressed()) {
            mc.displayGuiScreen(new GuiBaublesTab(player));
        }
    }

    private void registerTexture(TextureMap map, BaubleTypeEx type) {
        map.registerSprite(new ResourceLocation(Baubles.MODID, type.getTexture()));
    }
}