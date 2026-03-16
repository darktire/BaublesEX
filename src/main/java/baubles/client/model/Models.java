package baubles.client.model;

import baubles.api.model.ModelBauble;
import com.github.bsideup.jabel.Desugar;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Models {
    private static ModelManager MODEL_MANAGER;
    private static final Set<String> REG_QUE = new HashSet<>();
    private static final Map<Key, ModelBauble> CACHE = new HashMap<>();

    public static Key wrap(Item item) {
        return new Key(item, 0, null);
    }

    public static Key wrap(Item item, int metadata) {
        return new Key(item, metadata, null);
    }

    public static Key wrap(Item item, int metadata, RenderPlayer renderPlayer) {
        return new Key(item, metadata, renderPlayer);
    }

    @Desugar
    public record Key(Item item, int meta, RenderPlayer renderPlayer) {}

    public static ModelBauble getInstance(Key key, Function<Key, ModelBauble> factory) {
        return CACHE.computeIfAbsent(key, factory);
    }

    public static void setModelManager(ModelManager manager) {
        MODEL_MANAGER = manager;
    }

    public static IBakedModel getBakedModel(ModelResourceLocation mrl) {
        return MODEL_MANAGER.getModel(mrl);
    }

    public static void enqueue(String path) {
        REG_QUE.add(path);
    }

    public static void loadModel() {
        for (String path : REG_QUE) {
            ModelLoader.registerItemVariants(Items.AIR, new ModelResourceLocation(path));
        }
    }
}
