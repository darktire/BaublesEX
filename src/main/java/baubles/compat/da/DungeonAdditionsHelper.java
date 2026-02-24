package baubles.compat.da;

import baubles.api.BaublesApi;
import baubles.mixin.late.da.ItemTrinketAccessor;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DungeonAdditionsHelper {
    public static List<ItemStack> artefacts2Baubles(EntityPlayer player, ItemTrinket.baubleSlot[] types) {
        List<ItemStack> artefacts = new ArrayList<>();
        int var4 = types.length;
        if (var4 != 0) {
            for (ItemTrinket.baubleSlot type1 : types) {
                BaublesApi.applyByIndex(player, (baubles, idx) -> {
                    ItemStack stack = baubles.getStackInSlot(idx);
                    Item item = stack.getItem();
                    if (item instanceof ItemTrinket && ((ItemTrinketAccessor) item).getType() == type1) {
                        artefacts.add(stack);
                    }
                });
            }
        }

        return artefacts;
    }
}
