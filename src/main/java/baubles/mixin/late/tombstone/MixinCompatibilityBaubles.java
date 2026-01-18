package baubles.mixin.late.tombstone;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.IBaublesSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ovh.corail.tombstone.compatibility.CompatibilityBaubles;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mixin(value = CompatibilityBaubles.class, remap = false)
public class MixinCompatibilityBaubles {
    @Inject(method = "autoEquip", at = @At("HEAD"), cancellable = true)
    void inject(EntityPlayer player, ItemStackHandler itemHandler, List<Integer> ids, boolean isRespawn, CallbackInfo ci) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        ListIterator<Integer> idIterator = ids.listIterator(ids.size());

        List<Integer> e = IntStream.range(0, baubles.getSlots())
                .filter(idx -> baubles.getStackInSlot(idx).isEmpty())
                .boxed()
                .collect(Collectors.toList());

        while(idIterator.hasPrevious()) {
            int temp = idIterator.previous();
            ItemStack stack = itemHandler.getStackInSlot(temp);
            if (stack.getMaxStackSize() == 1) {
                if (!BaublesApi.isBauble(stack)) continue;
                AbstractWrapper bauble = BaublesApi.toBauble(stack);
                for (BaubleTypeEx type : bauble.getTypes(stack)) {
                    int idx = baubles.indexOf(type, 0);
                    while (idx != -1 && !e.contains(idx)) idx = baubles.indexOf(type, idx + 1);
                    if (idx != -1) {
                        ItemStack stack1 = stack.copy();
                        baubles.setStackInSlot(idx, stack1);
                        bauble.onEquipped(stack1, player);
                        itemHandler.setStackInSlot(temp, ItemStack.EMPTY);
                        e.remove((Integer) idx);
                        break;
                    }
                }
            }
        }

        if (!player.world.isRemote) IBaublesSync.forceSync(player);

        ci.cancel();
    }
}
