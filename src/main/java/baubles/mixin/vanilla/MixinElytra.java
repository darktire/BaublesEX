package baubles.mixin.vanilla;

import baubles.common.util.HookHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public abstract class MixinElytra {
    @Mixin(EntityPlayerSP.class)
    public abstract static class MixinPlayerSP {
        @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
        private ItemStack injected(EntityPlayerSP entity, EntityEquipmentSlot slot) {
            return HookHelper.elytraInBaubles(entity, slot);
        }
    }

    @Mixin(EntityLivingBase.class)
    public abstract static class MixinEntityBase {
        @Redirect(method = "updateElytra", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
        private ItemStack injected(EntityLivingBase entity, EntityEquipmentSlot slot) {
            return HookHelper.elytraInBaubles(entity, slot);
        }
    }

    @Mixin(NetHandlerPlayServer.class)
    public abstract static class MixinNetHandler {
        @Redirect(method = "processEntityAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
        private ItemStack injected(EntityPlayerMP entity, EntityEquipmentSlot slot) {
            return HookHelper.elytraInBaubles(entity, slot);
        }
    }

    @Mixin(LayerCape.class)
    public abstract static class MixinCape {
        @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
        private ItemStack injected(AbstractClientPlayer entity, EntityEquipmentSlot slot) {
            return HookHelper.elytraInBaubles(entity, slot);
        }
    }

    @Mixin(LayerElytra.class)
    public abstract static class MixinLayer {
        @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
        private ItemStack injected(EntityLivingBase entity, EntityEquipmentSlot slot) {
            return HookHelper.elytraInBaubles(entity, slot);
        }
    }
}
