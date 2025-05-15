package baubles.api.cap;

import baubles.api.IBauble;
import net.minecraft.item.Item;

import java.util.HashMap;

public class BaubleItemRegister {
    protected static HashMap<Item, IBauble> baubleItems = new HashMap<>();

    public void registerItem(Item item, IBauble bauble) {
        baubleItems.put(item, bauble);
    }

    public static IBauble getIBauble(Item item) {
        if (item instanceof IBauble) return (IBauble) item;
        return baubleItems.get(item);
    }
}
