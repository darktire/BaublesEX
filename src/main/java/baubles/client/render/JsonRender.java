package baubles.client.render;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelBaked;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class JsonRender implements IRenderBauble {

    private final ModelBaked model;
    private final RenderType type;

    public JsonRender(String path, String type) {
        this.model = new ModelBaked(new ModelResourceLocation(path));
        this.type = getType(type);
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.model;
    }

    @Override
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.type;
    }

    private static final Map<String, RenderType> LOOKUP = Arrays.stream(RenderType.values())
            .collect(Collectors.toMap(e -> e.name().toLowerCase(), e -> e));

    private static RenderType getType(String name) {
        return Optional.ofNullable(LOOKUP.get(name.toLowerCase())).orElse(RenderType.BODY);
    }

    public static class More implements IRenderBauble {

        private final List<IRenderBauble> list = new ArrayList<>();

        private More() {}

        @Override
        public List<IRenderBauble> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
            return list;
        }
    }

    public static More of (Set<IRenderBauble> render) {
        More more = new More();
        more.list.addAll(render);
        return more;
    }
}
