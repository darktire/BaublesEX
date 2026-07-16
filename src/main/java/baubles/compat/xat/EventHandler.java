package baubles.compat.xat;

import baubles.api.event.BaublesChangeEvent;
import baubles.compat.ModOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.items.base.AccessoryBase;

@ModOnly("xat")
public class EventHandler {
    @SubscribeEvent
    public static void equipmentRenderEvent(BaublesChangeEvent event) {
        ItemStack stack = event.getStackIn();
        NBTTagCompound compound = stack.getTagCompound();
        if (stack.getItem() instanceof AccessoryBase && compound != null) {
            compound.setInteger("slot", event.getIndex());
            compound.setString("handler", TrinketHelper.SlotInformation.ItemHandlerType.BAUBLES.getName());
        }
    }
}
