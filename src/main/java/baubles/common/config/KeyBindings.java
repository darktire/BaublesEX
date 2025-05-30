package baubles.common.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    private static final String category = "key.categories.baubles";
    public static final KeyBinding KEY_BAUBLES = new KeyBinding("keybind.baublesinventory", Keyboard.KEY_B, category);
    public static final KeyBinding KEY_BAUBLES_TAB = new KeyBinding("keybind.baublestab", Keyboard.KEY_NONE, category);


    public KeyBindings() {
        init();
    }

    private void init() {
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
        ClientRegistry.registerKeyBinding(KEY_BAUBLES_TAB);
    }
}
