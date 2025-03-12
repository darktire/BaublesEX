package baubles.client;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.client.gui.GuiBaublesButton;
import baubles.client.gui.GuiBaublesTab;
import baubles.client.gui.GuiBaublesTabButton;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.Baubles;
import baubles.common.Config;
import baubles.common.items.ItemRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ClientEventHandler {

    @SubscribeEvent
    public void registerItemModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ItemRing.ringModel, 0, new ModelResourceLocation("baubles:ring", "inventory"));
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
            IBauble bauble = event.getItemStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
            String bt = null;
            if (bauble != null) {
                bt = "name." + bauble.getBaubleType(event.getItemStack()).name();
                event.getToolTip().add(TextFormatting.GOLD + I18n.format(bt));
            }
        }
    }

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        for (BaubleType type : BaubleType.values()) {
            map.registerSprite(new ResourceLocation(Baubles.MODID, "gui/slots/" + type.name));
        }
    }

    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiContainer) {
            GuiContainer gui = (GuiContainer) event.getGui();
            if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiPlayerExpanded) {
                if (Config.baublesButton) {
                    event.getButtonList().add(new GuiBaublesButton(55, gui, 64, 9, I18n.format((event.getGui() instanceof GuiInventory) ? "button.baubles" : "button.normal")));
                }
                if (Config.baublesTab) {
                    event.getButtonList().add(new GuiBaublesTabButton(60, gui, Config.invPosX, 0));
                    event.getButtonList().add(new GuiBaublesTabButton(60, gui, Config.babPosX, 1));
                }
            }
            if (event.getGui() instanceof GuiBaublesTab) {
                event.getButtonList().add(new GuiBaublesTabButton(60, gui, Config.invPosX, 0));
                event.getButtonList().add(new GuiBaublesTabButton(60, gui, Config.babPosX, 1));
            }
/*            if (event.getGui() instanceof GuiContainerCreative) {
                if (Config.baublesButton) {
                    event.getButtonList().add(new GuiBaublesButton(55, gui, 95, 6, I18n.format((event.getGui() instanceof GuiInventory) ? "button.baubles" : "button.normal")));
                }
            }*/
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        if (ClientProxy.KEY_BAUBLES.isPressed()) {
            mc.displayGuiScreen(new GuiPlayerExpanded(player));
        }
        if (ClientProxy.KEY_BAUBLES_TAB.isPressed()) {
            mc.displayGuiScreen(new GuiBaublesTab(player));
        }
    }
}
