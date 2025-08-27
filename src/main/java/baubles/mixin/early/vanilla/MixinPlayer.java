package baubles.mixin.early.vanilla;

import baubles.util.IPlayerTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayer.class)
@Implements(@Interface(iface = IPlayerTarget.class, prefix = "baubles$"))
public abstract class MixinPlayer {
    @Unique
    private EntityLivingBase baubles$target;

    @Unique
    public void baubles$setTarget(EntityLivingBase target) {
        this.baubles$target = target;
    }

    @Unique
    public EntityLivingBase baubles$getTarget() {
        EntityLivingBase target = this.baubles$target;
        this.baubles$target = null;
        return target;
    }
}
