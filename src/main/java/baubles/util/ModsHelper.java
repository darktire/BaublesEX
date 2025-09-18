package baubles.util;

import baubles.Baubles;
import baubles.compat.ModOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ModsHelper {
    public static final Map<String, Boolean> INFO = new HashMap<>();

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

    public static void patchModsEvents(ASMDataTable table) {
        Set<ASMDataTable.ASMData> list = table.getAll(ModOnly.class.getName());
        ModClassLoader mcl = Loader.instance().getModClassLoader();

        for (ASMDataTable.ASMData data : list) {
            try {
                String mod = (String) data.getAnnotationInfo().get("value");
                boolean client = Boolean.TRUE.equals(data.getAnnotationInfo().get("client"));

                if (!isModLoaded(mod)) continue;
                if (client && FMLCommonHandler.instance().getSide() == Side.SERVER) continue;

                String className = data.getClassName();
                Class<?> clazz = Class.forName(className, false, mcl);
                MinecraftForge.EVENT_BUS.register(clazz);
            } catch (ClassNotFoundException e) {
                Baubles.log.warn("An error occurred trying to load the compat event {} for mod {}", data.getClassName(), data.getAnnotationInfo().get("value"));
            }
        }
    }
}
