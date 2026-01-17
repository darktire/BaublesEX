package baubles.util;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.IWrapper;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.config.Config;
import baubles.common.config.json.ConversionHelper;
import baubles.common.items.BaubleElytra;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import baubles.compat.ModOnly;
import baubles.compat.config.Compat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.*;

public class HookHelper {

    public static final Map<String, Boolean> INFO = new HashMap<>();

    public static ItemStack universalCondition(EntityLivingBase entity, EntityEquipmentSlot slot, boolean using) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        boolean isElytra = stack.getItem() instanceof ItemElytra;
        boolean unusable = using && isElytra && !ItemElytra.isUsable(stack);
        boolean toFind = !isElytra || unusable;
        if (Config.ModItems.elytraBauble && toFind && BaubleElytra.isWearing(entity, using)) {
            return BaubleElytra.getWearing(entity, using);
        }
        return stack;
    }

    public static void tryEquipping(EntityPlayer playerIn, ItemStack stack) {
        tryEquipping(playerIn, EnumHand.MAIN_HAND, stack);
    }

    public static boolean tryEquipping(EntityPlayer playerIn, EnumHand hand, ItemStack stack) {
        IBauble bauble = BaublesApi.toBauble(stack);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        List<BaubleTypeEx> types = bauble.getTypes(stack);
        for (int i = 0, s = baubles.getSlots(); i < s; i++) {
            boolean match = baubles.getTypeInSlot(i).contains(types);
            if (match && baubles.getStackInSlot(i).isEmpty()) {
                stack = stack.copy();
                baubles.setStackInSlot(i, stack);
                if (!playerIn.capabilities.isCreativeMode) {
                    if (hand == EnumHand.MAIN_HAND) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                    }
                    else {
                        playerIn.inventory.offHandInventory.set(0, ItemStack.EMPTY);
                    }
                }
                bauble.onEquipped(stack, playerIn);
                if (!playerIn.world.isRemote) {
                    PacketSync pkt = PacketSync.S2CPack(playerIn, i, stack, -1);
                    PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) playerIn);
                }
                return true;
            }
        }
        return false;
    }

    public static void configSlot(String typeName, int n, boolean modifying) {
        Property property = Config.Slots.getCategory().get(typeName + "Slot");
        if (property == null) {
            try {
                List<BaubleTypeEx> types = ConversionHelper.jsonToType(BaubleTypeEx.class);
                for (BaubleTypeEx type : types) {
                    if (type.getName().equals(typeName)) {
                        if (modifying) {
                            n += type.getAmount();
                        }
                        type.setAmount(n);
                    }
                }
                ConversionHelper.typeToJson(types, false);
            } catch (Throwable e) {
                BaublesApi.log.error(e);
            }
        }
        else {
            if (modifying) {
                n += property.getInt();
            }
            property.set(n);
            Config.saveConfig();
            Config.syncToBaubles();
        }
    }

    public static Field getField(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className, false, Loader.instance().getModClassLoader());
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(String.format("%s.%s not found", className, fieldName), e);
        }
    }

    public static Object getValue(Field field, Object instance) {
        try {
            return field.getFloat(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //-----------------------------------LOADING MODS-----------------------------------//

    public static boolean isModLoaded(String mod) {
        Boolean loaded = INFO.get(mod);
        if (loaded == null) {
            switch (mod) {
                case "rlartifacts" : loaded = checkByName("RLArtifacts"); break;
                case "iceandfire" : loaded = checkByClass("com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble"); break;
                default: loaded = Loader.isModLoaded(mod);
            }
            INFO.put(mod, loaded);
        }
        return loaded;
    }

    public static boolean doApply(ArrayList<String> mods) {
        for (String mod : mods) {
            if (!isModLoaded(mod) || !getConfig(mod)) return false;
        }
        return true;
    }

    private static boolean checkByName(String modName) {
        return Loader.instance().getModList().stream().anyMatch(mod -> mod.getName().equals(modName));
    }

    private static boolean checkByClass(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static String getTargetModId(String mixinClassName) {
        String substring = mixinClassName.substring(19);
        return substring.substring(0, substring.indexOf('.'));
    }

    public static boolean getConfig(String modid) {
        switch (modid) {
            case "aether_legacy": return Compat.aether;
            case "botania": return Compat.botania;
            default: return true;
        }
    }

    public static void patchModsEvents(ASMDataTable table) {
        Set<ASMDataTable.ASMData> list = table.getAll(ModOnly.class.getName());
        ModClassLoader mcl = Loader.instance().getModClassLoader();

        for (ASMDataTable.ASMData data : list) {
            try {
                @SuppressWarnings("unchecked")
                ArrayList<String> mods = (ArrayList<String>) data.getAnnotationInfo().get("value");
                boolean client = Boolean.TRUE.equals(data.getAnnotationInfo().get("client"));

                if (!doApply(mods)) continue;
                if (client && FMLCommonHandler.instance().getSide() == Side.SERVER) continue;

                String className = data.getClassName();
                Class<?> clazz = Class.forName(className, false, mcl);
                MinecraftForge.EVENT_BUS.register(clazz);
            } catch (ClassNotFoundException e) {
                BaublesApi.log.warn("An error occurred trying to load the compat event {} for mod {}", data.getClassName(), data.getAnnotationInfo().get("value"));
            }
        }
    }

    //-----------------------------------FOR HARD CODING-----------------------------------//

    public static ItemStack getStack(IBaublesItemHandler baubles, Object symbol) {// todo cache
        int idx = 0;
        if (symbol instanceof Class) {
            while (idx < baubles.getSlots()) {
                ItemStack get = baubles.getStackInSlot(idx++);
                if (((Class<?>) symbol).isInstance(get.getItem())) return get;
            }
            return ItemStack.EMPTY;
        }
        else {
            idx = baubles.indexOf(symbol, idx);
            return baubles.getStackInSlot(idx);
        }
    }

    public static BaubleTypeEx getMainType(ItemStack stack) {
        IWrapper bauble = BaublesApi.toBauble(stack);
        if (bauble == null) return null;
        return bauble.getTypes(stack).get(0);
    }
}
