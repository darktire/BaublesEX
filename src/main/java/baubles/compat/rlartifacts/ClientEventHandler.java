package baubles.compat.rlartifacts;

import artifacts.common.util.RenderHelper;
import baubles.api.event.BaublesRenderEvent;
import baubles.compat.ModOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModOnly(value = "RLArtifacts", client = true)
public class ClientEventHandler {
    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent.InBaubles event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack)) {
                event.cancel();
            }
        }
    }

    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent.InEquipments event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack)) {
                event.cancel();
            }
        }
    }
}
