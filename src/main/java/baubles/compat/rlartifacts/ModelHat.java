package baubles.compat.rlartifacts;

import artifacts.client.model.ModelDrinkingHat;
import baubles.client.model.ModelInherit;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

class ModelHat extends ModelInherit {
    public ModelHat() {
        super(new Edit(), null);
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (entity instanceof EntityPlayer && isWouterke(entity)) {
            return Resources.HAT_SPECIAL_TEXTURE;
        }
        else return Resources.HAT_TEXTURE;
    }

    private static class Edit extends ModelDrinkingHat {
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity instanceof EntityPlayer) {
                if (isWouterke(entity)) {
                    this.hatShade.showModel = ((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
                } else {
                    this.hatShade.showModel = false;
                }
            }
            this.hat.render(scale);
        }
    }

    private static boolean isWouterke(Entity entity) {
        return entity.getName().equals("wouterke");
    }
}
