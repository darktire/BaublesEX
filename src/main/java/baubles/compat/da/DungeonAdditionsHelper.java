package baubles.compat.da;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DungeonAdditionsHelper {
    private static final Map<ItemTrinket.baubleSlot, BaubleTypeEx> ARTEFACT_TYPE_MAP = ImmutableMap.<ItemTrinket.baubleSlot, BaubleTypeEx>builder()
            .put(ItemTrinket.baubleSlot.AMULET, TypeData.Preset.AMULET)
            .put(ItemTrinket.baubleSlot.CHARM, TypeData.Preset.CHARM)
            .put(ItemTrinket.baubleSlot.TRINKET, TypeData.Preset.TRINKET)
            .build();

    public static List<ItemStack> artefacts2Baubles(EntityPlayer player, ItemTrinket.baubleSlot[] types) {
        List<ItemStack> artefacts = new ArrayList<>();
        int var4 = types.length;
        if (var4 != 0) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);

            for (ItemTrinket.baubleSlot type1 : types) {
                BaubleTypeEx type2 = ARTEFACT_TYPE_MAP.get(type1);
                int idx = baubles.indexOf(type2, 0);
                if (idx == -1) continue;

                for (; idx < baubles.getSlots() && baubles.getTypeInSlot(idx) == type2; idx++) {
                    ItemStack stack = baubles.getStackInSlot(idx);
                    if (stack.getItem() instanceof ItemTrinket) {
                        artefacts.add(stack);
                    }
                }
            }
        }

        return artefacts;
    }
}
