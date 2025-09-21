package baubles.compat.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.compat.CommonRcs;
import cursedflames.bountifulbaubles.BountifulBaubles;
import net.minecraft.util.ResourceLocation;

public class Resources extends CommonRcs {
    public static final ResourceLocation CROSS_TEXTURE = getLoc("amulet_cross.png");
    public static final ResourceLocation EMPTY_TEXTURE = getLoc("amulet_sin_empty.png");
    public static final ResourceLocation GLUTTONY_TEXTURE = getLoc("amulet_sin_gluttony.png");
    public static final ResourceLocation PRIDE_TEXTURE = getLoc("amulet_sin_pride.png");
    public static final ResourceLocation WRATH_TEXTURE = getLoc("amulet_sin_wrath.png");

    public static final ResourceLocation SUNGLASSES_TEXTURE = getLoc("sunglasses_layer_1.png");

    public static final ModelBauble SUNGLASSES = new ModelSunglasses();

    static ResourceLocation getLoc(String path) {
        return getLoc(BountifulBaubles.MODID, BountifulBaubles.ARMOR_TEXTURE_PATH, path);
    }
}
