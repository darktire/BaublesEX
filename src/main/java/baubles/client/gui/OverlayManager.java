package baubles.client.gui;

import baubles.common.config.Config;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class OverlayManager {
    private static final List<Class<?>> GUIS = new ArrayList<>();
    private static final WeakHashMap<GuiScreen, GuiOverlay> OVERLAYS = new WeakHashMap<>();

    public static void registerGui(Class<?> cls) {
        GUIS.add(cls);
    }

    public static void setOverlay(GuiScreen gui, GuiOverlay ex) {
        OVERLAYS.put(gui, ex);
    }

    public static GuiOverlay getOverlay(GuiScreen gui) {
        return OVERLAYS.get(gui);
    }

    public static boolean isCovered(GuiScreen gui) {
        return OVERLAYS.containsKey(gui);
    }

    public static GuiOverlay remove(GuiScreen gui) {
        return OVERLAYS.remove(gui);
    }

    public static boolean isTarget(GuiScreen gui) {
        return Config.Gui.isTarget(gui) || GUIS.stream().anyMatch(cls -> cls.isInstance(gui));
    }
}
