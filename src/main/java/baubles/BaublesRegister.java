package baubles;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.config.json.JsonHelper;
import baubles.common.items.BaubleElytra;
import baubles.common.items.ItemRing;
import baubles.common.items.ItemTire;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class BaublesRegister {
    public BaublesRegister() {
    }

    public static void registerItems() {
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(IBauble.class::isInstance)
                .forEach(ItemsData::registerBauble);

        if (Config.ModItems.elytraBauble) {
            BaubleElytra elytra = new BaubleElytra();
            ItemsData.registerBauble(Items.ELYTRA, elytra);
            ItemsData.registerRender(Items.ELYTRA, elytra);
        }

        try {
            JsonHelper.jsonToItem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerTypes() {
        for (BaubleType type : BaubleType.values()) {
            int value = Config.Slots.getCfgAmount(type.toString());
            TypesData.registerBauble(type.getExpansion().setAmount(value));
        }
        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) {
            TypesData.Preset.ELYTRA.setAmount(1);
        }
        else TypesData.Preset.ELYTRA.setAmount(0);

        try {
            List<BaubleTypeEx> types = JsonHelper.jsonToType();
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
        TypesData.applyToTypes(types::add);
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
        TypesData.applyToTypes(trinket::addOriSlots);
    }

    @Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
    public static class ModItems {
        public static final Item ring = new ItemRing().setRegistryName("ring");
        public static final Item tire = new ItemTire().setRegistryName("tire");

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            if (Config.ModItems.itemRing) event.getRegistry().register(ring);
            if (Config.ModItems.testItem) event.getRegistry().register(tire);
        }
    }

    @SubscribeEvent
    public static void createRegistry(RegistryEvent.NewRegistry event) {
        TypesData.create();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegistering(RegistryEvent.Register<BaubleTypeEx> event) {
        registerTypes();
        loadValidSlots();
        registerItems();
        Config.setupBlacklist();
    }
}
