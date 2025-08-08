package baubles.mixin;

import baubles.common.util.FlyingHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Mixin(LayerCape.class)
    public abstract static class CapeMixin implements LayerRenderer<AbstractClientPlayer> {
        @Unique private AbstractClientPlayer CapeMixin$player;

        @Inject(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At("HEAD"))
        private void getPlayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
            this.CapeMixin$player = entitylivingbaseIn;
        }


        @ModifyVariable(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At("STORE"), ordinal = 0)
        private ItemStack injected(ItemStack stack) {
            return FlyingHelper.elytraInBaubles(stack, this.CapeMixin$player);
        }
    }

    @Mixin(LayerElytra.class)
    public abstract static class LayerMixin implements LayerRenderer<AbstractClientPlayer> {
        @Unique private EntityLivingBase LayerMixin$player;

        @Inject(method = "doRenderLayer", at = @At("HEAD"))
        private void getPlayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
            this.LayerMixin$player = entitylivingbaseIn;
        }

        @ModifyVariable(method = "doRenderLayer", at = @At("STORE"), ordinal = 0)
        private ItemStack injected(ItemStack stack) {
            return FlyingHelper.elytraInBaubles(stack, this.LayerMixin$player);
        }
    }
}
