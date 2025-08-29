package baubles.client.model;

import baubles.api.model.ModelBauble;
import baubles.client.ClientProxy;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModelElytra extends ModelBauble {
    private static final Map<Map.Entry<Item, Boolean>, ModelElytra> instances = new HashMap<>();
    private final RenderPlayer renderPlayer;
    private static final boolean flag = Loader.isModLoaded("mobends");

    public ModelElytra(Item item, boolean slim) {
        super(item, slim, new net.minecraft.client.model.ModelElytra());
        this.renderPlayer = slim ? ClientProxy.SLIM_LAYER.getRenderPlayer() : ClientProxy.NORMAL_LAYER.getRenderPlayer();
    }

    public static ModelElytra instance(Item item, boolean slim) {
        Map.Entry<Item, Boolean> pair = new AbstractMap.SimpleEntry<>(item, slim);
        ModelElytra model = instances.get(pair);
        if (model == null) {
            model = new ModelElytra(item, slim);
            instances.put(pair, model);
        }
        return model;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (flag && entity instanceof AbstractClientPlayer) {
            PlayerData data = BenderHelper.getData((AbstractClientPlayer) entity, this.renderPlayer);
            assert data != null;
            data.body.applyCharacterTransform(0.0625F);
            GlStateManager.translate(0.0F, -12.0F * scale, 0.0F);
        }
        else {
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
        }
        this.model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean needLocating() {
        return false;
    }
}
