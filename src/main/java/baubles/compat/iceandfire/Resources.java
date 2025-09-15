package baubles.compat.iceandfire;

import baubles.api.model.ModelBauble;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble;
import net.minecraft.util.ResourceLocation;

public class Resources {
    public static final ResourceLocation BLINDFOLD = new ResourceLocation(IceAndFire.MODID, "textures/models/armor/blindfold_layer_1.png");
    public static final ResourceLocation EAR_PLUGS = new ResourceLocation(IceAndFire.MODID, "textures/models/armor/earplugs_layer_1.png");

    public static final ModelBauble Model_HEADWEAR =  new ModelBauble(new ModelHeadBauble());
}
