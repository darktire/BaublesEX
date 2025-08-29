package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.IBauble;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelElytra;
import baubles.common.config.Config;
import baubles.util.IHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BaubleElytra implements IBauble, IRenderBauble {
    private static final BaubleTypeEx TYPE = TypesData.getTypeByName(Config.ModItems.elytraSlot);
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("minecraft", "textures/entity/elytra.png");

    @Override
    public BaubleTypeEx getBaubleType() {
    return TYPE;
}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            ((IHelper) entity).setFlag(true);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            ((IHelper) entity).setFlag(false);
        }
    }

    @Override
    public ModelBauble getModel(boolean slim) {
        return ModelElytra.instance(Items.ELYTRA, slim);
    }

    @Override
    public ResourceLocation getTexture(boolean slim, EntityLivingBase entity) {
        if (entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;

            if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                return abstractclientplayer.getLocationElytra();
            }
            else if (abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                return abstractclientplayer.getLocationElytra();
            }
            else {
                return TEXTURE_ELYTRA;
            }
        }
        else {
            return TEXTURE_ELYTRA;
        }
    }

    @Override
    public IRenderBauble.RenderType getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
