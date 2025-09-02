package baubles.mixin.early.vanilla;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderItem.class)
public interface AccessorRenderItem {
    @Invoker("renderEffect")
    void renderItemGlint(IBakedModel model);
}
