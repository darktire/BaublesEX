package baubles.compat.iceandfire;

import baubles.api.model.ModelBauble;
import baubles.compat.CommonRcs;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble;
import net.minecraft.util.ResourceLocation;

public class Resources extends CommonRcs {
    public static final ResourceLocation BLINDFOLD = getLoc("blindfold_layer_1.png");
    public static final ResourceLocation EAR_PLUGS = getLoc("earplugs_layer_1.png");

    public static final ModelBauble Model_HEADWEAR = new ModelBauble(new ModelHeadBauble());

    static ResourceLocation getLoc(String path) {
        return getLoc(IceAndFire.MODID, ARMOR_TEXTURE_PATH, path);
    }
}
