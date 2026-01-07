package baubles.compat.thaumicperiphery;

import baubles.compat.CommonRcs;
import net.minecraft.util.ResourceLocation;
import thaumicperiphery.ThaumicPeriphery;

public class Resources extends CommonRcs {
    public static final ResourceLocation GIRDLE_MUNDANE = getLoc("girdle_mundane.png");
    public static final ResourceLocation GIRDLE_FANCY = getLoc("girdle_fancy.png");
    public static final ResourceLocation AMULET_MUNDANE = getLoc("amulet_mundane.png");
    public static final ResourceLocation AMULET_FANCY = getLoc("amulet_fancy.png");
    public static final ResourceLocation AMULET_VIS_STONE = getLoc("amulet_vis_stone.png");
    public static final ResourceLocation AMULET_VIS = getLoc("amulet_vis.png");
    public static final ResourceLocation PAULDRON = getLoc("pauldron.png");
    public static final ResourceLocation PAULDRON_REPULSION = getLoc("pauldron_repulsion.png");
    public static final ResourceLocation FOCUS_POUCH = getLoc("focus_pouch.png");

    public static final ResourceLocation ARROW_TEXTURE = getLoc("magic_arrow.png");

    public static final ResourceLocation VIS_PHYLACTERY = getLoc("vis_phylactery.png");

    public static final ModelQuiver MODEL_QUIVER = new ModelQuiver();

    static ResourceLocation getLoc(String path) {
        return getLoc(ThaumicPeriphery.MODID, MODEL_TEXTURE, path);
    }
}
