package baubles.mixin.late.da;

import com.dungeon_additions.da.items.trinket.ItemTrinket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemTrinket.class, remap = false)
public interface ItemTrinketAccessor {
    @Accessor("baubleSlot")
    ItemTrinket.baubleSlot getType();
}
