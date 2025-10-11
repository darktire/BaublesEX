package baubles.util;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.items.BaubleElytra;
import baubles.compat.ModOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        boolean unusable = using && stack.getItem() instanceof ItemElytra && !ItemElytra.isUsable(stack);
        boolean toFind = !(stack.getItem() instanceof ItemElytra) || unusable;
        if (Config.ModItems.elytraBauble && toFind && BaubleElytra.isWearing(entity, using)) {
            return BaubleElytra.getWearing(entity, using);
        }
        return stack;
    }

    public static boolean tryEquipping(EntityPlayer playerIn, ItemStack stack) {
        IBauble bauble = BaublesApi.toBauble(stack);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        for (BaubleTypeEx type : bauble.getTypes(stack)) {
            for (int i = 0, s = baubles.getSlots(); i < s; i++) {
                boolean match = baubles.getTypeInSlot(i) == type || type == TypesData.Preset.TRINKET;
                if (match && baubles.getStackInSlot(i).isEmpty()) {
                    baubles.setStackInSlot(i, stack.copy());
                    if (!playerIn.capabilities.isCreativeMode) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                    }
                    bauble.onEquipped(stack, playerIn);
                    return true;
                }
            }
        }
        return false;
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

    public static boolean isModLoaded(ArrayList<String> mods) {
        for (String mod : mods) {
            if (!isModLoaded(mod)) return false;
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

    public static boolean doApplyMixin(String modid) {
        switch (modid) {
//            case "aether_legacy": return Compat.aether;
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

                if (!isModLoaded(mods)) continue;
                if (client && FMLCommonHandler.instance().getSide() == Side.SERVER) continue;

                String className = data.getClassName();
                Class<?> clazz = Class.forName(className, false, mcl);
                MinecraftForge.EVENT_BUS.register(clazz);
            } catch (ClassNotFoundException e) {
                BaublesApi.log.warn("An error occurred trying to load the compat event {} for mod {}", data.getClassName(), data.getAnnotationInfo().get("value"));
            }
        }
    }
}
