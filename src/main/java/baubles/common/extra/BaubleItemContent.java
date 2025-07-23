package baubles.common.extra;

import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaubleItemRegister;
import baubles.common.Baubles;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Set;

public class BaubleItemContent extends BaubleItemRegister {

    private static Set<String> items;
    public static boolean isInit = false;

    public BaubleItemContent() {
        isInit = true;
//        Baubles.jsonHelper.jsonToItem();
        items = regHelper.keySet();
    }

    public void registerItem(String itemName, String type) {
        Item item = Item.getByNameOrId(itemName);
        BaubleItem baubleItem = new BaubleItem(item, Baubles.baubles.getBaubles(type));
        registerItem(item, baubleItem);
//        registerItem(elytra, new BaubleItem(elytra, Baubles.baubles.getBaubles("body")));
    }

    public void registerItem(Item item) {
        String type = regHelper.get(String.valueOf(item.getRegistryName()));
        BaubleItem baubleItem = new BaubleItem(item, Baubles.baubles.getBaubles(type));
        registerItem(item, baubleItem);
//        registerItem(elytra, new BaubleItem(elytra, Baubles.baubles.getBaubles("body")));
    }
    public static boolean isExtra(Item item) {
        return items.contains(String.valueOf(item.getRegistryName()));
    }

    public static void setRegHelper(HashMap<String, String> map) {
        regHelper = map;
    }
}
