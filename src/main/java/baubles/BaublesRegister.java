package baubles;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.IBaubleKey;
import baubles.api.attribute.AttributeManager;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.common.config.Config;
import baubles.common.config.json.Category;
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
                .forEach(ItemData::registerBauble);

        if (Config.ModItems.elytraBauble) {
            BaubleElytra elytra = new BaubleElytra();
            ItemData.registerBauble(Items.ELYTRA, elytra);
            ItemData.registerRender(Items.ELYTRA, elytra);
        }

        ItemData.backup();

        try {
            ConversionHelper.fromJson(Category.ITEM_DATA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        ItemData.redirect(BaublesRegister::check, ArmorHelper.INSTANCE);
    }

    public static void setTypes() {
        TypeData.Preset.AMULET.setAmount(Config.Slots.getCfgAmount("head"));
        TypeData.Preset.RING.setAmount(Config.Slots.getCfgAmount("ring"));
        TypeData.Preset.BELT.setAmount(Config.Slots.getCfgAmount("belt"));
        TypeData.Preset.TRINKET.setAmount(Config.Slots.getCfgAmount("trinket"));
        TypeData.Preset.HEAD.setAmount(Config.Slots.getCfgAmount("head"));
        TypeData.Preset.BODY.setAmount(Config.Slots.getCfgAmount("body"));
        TypeData.Preset.CHARM.setAmount(Config.Slots.getCfgAmount("charm"));

        if (Config.ModItems.elytraBauble && Config.ModItems.elytraSlot.equals("elytra")) {
            TypeData.Preset.ELYTRA.setAmount(1);
        }

        try {
            ConversionHelper.fromJson(Category.TYPE_DATA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadValidSlots() {
        TypeData.initOrderList();
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
        TypeData.create();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegistering(RegistryEvent.Register<BaubleTypeEx> event) {
        TypeData.registerTypes();
        loadValidSlots();
    }

    private static boolean check(IBaubleKey key) {
        Item item = key.ref().getItem();
        return item instanceof ItemArmor && !(item instanceof IRenderBauble);
    }
}
