package baubles.common.util;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.util.ItemsData;
import baubles.api.util.TypesData;
import baubles.common.Config;
import baubles.common.items.ItemRing;
import baubles.common.items.ItemTire;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class BaublesRegistry {
    public static BaubleTypeEx TRINKET = BaubleType.TRINKET.getNewType();

    public BaublesRegistry() {
        registerBaubles();
        loadValidSlots();
    }

    public void registerItems() {
        for (Item item : Item.REGISTRY) {
            if (item instanceof IBauble) {
                ItemsData.registerBauble(item);
            }
        }
        if (Config.ModItems.elytraBauble) ItemsData.registerBauble(Items.ELYTRA, TypesData.getTypeByName(Config.ModItems.elytraSlot));
    }

    public void registerBaubles() {
        int amount = 0;
        for (BaubleType type : BaubleType.values()) {
            int value = Config.Slots.getCfgAmount(type.toString());
            if (type == BaubleType.TRINKET) value = 0;
            TypesData.registerBauble(type.getNewType(), value);
            amount += value;
        }

        int trinket = Config.Slots.getCfgAmount(BaubleType.TRINKET.toString());
        if (trinket > amount) {
            TypesData.registerBauble(BaubleType.TRINKET.getNewType(), trinket - amount);
        }

        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) TypesData.registerBauble("elytra", 1);
    }

    public void loadValidSlots() {
        int pointer = 0;
        Iterator<BaubleTypeEx> iterator = TypesData.iterator();
        TypesData.initLazyList();
        while (iterator.hasNext()) {
            BaubleTypeEx type = iterator.next();
            int amount = type.getAmount();
            for (int i = 0; i < amount; i++) {
                type.addOriSlots(pointer + i);
                TypesData.addLazySlots(type);
            }
            pointer += amount;
        }
        TypesData.setSum(pointer);

        BaubleTypeEx trinket = TRINKET;
        iterator.forEachRemaining(trinket::addOriSlots);

    }

    @Mod.EventBusSubscriber
    public static class ModItems {
        public static final Item Ring = new ItemRing().setRegistryName("ring");
        public static final Item Tire = new ItemTire().setRegistryName("tire");

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(Ring);
            if (Config.ModItems.testItem) event.getRegistry().register(Tire);
        }
    }
}
