package baubles.mixin.early.vanilla;

import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPlayer.class)
public interface AccessorRenderPlayer {
    @Accessor("smallArms")
    boolean isSlim();
}
