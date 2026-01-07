package baubles.api.event;


import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BaublesRenderEvent extends BaublesEvent {
    private final RenderPlayer renderPlayer;

    public BaublesRenderEvent(EntityLivingBase entity, RenderPlayer renderPlayer, ItemStack stack) {
        super(entity, stack);
        this.renderPlayer = renderPlayer;
    }

    public RenderPlayer getRenderPlayer() {
        return this.renderPlayer;
    }

    public static BaublesRenderEvent of(EntityLivingBase entity, RenderPlayer renderPlayer, ItemStack stack, Object slotId) {
        if (slotId instanceof Integer) {
            return new InBaubles(entity, renderPlayer, stack, (Integer) slotId);
        }
        else if (slotId instanceof EntityEquipmentSlot) {
            return new InEquipments(entity, renderPlayer, stack, (EntityEquipmentSlot) slotId);
        }
        throw new IllegalArgumentException("slotId illegal");
    }

    public static class InBaubles extends BaublesRenderEvent {
        private final int slot;

        public InBaubles(EntityLivingBase entity, RenderPlayer renderPlayer, ItemStack stack, int slot) {
            super(entity, renderPlayer, stack);
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }
    }

    public static class InEquipments extends BaublesRenderEvent {
        private final EntityEquipmentSlot slotIn;

        public InEquipments(EntityLivingBase entity, RenderPlayer renderPlayer, ItemStack stack, EntityEquipmentSlot slotIn) {
            super(entity, renderPlayer, stack);
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

        public SwitchTexture(EntityLivingBase entity, RenderPlayer renderPlayer, ItemStack stack, ResourceLocation texture, boolean flag) {
            super(entity, renderPlayer, stack);
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
