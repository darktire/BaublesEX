package baubles.compat.enigmaticlegacy;

import baubles.api.event.BaublesEvent;
import baubles.compat.ModOnly;
import keletu.enigmaticlegacy.item.ItemScrollBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModOnly(value = "enigmaticlegacy", client = true)
public class ClientEventHandler {
    @SubscribeEvent
    public static void updateAngle(BaublesEvent.Equip.Post event) {
        EntityLivingBase entity = event.getEntityLiving();
        Item item = event.getStack().getItem();
        if (item instanceof ItemScrollBauble) {
            ModelScroll.addItem(entity, item);
        }
    }

    @SubscribeEvent
    public static void updateAngle(BaublesEvent.Unequip.Post event) {
        EntityLivingBase entity = event.getEntityLiving();
        Item item = event.getStack().getItem();
        if (item instanceof ItemScrollBauble) {
            ModelScroll.removeItem(entity, item);
        }
    }
}
