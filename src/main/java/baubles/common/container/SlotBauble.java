package baubles.common.container;

import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotBauble  extends SlotBaubleHandler{
    public SlotBauble(EntityPlayer player, IBaublesItemHandler itemHandler, int slot, int par4, int par5) {
        super(player, itemHandler, slot, par4, par5);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }
}
