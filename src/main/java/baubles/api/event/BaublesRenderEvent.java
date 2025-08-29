package baubles.api.event;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BaublesRenderEvent extends LivingEvent {
    private final boolean slim;
    private final ItemStack stack;

    public BaublesRenderEvent(EntityLivingBase entity, boolean slim, ItemStack stack) {
        super(entity);
        this.slim = slim;
        this.stack = stack;
    }

    public boolean isCancelable()
    {
        return true;
    }

    public void setCanceled() {
        this.setCanceled(true);
    }

    public boolean isSlim() {
        return this.slim;
    }

    public ItemStack getStack() {
        return stack;
    }

    public static class InBaubles extends BaublesRenderEvent {
        private final int slot;

        public InBaubles(EntityLivingBase entity, boolean slim, ItemStack stack, int slot) {
            super(entity, slim, stack);
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }
    }

    public static class InEquipments extends BaublesRenderEvent {
        private final EntityEquipmentSlot slotIn;

        public InEquipments(EntityLivingBase entity, boolean slim, ItemStack stack, EntityEquipmentSlot slotIn) {
            super(entity, slim, stack);
            this.slotIn = slotIn;
        }

        public EntityEquipmentSlot getSlotIn() {
            return this.slotIn;
        }
    }

    public static class SwitchTexture extends BaublesRenderEvent {
        private ResourceLocation texture;
        private boolean changed;

        public SwitchTexture(EntityLivingBase entity, boolean slim, ItemStack stack, ResourceLocation texture) {
            super(entity, slim, stack);
            this.texture = texture;
            this.changed = false;
        }

        public ResourceLocation getTexture() {
            return this.texture;
        }

        public void setTexture(ResourceLocation texture) {
            this.texture = texture;
            this.changed = true;
        }

        public boolean isChanged() {
            return this.changed;
        }
    }
}
