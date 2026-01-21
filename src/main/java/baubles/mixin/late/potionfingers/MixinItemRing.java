package baubles.mixin.late.potionfingers;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.potionfingers.ItemRing;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemRing.class,remap = false)
public class MixinItemRing {
    @Redirect(method = "updatePotionStatus", at = @At(value = "INVOKE", target = "Lbaubles/api/BaubleType;getValidSlots()[I"))
    private int[] redirect(BaubleType instance, @Local(argsOnly = true) EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        List<Integer> list = new ArrayList<>();

        int curr = 0;
        int idx;
        while ((idx = baubles.indexOf(this, curr)) != -1) {
            list.add(idx);
            curr = idx + 1;
        }

        return list.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }
}
