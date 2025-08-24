package baubles.common.util;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.Config;
import baubles.common.items.ItemRing;
import baubles.common.items.ItemTire;
import me.paulf.wings.server.item.ItemWings;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BaublesRegistry {
    public static BaubleTypeEx TRINKET = BaubleType.TRINKET.getExpansion();

    public BaublesRegistry() {
        registerBaubles();
        loadValidSlots();
    }

    public void registerItems() {
        for (Item item : Item.REGISTRY) {
            if (item instanceof IBauble) {
                ItemsData.registerBauble(item, (IBauble) item);
            }
            else if (Loader.isModLoaded("wings") && item instanceof ItemWings) {
                ItemsData.registerBauble(item, TypesData.getTypeByName("body"));
            }
        }
        if (Config.ModItems.elytraBauble) ItemsData.registerBauble(Items.ELYTRA, TypesData.getTypeByName(Config.ModItems.elytraSlot));

        try {
            List<BaublesWrapper> items = Config.json.jsonToItem();
            if (items != null) {
                items.forEach(wrapper -> ItemsData.registerBauble(wrapper.getItem(), wrapper));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerBaubles() {
        for (BaubleType type : BaubleType.values()) {
            int value = Config.Slots.getCfgAmount(type.toString());
            TypesData.registerBauble(type.getExpansion().setAmount(value));
        }
        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) {
            BaubleType.ELYTRA.getExpansion().setAmount(1);
        }

        try {
            List<BaubleTypeEx> types = Config.json.jsonToType();
            if (types != null) {
                types.forEach(TypesData::registerBauble);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadValidSlots() {
        int pointer = 0;
        List<BaubleTypeEx> types = new ArrayList<>();
        Iterator<BaubleTypeEx> iterator = TypesData.iterator();
        iterator.forEachRemaining(types::add);
        types.sort(Collections.reverseOrder());
        TypesData.initLazyList();
        for (BaubleTypeEx type : types) {
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
