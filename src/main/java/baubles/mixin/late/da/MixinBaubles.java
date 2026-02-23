package baubles.mixin.late.da;

import baubles.compat.da.DungeonAdditionsHelper;
import com.dungeon_additions.da.integration.BaublesIntegration;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = BaublesIntegration.class, remap = false)
public class MixinBaubles {
    @Inject(method = "getEquippedArtifacts", at = @At("HEAD"), cancellable = true)
    private static void inject0(EntityPlayer player, ItemTrinket.baubleSlot[] types, CallbackInfoReturnable<List<ItemTrinket>> cir) {
        cir.setReturnValue(DungeonAdditionsHelper.artefacts2Baubles(player, types).stream().map(stack -> ((ItemTrinket) stack.getItem())).collect(Collectors.toList()));
    }

    @Inject(method = "getArtifactItemstack", at = @At("HEAD"), cancellable = true)
    private static void inject1(EntityPlayer player, ItemTrinket.baubleSlot[] types, CallbackInfoReturnable<ItemStack> cir) {
        List<ItemStack> stacks = DungeonAdditionsHelper.artefacts2Baubles(player, types);
        cir.setReturnValue(stacks.isEmpty() ? null : stacks.get(0));
    }
}
