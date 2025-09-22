package baubles.compat.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.compat.CommonRcs;
import keletu.enigmaticlegacy.EnigmaticLegacy;
import keletu.enigmaticlegacy.client.ModelHeadBauble;
import net.minecraft.util.ResourceLocation;

public class Resources extends CommonRcs {
    public static final ResourceLocation FLAME_TEXTURE = getLoc("amulet_ascension.png");
    public static final ResourceLocation THORN_TEXTURE = getLoc("amulet_eldritch.png");

    public static final ResourceLocation OMINOUS_MASK = getLoc("half_heart_mask.png");

    public static final ModelBauble MASK_MODEL = new ModelBauble(new ModelHeadBauble());


    static ResourceLocation getLoc(String path) {
        return getLoc(EnigmaticLegacy.MODID, LAYER_TEXTURE_PATH, path);
    }
}
