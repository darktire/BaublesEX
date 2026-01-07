package baubles.compat.xat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

import java.util.Collections;
import java.util.List;

public class ClawsRender implements IRenderBauble {

    private final ModelBauble model;
    private final RenderType type;

    private static final ClawsRender L = new ClawsRender(new ModelClaws(true), RenderType.ARM_LEFT);
    private static final ClawsRender R = new ClawsRender(new ModelClaws(false), RenderType.ARM_RIGHT);

    private static final List<IRenderBauble> LEFT = Collections.singletonList(L);
    private static final List<IRenderBauble> RIGHT = Collections.singletonList(R);
    private static final List<IRenderBauble> BOTH = ImmutableList.of(L, R);

    public ClawsRender(ModelBauble model, RenderType type) {
        this.model = model;
        this.type = type;
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.model;
    }

    @Override
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.type;
    }

    public static List<IRenderBauble> get(EntityLivingBase entity) {
        if (!TrinketsConfig.SERVER.Items.FAELIS_CLAW.compat.baubles.equip_multiple) {
            return BOTH;
        }
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        int j = baubles.indexOf(TypesData.Preset.RING, 0);
        int k = baubles.indexOf(ModItems.baubles.BaubleFaelisClaw, 0);
        if (k != -1) {
            int l = baubles.indexOf(ModItems.baubles.BaubleFaelisClaw, k);
            if (l != k) return BOTH;
        }
        if (((k - j) & 1) == 0) {
            return RIGHT;
        }
        else {
            return LEFT;
        }
    }
}
