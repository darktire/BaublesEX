package baubles.compat.wizardry;

import baubles.api.BaublesApi;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WizardryHelper {
    public static List<ItemStack> artefacts2Baubles(EntityPlayer player, ItemArtefact.Type[] types) {
        List<ItemStack> artefacts = new ArrayList<>();
        int var4 = types.length;
        if (var4 != 0) {
            for (ItemArtefact.Type type1 : types) {
                BaublesApi.applyByIndex(player, (baubles, idx) -> {
                    ItemStack stack = baubles.getStackInSlot(idx);
                    Item item = stack.getItem();
                    if (item instanceof ItemArtefact && ((ItemArtefact) item).getType() == type1) {
                        artefacts.add(stack);
                    }
                });
            }
        }

        return artefacts;
    }
}
