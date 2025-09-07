package baubles.compat.enigmaticlegacy;

import baubles.api.event.BaublesChangeEvent;
import keletu.enigmaticlegacy.item.ItemScrollBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public static void updateAngle(BaublesChangeEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            Item itemIn = event.getStackIn().getItem();
            Item itemOut = event.getStackOut().getItem();
            if (itemIn instanceof ItemScrollBauble) {
                ModelScroll.addItem(itemIn);
            }
            if (itemOut instanceof ItemScrollBauble) {
                ModelScroll.removeItem(itemOut);
            }
        }
    }
}
