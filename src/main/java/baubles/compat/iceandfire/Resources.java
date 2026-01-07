package baubles.compat.iceandfire;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelInherit;
import baubles.compat.CommonRcs;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble;
import net.minecraft.util.ResourceLocation;

public class Resources extends CommonRcs {

    public static final ModelBauble BLINDFOLD = new ModelInherit(new ModelHeadBauble(), getLoc("blindfold_layer_1.png"));
    public static final ModelBauble EAR_PLUGS = new ModelInherit(new ModelHeadBauble(), getLoc("earplugs_layer_1.png"));

    static ResourceLocation getLoc(String path) {
        return getLoc(IceAndFire.MODID, ARMOR_TEXTURE_PATH, path);
    }
}
