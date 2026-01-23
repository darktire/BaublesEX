package baubles.compat.magnet;

import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.compat.ModOnly;
import com.supermartijn642.simplemagnets.MagnetItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collections;
import java.util.List;

@ModOnly("simplemagnets")
public class BaubleMagnet implements IBauble {
    private static final BaubleMagnet MAGNET = new BaubleMagnet();
    private static final List<BaubleTypeEx> CHARM = Collections.singletonList(TypeData.Preset.CHARM);

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
        return CHARM;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player){
        itemstack.getItem().onUpdate(itemstack, player.world, player, -1, false);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(MagnetItem.class::isInstance)
                .forEach(i -> ItemData.registerBauble(i, MAGNET));
    }
}
