package baubles.api.event;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BaublesRenderEvent extends BaublesEvent {
    private final boolean slim;

    public BaublesRenderEvent(EntityLivingBase entity, boolean slim, ItemStack stack) {
        super(entity, stack);
        this.slim = slim;
    }

    public boolean isSlim() {
        return this.slim;
    }

    public static BaublesRenderEvent of(EntityLivingBase entity, boolean slim, ItemStack stack, Object slotId) {
        if (slotId instanceof Integer) {
            return new InBaubles(entity, slim, stack, (Integer) slotId);
        }
        else if (slotId instanceof EntityEquipmentSlot) {
            return new InEquipments(entity, slim, stack, (EntityEquipmentSlot) slotId);
        }
        throw new IllegalArgumentException("slotId illegal");
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
        private final boolean isEmissiveMap;

        public SwitchTexture(EntityLivingBase entity, boolean slim, ItemStack stack, ResourceLocation texture, boolean flag) {
            super(entity, slim, stack);
            this.texture = texture;
            this.isEmissiveMap = flag;
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

        public boolean isEmissiveMap() {
            return this.isEmissiveMap;
        }
    }
}
