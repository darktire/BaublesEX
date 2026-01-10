package baubles.mixin.late.setbonus;

import baubles.compat.setbonus.Resource;
import com.fantasticsource.setbonus.common.bonuselements.BonusElementEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(value = BonusElementEnchantment.class, remap = false)
public abstract class MixinBonus {

    @Redirect(
            method = "updateActive(Lnet/minecraft/entity/player/EntityPlayer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/fantasticsource/setbonus/common/bonusrequirements/setrequirement/SlotData;getSlotIDs(Ljava/lang/String;)Ljava/util/ArrayList;")
    )
    private ArrayList<Integer> redirect(String title, EntityPlayer player) {
        return Resource.correct(title,  () -> Resource.getSlots(title, player));
    }
}
