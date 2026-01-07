package baubles.compat.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import thaumicperiphery.ModContent;

public class PauldronRender implements IRenderBauble {
    private final ModelBauble model;
    private final RenderType type;

    public static final ImmutableList<IRenderBauble> PAULDRON = ImmutableList.of(
            new PauldronRender(new ModelPauldron.Body(ModContent.pauldron), RenderType.BODY),
            new PauldronRender(new ModelPauldron.Arm(ModContent.pauldron), RenderType.ARM_RIGHT)
    );

    public static final ImmutableList<IRenderBauble> PAULDRON_REPULSION = ImmutableList.of(
            new PauldronRender(new ModelPauldron.Body(ModContent.pauldron_repulsion), RenderType.BODY),
            new PauldronRender(new ModelPauldron.Arm(ModContent.pauldron_repulsion), RenderType.ARM_RIGHT)
    );

    private PauldronRender(ModelBauble model, RenderType type) {
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
