package baubles.mixin.late.improvedbackpacks;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.poopycoders.improvedbackpacks.init.ModItems;
import ru.poopycoders.improvedbackpacks.inventory.InventoryBackpack;
import ru.poopycoders.improvedbackpacks.inventory.containers.ContainerBackpack;
import ru.poopycoders.improvedbackpacks.utils.NBTUtils;

@Mixin(ContainerBackpack.class)
public class MixinContainerBackpack {
    @Shadow @Final private InventoryBackpack backpackInventory;

    @Redirect(
            method = "getRealBackpack",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack get(IBaublesItemHandler instance, int i) {
        ItemStack stack = HookHelper.getStack(instance, ModItems.ENDER_BACKPACK);
        if (stack.isEmpty()) HookHelper.getStack(instance, ModItems.BACKPACK);
        return stack;
    }

    @Redirect(
            method = "saveBackpack",
            at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack set(IBaublesItemHandler instance, int i) {
        Item[] items = {ModItems.ENDER_BACKPACK, ModItems.BACKPACK};
        for (Item item : items) {
            int slot = instance.indexOf(item, 0);
            ItemStack stack = instance.getStackInSlot(slot);
            if (stack == this.backpackInventory.backpack) {
                ItemStack copy = this.backpackInventory.backpack.copy();
                ItemStackHelper.saveAllItems(NBTUtils.getStackTag(copy), this.backpackInventory.items);
                NBTUtils.getStackTag(copy).setUniqueId("UUID", this.backpackInventory.getUUID());
                instance.setStackInSlot(slot, copy);
                this.backpackInventory.backpack = copy;
                break;
            }
        }
        return ItemStack.EMPTY;
    }
}
