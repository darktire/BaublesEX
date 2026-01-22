package baubles;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.IBaubleKey;
import baubles.api.attribute.AttributeManager;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import baubles.client.render.ArmorHelper;
import baubles.common.config.Config;
import baubles.common.config.json.ConversionHelper;
import baubles.common.items.BaubleElytra;
import baubles.common.items.ItemRing;
import baubles.common.items.ItemTire;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class BaublesRegister {

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
            ConversionHelper.jsonToItem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTypes() {
        TypesData.Preset.AMULET.setAmount(Config.Slots.getCfgAmount("head"));
        TypesData.Preset.RING.setAmount(Config.Slots.getCfgAmount("ring"));
        TypesData.Preset.BELT.setAmount(Config.Slots.getCfgAmount("belt"));
        TypesData.Preset.TRINKET.setAmount(Config.Slots.getCfgAmount("trinket"));
        TypesData.Preset.HEAD.setAmount(Config.Slots.getCfgAmount("head"));
        TypesData.Preset.BODY.setAmount(Config.Slots.getCfgAmount("body"));
        TypesData.Preset.CHARM.setAmount(Config.Slots.getCfgAmount("charm"));

        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) {
            TypesData.Preset.ELYTRA.setAmount(1);
        }

        try {
            ConversionHelper.jsonToType(BaubleTypeEx.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadValidSlots() {
        TypesData.initOrderList();
        AttributeManager.loadAttributes();
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

    @SubscribeEvent
    public static void beforeRegistering(RegistryEvent.Register<BaubleTypeEx> event) {
        registerItems();
        ItemsData.redirect(BaublesRegister::check, ArmorHelper.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegistering(RegistryEvent.Register<BaubleTypeEx> event) {
        TypesData.registerTypes();
        loadValidSlots();
    }

    private static boolean check(IBaubleKey key) {
        Item item = key.ref().getItem();
        return item instanceof ItemArmor && !(item instanceof IRenderBauble);
    }
}
