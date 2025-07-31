package baubles.common.util;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.util.BaubleItemsContent;
import baubles.api.util.BaublesContent;
import baubles.common.Config;
import baubles.common.config.cfg.CfgBaubles;
import net.minecraft.item.Item;

import java.util.Iterator;

public class BaublesRegister {

    public BaublesRegister() {
        registerBaubles();
        loadValidSlots();
    }

    public void registerItems() {
        for (Item item : Item.REGISTRY) {
            if (item instanceof IBauble) {
                BaubleItemsContent.registerBauble(item);
            }
        }
    }

    public void registerBaubles() {
        int amount = 0;
        for (BaubleType type : BaubleType.values()) {
            int value = CfgBaubles.getCfgAmount(type.toString());
            if (type.equals(BaubleType.TRINKET) && !Config.trinketLimit) value = 0;
            BaublesContent.registerBauble(type.getNewType(), value);
            amount += value;
        }
        if (!Config.trinketLimit) {
            int trinket = CfgBaubles.getCfgAmount(BaubleType.TRINKET.toString());
            if (trinket > amount) {
                BaublesContent.registerBauble(BaubleType.TRINKET.getNewType(), trinket - amount);
            }
        }
    }

    public void loadValidSlots() {
        int pointer = 0;
        Iterator<BaubleTypeEx> iterator = BaublesContent.iterator();
        BaublesContent.initLazyList();
        while (iterator.hasNext()) {
            BaubleTypeEx type = iterator.next();
            int amount = type.getAmount();
            for (int i = 0; i < amount; i++) {
                type.addOriSlots(pointer + i);
                BaublesContent.addLazySlots(type);
            }
            pointer += amount;
        }
        BaublesContent.setSum(pointer);
        if (!Config.trinketLimit) {
            BaubleTypeEx trinket = BaublesContent.getTypeByName("trinket");
            iterator.forEachRemaining(trinket::addOriSlots);
        }
    }
}
