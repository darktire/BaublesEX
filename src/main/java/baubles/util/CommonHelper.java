package baubles.util;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifier;
import baubles.common.network.PacketSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.config.Property;

import java.util.List;

public class CommonHelper {

    public static void tryEquipping(EntityPlayer playerIn, ItemStack stack) {
        tryEquipping(playerIn, EnumHand.MAIN_HAND, stack);
    }

    public static boolean tryEquipping(EntityPlayer playerIn, EnumHand hand, ItemStack stack) {
        IBauble bauble = BaublesApi.toBauble(stack);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) playerIn);
        List<BaubleTypeEx> types = bauble.getTypes(stack);
        for (int i = 0, s = baubles.getSlots(); i < s; i++) {
            boolean match = baubles.getTypeInSlot(i).contains(types);
            if (match && baubles.getStackInSlot(i).isEmpty()) {
                stack = stack.copy();
                baubles.setStackInSlot(i, stack);
                if (!playerIn.capabilities.isCreativeMode) {
                    if (hand == EnumHand.MAIN_HAND) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                    }
                    else {
                        playerIn.inventory.offHandInventory.set(0, ItemStack.EMPTY);
                    }
                }
                bauble.onEquipped(stack, playerIn);
                if (!playerIn.world.isRemote) {
                    PacketSync pkt = PacketSync.S2CPack(playerIn, i, stack, -1);
                    PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) playerIn);
                }
                return true;
            }
        }
        return false;
    }

    public static void configSlot(String typeName, int n, boolean modifying) {
        Property property = Config.Slots.getCategory().get(typeName + "Slot");
        if (property == null) {
            try {//todo
//                List<BaubleTypeEx> types = ConversionHelper.fromJson(ConversionHelper.Category.TYPE_DATA);
//                for (BaubleTypeEx type : types) {
//                    if (type.getName().equals(typeName)) {
//                        if (modifying) {
//                            n += type.getAmount();
//                        }
//                        type.setAmount(n);
//                    }
//                }
//                ConversionHelper.toJson(types, ConversionHelper.Category.TYPE_DATA, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            if (modifying) {
                n += property.getInt();
            }
            property.set(n);
            Config.saveConfig();
            Config.syncToBaubles();
        }
    }

    public static void applyAnonymousModifier(EntityLivingBase entity, BaubleTypeEx type, int modifier) {
        AbstractAttributeMap map = entity.getAttributeMap();
        AdvancedInstance instance = AttributeManager.getInstance(map, type);
        double present = instance.getAnonymousModifier(0);
        instance.applyAnonymousModifier(0, present + modifier);
        PacketHandler.INSTANCE.sendTo(new PacketModifier(entity, type, (int) (present + modifier), 0), (EntityPlayerMP) entity);
    }

    public static void setAnonymousModifier(EntityLivingBase entity, BaubleTypeEx type, int modifier) {
        AbstractAttributeMap map = entity.getAttributeMap();
        AdvancedInstance instance = AttributeManager.getInstance(map, type);
        instance.applyAnonymousModifier(0, modifier);
        PacketHandler.INSTANCE.sendTo(new PacketModifier(entity, type, modifier, 0), (EntityPlayerMP) entity);
    }
}
