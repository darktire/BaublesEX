package baubles.api;

import baubles.api.cap.IBaublesListener;
import baubles.api.render.IRenderBauble;
import net.minecraft.item.ItemStack;

public interface IWrapper extends IBauble, IRenderBauble, IBaublesListener<IWrapper> {
    ItemStack getItemStack();
}
