package baubles.compat.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import keletu.enigmaticlegacy.EnigmaticLegacy;
import keletu.enigmaticlegacy.client.ModelHeadBauble;
import net.minecraft.util.ResourceLocation;

public class Resources {
    public static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/layer/amulet_ascension.png");
    public static final ResourceLocation THORN_TEXTURE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/layer/amulet_eldritch.png");

    public static final ResourceLocation OMINOUS_MASK = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/layer/half_heart_mask.png");

    public static final ModelBauble MASK_MODEL = new ModelBauble(new ModelHeadBauble());
}
