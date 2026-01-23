package baubles.compat.wizardry;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import com.google.common.collect.ImmutableMap;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WizardryHelper {
    private static final Map<ItemArtefact.Type, BaubleTypeEx> ARTEFACT_TYPE_MAP = ImmutableMap.<ItemArtefact.Type, BaubleTypeEx>builder()
            .put(ItemArtefact.Type.RING, TypeData.Preset.RING)
            .put(ItemArtefact.Type.AMULET, TypeData.Preset.AMULET)
            .put(ItemArtefact.Type.CHARM, TypeData.Preset.CHARM)
            .put(ItemArtefact.Type.BELT, TypeData.Preset.BELT)
            .put(ItemArtefact.Type.BODY, TypeData.Preset.BODY)
            .put(ItemArtefact.Type.HEAD, TypeData.Preset.HEAD)
            .build();

    public static List<ItemStack> artefacts2Baubles(EntityPlayer player, ItemArtefact.Type[] types) {
        List<ItemStack> artefacts = new ArrayList<>();
        int var4 = types.length;
        if (var4 != 0) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);

            for (ItemArtefact.Type type1 : types) {
                BaubleTypeEx type2 = ARTEFACT_TYPE_MAP.get(type1);
                int idx = baubles.indexOf(type2, 0);

                for (; idx < baubles.getSlots() && baubles.getTypeInSlot(idx) == type2; idx++) {
                    ItemStack stack = baubles.getStackInSlot(idx);
                    if (stack.getItem() instanceof ItemArtefact) {
                        artefacts.add(stack);
                    }
                }
            }
        }

        return artefacts;
    }
}
