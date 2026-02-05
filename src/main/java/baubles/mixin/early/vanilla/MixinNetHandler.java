package baubles.mixin.early.vanilla;

import baubles.util.HookHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandler {
    @Redirect(method = "processEntityAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack injected(EntityPlayerMP entity, EntityEquipmentSlot slot) {
        return HookHelper.universalCondition(entity, slot, true);
    }
}
