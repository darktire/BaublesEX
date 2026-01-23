package baubles.compat.setbonus;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.registries.TypeData;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.SlotData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Resource {
    public static final String PREFIX = "bauble";
    private static final int BAUBLES_OFFSET = -2147483647;

    public static ArrayList<Integer> correct(String title, Supplier<ArrayList<Integer>> func) {
        return title.startsWith(Resource.PREFIX) ? func.get() : SlotData.getSlotIDs(title);
    }

    public static ArrayList<Integer> getSlots(String title, EntityPlayer player) {
        BaubleTypeEx type = TypeData.getTypeByName(title.substring(7));
        if (type != null) {
            ArrayList<Integer> list = new ArrayList<>();
            BaublesApi.applyByIndex(player, (instance, i) -> {
                if (instance.getTypeInSlot(i) == type) {
                    list.add(i + Resource.BAUBLES_OFFSET);
                }
            });
            return list;
        }
        return new ArrayList<>();
    }
}
