package baubles.client;

import baubles.Baubles;
import baubles.BaublesRegister;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.registries.TypesData;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.config.Config;
import baubles.common.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Baubles.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        registerModel(BaublesRegister.ModItems.Ring);
        if (Config.ModItems.testItem) registerModel(BaublesRegister.ModItems.Tire);
    }

    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty() && BaublesApi.isBauble(event.getItemStack())) {
            IBauble bauble = BaublesApi.toBauble(event.getItemStack());
            StringBuilder tooltip = new StringBuilder(TextFormatting.GOLD + I18n.format("baubles.tooltip") + ": ");
            if (bauble instanceof BaublesWrapper) {
                Iterator<BaubleTypeEx> types = bauble.getBaubleTypes().iterator();
                while (types.hasNext()) {
                    tooltip.append(I18n.format(types.next().getTranslateKey()));
                    if (types.hasNext()) tooltip.append(", ");
                }
            }
            else {
                Baubles.log.warn("bauble cap on " + event.getItemStack().getTranslationKey() + " registered incorrect");
                tooltip.append(I18n.format("name." + bauble.getBaubleType(event.getItemStack()).toString().toLowerCase()));
            }
            event.getToolTip().add(tooltip.toString());
        }
    }

    @SubscribeEvent
    public static void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        Iterator<BaubleTypeEx> types = TypesData.iterator();
        types.forEachRemaining(type -> map.registerSprite(new ResourceLocation(type.getTexture())));
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        if (KeyBindings.KEY_BAUBLES.isPressed()) {
            mc.displayGuiScreen(new GuiPlayerExpanded(player));
        }
    }
}