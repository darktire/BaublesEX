package baubles.util;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.ItemsData;
import baubles.common.config.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

public class HookHelper {
    private final static ItemStack ELYTRA = new ItemStack(Items.ELYTRA);

    public static ItemStack elytraInBaubles(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (Config.ModItems.elytraBauble && entity instanceof EntityPlayer && ((!(stack.getItem() instanceof ItemElytra) || stack.getItem() instanceof ItemElytra && !ItemElytra.isUsable(stack)))) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
            if (baubles != null) {
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack stack1 = baubles.getStackInSlot(i);
                    if (stack1.getItem() instanceof ItemElytra && ItemElytra.isUsable(stack1)) return stack1;
                }
            }
        }
        return stack;
    }

    public static ItemStack capeCondition(AbstractClientPlayer entity, EntityEquipmentSlot slot) {
        Boolean flag = ((IHelper) entity).getFlag();
        if (flag != null && flag) return ELYTRA;
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (Config.ModItems.elytraBauble && flag == null && !(stack.getItem() instanceof ItemElytra)) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
            if (baubles != null) {
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack stack1 = baubles.getStackInSlot(i);
                    if (stack1.getItem() instanceof ItemElytra) {
                        return stack1;
                    }
                }
            }
            ((IHelper) entity).setFlag(false);
        }
        return stack;
    }

    public static ICapabilityProvider redirectBaubleCap(ItemStack stack, ICapabilityProvider provider) {
        IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        if (bauble != null && !(bauble instanceof BaublesWrapper)) {
            if (!ItemsData.isBauble(stack.getItem())) {
                ItemsData.registerBauble(stack.getItem(), ((BaubleType) bauble.getBaubleType(stack)).getExpansion());
            }
            return new BaublesCapabilityProvider(stack);
        }
        return provider;
    }

    private static final Map<String, Boolean> isModLoaded = new HashMap<>();
    public static boolean isModLoaded(String modName) {
        Boolean flag = isModLoaded.get(modName);
        if (flag == null) {
            flag = Loader.instance().getModList().stream().anyMatch(mod -> mod.getName().equals(modName));
            isModLoaded.put(modName, flag);
        }
        return flag;
    }
}
