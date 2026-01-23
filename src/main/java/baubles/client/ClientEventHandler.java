package baubles.client;

import baubles.BaublesRegister;
import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.event.BaublesRenderEvent;
import baubles.api.registries.TypesData;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.config.Config;
import baubles.common.config.KeyBindings;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
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
import org.lwjgl.input.Keyboard;

import java.util.Collection;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void equipmentRenderEvent(BaublesRenderEvent.InEquipments event) {
        if (event.getStack().getItem() instanceof ItemElytra) {
            event.cancel();
        }
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        registerModel(BaublesRegister.ModItems.ring, 0);
        if (Config.ModItems.testItem) {
            registerModel(BaublesRegister.ModItems.tire, 0);
            registerModel(BaublesRegister.ModItems.tire, 1);
            registerModel(BaublesRegister.ModItems.tire, 2);
            registerModel(BaublesRegister.ModItems.tire, 3);
        }
    }

    private static void registerModel(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && BaublesApi.isBauble(stack)) {
            try {
                AbstractWrapper bauble = BaublesApi.toBauble(stack);
                String base = TextFormatting.GOLD + I18n.format("title.baubles") + ": " +
                        bauble.getTypes(stack).stream()
                            .map(BaubleTypeEx::getTranslateKey)
                            .map(I18n::format)
                            .collect(Collectors.joining(", "));
                event.getToolTip().add(base);
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                    String parents = bauble.getTypes(stack).stream()
                            .map(BaubleTypeEx::getParents)
                            .flatMap(Collection::stream)
                            .distinct()
                            .map(BaubleTypeEx::getTranslateKey)
                            .map(I18n::format)
                            .collect(Collectors.joining(", "));
                    event.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("title.baubles.parents") + ": " + parents);
                }
                event.getToolTip().add("");
                event.getToolTip().add(I18n.format("title.baubles.tooltip"));
                bauble.getModules(stack, event.getEntityLiving()).forEach(module -> event.getToolTip().add(module.getDescription()));
            } catch (Exception e) {
                throw new RuntimeException(String.format("baubles_cap for %s is outdated", stack.getItem().getRegistryName()));
            }
        }
    }

    @SubscribeEvent
    public static void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        TypesData.applyToTypes(type -> map.registerSprite(new ResourceLocation(type.getTexture())));
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        if (KeyBindings.KEY_BAUBLES.isKeyDown()) {
            mc.displayGuiScreen(new GuiPlayerExpanded(player, player));
            PacketHandler.INSTANCE.sendToServer(new PacketOpen(PacketOpen.Option.EXPANSION));
        }
    }
}