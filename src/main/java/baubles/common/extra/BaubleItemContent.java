package baubles.common.extra;

import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaubleItemRegister;
import baubles.common.Baubles;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Set;

public class BaubleItemContent extends BaubleItemRegister {

    private static Set<String> items;

    public BaubleItemContent() {
        Baubles.jsonHelper.jsonToItem();
        items = regHelper.keySet();
        regHelper.forEach(this::registerItem);
    }

    public void registerItem(String itemName, String type) {
        Item item = Item.getByNameOrId(itemName);
        BaubleItem baubleItem = new BaubleItem(item, Baubles.baubles.getBaubles(type));
        registerItem(item, baubleItem);
//        registerItem(elytra, new BaubleItem(elytra, Baubles.baubles.getBaubles("body")));
    }
    public static boolean isBauble(Item item) {
        boolean isDefault = item instanceof IBauble;
        boolean isExtra = items.contains(String.valueOf(item.getRegistryName()));
        return isDefault || isExtra;
    }

    public static void setRegHelper(HashMap<String, String> map) {
        regHelper = map;
    }
}
