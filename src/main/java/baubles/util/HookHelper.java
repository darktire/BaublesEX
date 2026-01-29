package baubles.util;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.config.Config;
import baubles.common.items.BaubleElytra;
import baubles.compat.ModOnly;
import baubles.compat.config.Compat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(String.format("%s.%s not found", clazz.getName(), fieldName), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object instance) {
        try {
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("cannot get %s", field.getName()), e);
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
        AbstractWrapper bauble = BaublesApi.toBauble(stack);
        if (bauble == null) return null;
        return bauble.getTypes(stack).get(0);
    }
}
