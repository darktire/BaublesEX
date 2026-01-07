package baubles.compat.rlartifacts;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class CloakRender implements IRenderBauble {
    private final ModelBauble model;
    private final RenderType type;

    public static final ImmutableList<IRenderBauble> SUB = ImmutableList.of(
            new CloakRender(Resources.CLOAK_MODEL_UP, IRenderBauble.RenderType.HEAD),
            new CloakRender(Resources.CLOAK_MODEL_DOWN, IRenderBauble.RenderType.BODY)
    );

    private CloakRender(ModelBauble model, RenderType type) {
        this.model = model;
        this.type = type;
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.model;
    }

    @Override
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.type;
    }
}
