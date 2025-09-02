package baubles;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import baubles.common.config.Config;
import baubles.common.items.BaubleElytra;
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

public class BaublesRegister {
    private static final boolean WINGS = Loader.isModLoaded("wings");

    public BaublesRegister() {
    }

    public static void registerItems() {
        Item.REGISTRY.iterator().forEachRemaining(item -> {
            if (item instanceof IBauble) {
                ItemsData.registerBauble(item, (IBauble) item);
            }
            else if (WINGS && item instanceof ItemWings) {
                ItemsData.registerBauble(item, TypesData.Preset.BODY);
            }
        });
        if (Config.ModItems.elytraBauble) {
            BaubleElytra elytra = new BaubleElytra();
            ItemsData.registerBauble(Items.ELYTRA, (IBauble) elytra);
            ItemsData.registerBauble(Items.ELYTRA, (IRenderBauble) elytra);
        }

        try {
            List<BaublesWrapper> items = Config.json.jsonToItem();
            if (items != null) {
                items.forEach(wrapper -> ItemsData.registerBauble(wrapper.getItem(), wrapper));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerBaubles() {
        for (BaubleType type : BaubleType.values()) {
            int value = Config.Slots.getCfgAmount(type.toString());
            TypesData.registerBauble(type.getExpansion().setAmount(value));
        }
        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) {
            TypesData.Preset.ELYTRA.setAmount(1);
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

    public static void loadValidSlots() {
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

        BaubleTypeEx trinket = TypesData.Preset.TRINKET;
        iterator.forEachRemaining(trinket::addOriSlots);
    }

    @Mod.EventBusSubscriber
    public static class ModItems {
        public static final Item ring = new ItemRing().setRegistryName("ring");
        public static final Item tire = new ItemTire().setRegistryName("tire");

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            if (Config.ModItems.itemRing) event.getRegistry().register(ring);
            if (Config.ModItems.testItem) event.getRegistry().register(tire);
        }
    }
}
