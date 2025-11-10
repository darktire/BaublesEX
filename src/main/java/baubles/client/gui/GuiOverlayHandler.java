package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.client.gui.element.ElementButton;
import baubles.common.config.Config;
import baubles.compat.jei.IArea;
import baubles.util.HookHelper;
import cursedflames.bountifulbaubles.block.GuiReforger;
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
import org.lwjgl.input.Mouse;
import vazkii.botania.client.gui.box.GuiBaubleBox;

import java.lang.reflect.Method;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class GuiOverlayHandler {
    private static final WeakHashMap<GuiScreen, GuiExpanded> EXPANSION = new WeakHashMap<>();
    private static final boolean BOTANIA = HookHelper.isModLoaded("botania");
    private static final boolean BOUNTIFUL_BAUBLES = HookHelper.isModLoaded("bountifulbaubles");
    private static final Method RENDER_TOOLTIP = ObfuscationReflectionHelper.findMethod(GuiContainer.class, "func_191948_b", void.class, int.class, int.class);

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        GuiScreen gui = event.getGui();
        if (!isTarget(gui)) return;
        GuiExpanded ex = EXPANSION.get(gui);
        if (ex == null) {
            ex = GuiExpanded.create( Minecraft.getMinecraft().player);
            EXPANSION.put(gui, ex);
        }
    }

    @SubscribeEvent
    public static void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiContainer) {
            GuiContainer guiContainer = (GuiContainer) gui;
            if (gui instanceof GuiInventory) {
                if (Config.Gui.baublesButton) {
                    event.getButtonList().add(new ElementButton(55, guiContainer, 64, 9, I18n.format("button.baubles")));
                }
            }
            if (gui instanceof GuiContainerCreative) {
                if (Config.Gui.baublesButton) {
                    event.getButtonList().add(new ElementButton(55, guiContainer, 95, 6, I18n.format("button.baubles")));
                }
            }
        }

        initExpansion(gui);
    }

    private static void initExpansion(GuiScreen gui) {
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.get(gui);
        if (ex != null) {
            ex.initScreen(gui.mc, gui.width, gui.height);
            GuiContainer guiContainer = (GuiContainer) gui;
            ex.setOcclusion(guiContainer.inventorySlots.inventorySlots, guiContainer.getGuiLeft(), guiContainer.getGuiTop());
        }
    }

    @SubscribeEvent
    public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post e) throws Throwable {
        GuiScreen gui = e.getGui();
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.get(gui);
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
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.get(gui);
        if (ex == null || ex.mc == null) return;

        int x = Mouse.getEventX();
        int y = Mouse.getEventY();

        if (ex.isPointed(x, y)) {
            try {
                ex.handleMouseInput();
                e.setCanceled(true);
            } catch (Throwable ignore) {}
        }
    }

    @SubscribeEvent
    public static void onGuiClose(GuiCloseEvent e) {
        GuiScreen gui = e.getGui();
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.remove(gui);
        if (ex != null) {
            ex.onGuiClosed();
        }
    }

    public static boolean isTarget(GuiScreen gui) {
        return BOTANIA && gui instanceof GuiBaubleBox
                || BOUNTIFUL_BAUBLES && gui instanceof GuiReforger;
    }

    public static IArea getExpansion(GuiScreen gui) {
        return EXPANSION.get(gui);
    }
}
