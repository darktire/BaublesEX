package baubles.mixin;

import baubles.common.util.FlyingHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

public abstract class ElytraMixin {
    @Mixin(EntityPlayerSP.class)
    public abstract static class PlayerMixin extends EntityLivingBase {
        public PlayerMixin(World worldIn) { super(worldIn); }

        @ModifyVariable(method = "onLivingUpdate", at = @At("STORE"), ordinal = 0)
        private ItemStack injected(ItemStack stack) {
            return FlyingHelper.elytraInBaubles(stack, this);
        }
    }

    @Mixin(EntityLivingBase.class)
    public abstract static class EntityMixin extends Entity {
        public EntityMixin(World worldIn) { super(worldIn); }

        @ModifyVariable(method = "updateElytra", at = @At("STORE"), ordinal = 0)
        private ItemStack injected(ItemStack stack) {
            return FlyingHelper.elytraInBaubles(stack, (EntityLivingBase) (Entity) this);
        }
    }

    @Mixin(NetHandlerPlayServer.class)
    public abstract static class NetMixin implements INetHandlerPlayServer, ITickable {
        @Shadow public EntityPlayerMP player;

        @ModifyVariable(method = "processEntityAction", at = @At("STORE"), ordinal = 0)
        private ItemStack injected(ItemStack stack) {
            return FlyingHelper.elytraInBaubles(stack, this.player);
        }
    }
}
