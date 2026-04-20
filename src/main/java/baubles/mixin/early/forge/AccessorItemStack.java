package baubles.mixin.early.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemStack.class, remap = false)
public interface AccessorItemStack {
    @Accessor("capNBT") NBTTagCompound capNBT();
}
