package baubles.common.util;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.util.BaubleItemsContent;
import baubles.api.util.BaublesContent;
import baubles.common.Config;
import baubles.common.items.ItemRing;
import baubles.common.items.ItemTyre;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class BaublesRegistry {

    public BaublesRegistry() {
        registerBaubles();
        loadValidSlots();
    }

    public void registerItems() {
        for (Item item : Item.REGISTRY) {
            if (item instanceof IBauble) {
                BaubleItemsContent.registerBauble(item);
            }
        }
        if (Config.ModItems.elytraBauble) BaubleItemsContent.registerBauble(Items.ELYTRA, BaublesContent.getTypeByName("body"));
    }

    public void registerBaubles() {
        int amount = 0;
        for (BaubleType type : BaubleType.values()) {
            int value = Config.Slots.getCfgAmount(type.toString());
            if (type == BaubleType.TRINKET && !Config.trinketLimit) value = 0;
            BaublesContent.registerBauble(type.getNewType(), value);
            amount += value;
        }
        if (!Config.trinketLimit) {
            int trinket = Config.Slots.getCfgAmount(BaubleType.TRINKET.toString());
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

    @Mod.EventBusSubscriber
    public static class ModItems {
        public static final Item Ring = new ItemRing().setRegistryName("ring");
        public static final Item Tyre = new ItemTyre().setRegistryName("tyre");

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(Ring);
            if (Config.ModItems.testItem) event.getRegistry().register(Tyre);
        }
    }
}
