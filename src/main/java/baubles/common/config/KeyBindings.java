package baubles.common.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    private static final String category = "title.baubles";
    public static final KeyBinding KEY_BAUBLES = new KeyBinding(I18n.format("title.baubles.inventory"), Keyboard.KEY_B, category);

    public static void register() {
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
    }
}
