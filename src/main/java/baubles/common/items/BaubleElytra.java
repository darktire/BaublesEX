package baubles.common.items;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelElytra;
import baubles.common.config.Config;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaubleElytra implements IBauble, IRenderBauble {
    private static final Map<EntityLivingBase, Boolean> EQUIPPED = new ConcurrentHashMap<>();
    private static final Map<Map.Entry<EntityLivingBase, Boolean>, ItemStack> WEARING = new ConcurrentHashMap<>();
    private static final List<BaubleTypeEx> TYPE = Collections.singletonList(TypeData.getTypeByName(Config.ModItems.elytraSlot));

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack stack) {
        return TYPE;
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        EQUIPPED.put(entity, true);
        initWearing(entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        EQUIPPED.put(entity, false);
        initWearing(entity);
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelElytra.INSTANCE;
    }

    private static boolean initWearing(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        if (baubles == null) return false;

        boolean flag1 = false;
        boolean flag2 = false;
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack1 = baubles.getStackInSlot(i);
            if (stack1.getItem() instanceof ItemElytra) {
                boolean usable = ItemElytra.isUsable(stack1);
                if (!flag1 && usable) {
                    WEARING.put(new AbstractMap.SimpleEntry<>(entity, true), stack1);
                    flag1 = true;
                }
                else if (!flag2 && !usable) {
                    WEARING.put(new AbstractMap.SimpleEntry<>(entity, false), stack1);
                    flag2 = true;
                }
            }
            if (flag1 && flag2) return true;
        }
        return flag1 || flag2;
    }

    public static boolean isWearing(EntityLivingBase entity, boolean using) {
        Boolean e = EQUIPPED.get(entity);
        if (e == null) {
            e = initWearing(entity);
        }
        return e;
    }

    public static ItemStack getWearing(EntityLivingBase entity, boolean using) {
        return WEARING.getOrDefault(new AbstractMap.SimpleEntry<>(entity, using), ItemStack.EMPTY);
    }
}
