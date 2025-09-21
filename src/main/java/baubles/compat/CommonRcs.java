package baubles.compat;

import net.minecraft.util.ResourceLocation;

public class CommonRcs {
    protected static final String MODEL_TEXTURE = "textures/model/";
    protected static final String LAYER_TEXTURE_PATH = "textures/models/layer/";
    protected static final String ARMOR_TEXTURE_PATH = "textures/models/armor/";
    protected static final String ENTITY_LAYER = "textures/entity/layer/";
    protected static ResourceLocation getLoc(String namespace, String prefix, String path) {
        return new ResourceLocation(namespace, prefix + path);
    }
}
