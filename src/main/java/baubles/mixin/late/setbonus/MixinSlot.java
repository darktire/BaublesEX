package baubles.mixin.late.setbonus;

import baubles.compat.setbonus.Resource;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.SlotData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(value = SlotData.class, remap = false)
public class MixinSlot {

    @Redirect(
            method = "addSlotsFromString",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/fantasticsource/setbonus/common/bonusrequirements/setrequirement/SlotData;getSlotIDs(Ljava/lang/String;)Ljava/util/ArrayList;")
    )
    private static ArrayList<Integer> redirect1(String title, @Local(argsOnly = true) SlotData slotData) {
        return Resource.correct(title, () -> {
            slotData.slotNames.add(title.trim());
            return new ArrayList<>();
        });
    }

    @Redirect(
            method = "equipped",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/fantasticsource/setbonus/common/bonusrequirements/setrequirement/SlotData;getSlotIDs(Ljava/lang/String;)Ljava/util/ArrayList;"
            )
    )
    private ArrayList<Integer> redirect2(String title, EntityPlayer player) {
        return Resource.correct(title,  () -> Resource.getSlots(title, player));
    }
}
