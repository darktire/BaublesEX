package baubles.client.gui;

import baubles.api.BaublesApi;
import baubles.client.gui.element.ElementButton;
import baubles.common.config.Config;
import baubles.compat.jei.IArea;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.client.gui.box.GuiBaubleBox;

import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class GuiOverlayHandler {
    private static final WeakHashMap<GuiScreen, GuiExpanded> EXPANSION = new WeakHashMap<>();

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

        expand(gui);
    }

    private static void expand(GuiScreen gui) {
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.get(gui);
        if (ex == null) {
            EntityLivingBase owner = ((ContainerBaubleBox) (((GuiBaubleBox) gui).inventorySlots)).baubles.getOwner();
            ex = GuiExpanded.create(owner);
            EXPANSION.put(gui, ex);
        }
        ex.initScreen(gui.mc, gui.width, gui.height);
    }

    @SubscribeEvent
    public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post e) {
        GuiScreen gui = e.getGui();
        if (!isTarget(gui)) return;

        GuiExpanded ex = EXPANSION.get(gui);
        if (ex != null) {
            int x = e.getMouseX();
            int y = e.getMouseY();
            float partial = e.getRenderPartialTicks();

            ex.drawAll(x, y, partial);
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

    public static boolean isTarget(GuiScreen gui) {
        return gui instanceof GuiBaubleBox;
    }

    public static IArea getExpansion(GuiScreen gui) {
        return EXPANSION.get(gui);
    }
}
