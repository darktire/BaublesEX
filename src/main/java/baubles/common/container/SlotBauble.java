package baubles.common.container;

import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;

public class SlotBauble  extends SlotBaubleHandler{// Bountiful Baubles compat
    public SlotBauble(EntityPlayer player, IBaublesItemHandler itemHandler, int slot, int par4, int par5) {
        super(player, itemHandler, slot, par4, par5);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
