package baubles.client.gui.event;

import baubles.api.BaublesApi;
import baubles.client.gui.GuiOverlay;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.client.gui.OverlayManager;
import baubles.client.gui.element.ElementButton;
import baubles.common.config.Config;
import baubles.common.config.KeyBindings;
import baubles.common.container.SlotBaubleHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class GuiOverlayHandler {
    private static final Method RENDER_TOOLTIP = ObfuscationReflectionHelper.findMethod(GuiContainer.class, "func_191948_b", void.class, int.class, int.class);

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        GuiScreen gui = event.getGui();
        if (!(Config.Gui.overlay && OverlayManager.isTarget(gui))) return;
        GuiOverlay ex = OverlayManager.getOverlay(gui);
        if (ex == null) {
            OverlayManager.setOverlay(gui, new GuiOverlay(Minecraft.getMinecraft().player));
            PacketHandler.INSTANCE.sendToServer(new PacketOpen(PacketOpen.Option.ENHANCEMENT));
        }
    }

    @SubscribeEvent
    public static void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        initButtons(event, gui);
        initExpansion(gui);
    }

    private static void initButtons(GuiScreenEvent.InitGuiEvent.Post event, GuiScreen gui) {
        if (gui instanceof GuiContainer) {
            if (gui instanceof GuiInventory) {
                if (Config.Gui.baublesButton) {
                    event.getButtonList().add(new ElementButton(55, (GuiInventory) gui, 64, 9, I18n.format("button.baubles")));
                }
            }
            if (gui instanceof GuiContainerCreative) {
                if (Config.Gui.baublesButton) {
                    event.getButtonList().add(new ElementButton(55, (GuiContainer) gui, 95, 6, I18n.format("button.baubles")));
                }
            }
        }
    }

    private static void initExpansion(GuiScreen gui) {
        if (!OverlayManager.isCovered(gui)) return;

        GuiOverlay ex = OverlayManager.getOverlay(gui);
        if (ex != null) {
            ex.initScreen(gui.mc, gui.width, gui.height);
            GuiContainer guiContainer = (GuiContainer) gui;
            ex.setOcclusion(guiContainer.inventorySlots.inventorySlots, guiContainer.getGuiLeft(), guiContainer.getGuiTop());
        }
    }

    @SubscribeEvent
    public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post e) throws Exception {
        GuiScreen gui = e.getGui();
        if (!OverlayManager.isCovered(gui)) return;

        GuiOverlay ex = OverlayManager.getOverlay(gui);
        if (ex != null) {
            int x = e.getMouseX();
            int y = e.getMouseY();
            float partial = e.getRenderPartialTicks();

            ex.drawAll(x, y, partial);
            RENDER_TOOLTIP.invoke(gui, x, y);
        }
    }

    @SubscribeEvent
    public static void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre e) {
        int mouseButton = Mouse.getEventButton();
        if (mouseButton == -1) return;

        GuiScreen gui = e.getGui();
        if (!OverlayManager.isCovered(gui)) return;

        GuiOverlay ex = OverlayManager.getOverlay(gui);
        if (ex == null || ex.mc == null) return;

        int x = Mouse.getEventX();
        int y = Mouse.getEventY();

        if (ex.isPointed(x, y)) {
            try {
                ex.handleMouseInput();
                e.setCanceled(true);
            } catch (Exception ignore) {}
        }
    }

    @SubscribeEvent
    public static void onGuiClose(GuiCloseEvent e) {
        GuiScreen gui = e.getGui();
        if (!OverlayManager.isCovered(gui)) return;

        GuiOverlay ex = OverlayManager.remove(gui);
        if (ex != null) {
            ex.onGuiClosed();
        }
    }

    @SubscribeEvent
    public static void onKeyDown(GuiScreenEvent.KeyboardInputEvent.Post e) {
        if (!Keyboard.getEventKeyState()) return;
        if (Keyboard.getEventKey() != KeyBindings.KEY_BAUBLES.getKeyCode()) return;

        GuiScreen gui = e.getGui();
        if (gui instanceof GuiPlayerExpanded) {
            ((GuiPlayerExpanded) gui).displayNormalInventory();
            PacketHandler.INSTANCE.sendToServer(new PacketOpen(PacketOpen.Option.NORMAL));
        } else if (gui instanceof GuiInventory) {
            PacketHandler.INSTANCE.sendToServer(new PacketOpen(PacketOpen.Option.EXPANSION));
        }

        if (!OverlayManager.isTarget(gui)) return;
        if (OverlayManager.isCovered(gui)) {
            OverlayManager.remove(gui).onGuiClosed();
            ((GuiContainer) gui).inventorySlots.inventorySlots.forEach(slot -> {
                if (slot instanceof SlotBaubleHandler) {
                    ((SlotBaubleHandler) slot).setLocked(false);
                }
            });
        } else {
            GuiOverlay ex = new GuiOverlay(Minecraft.getMinecraft().player);
            OverlayManager.setOverlay(gui, ex);
            initExpansion(gui);
            PacketHandler.INSTANCE.sendToServer(new PacketOpen(PacketOpen.Option.ENHANCEMENT));
        }
    }

}
