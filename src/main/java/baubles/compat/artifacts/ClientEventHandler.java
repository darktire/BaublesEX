package baubles.compat.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.util.RenderHelper;
import baubles.api.BaublesApi;
import baubles.api.event.BaublesChangeEvent;
import baubles.api.event.BaublesRenderEvent;
import baubles.compat.ModOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModOnly(value = "RLArtifacts", client = true)
public class ClientEventHandler {
    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent.InBaubles event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack)) {
                event.canceled();
            }
        }
    }

    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent.InEquipments event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack) || !RenderHelper.shouldRenderInSlot(player, event.getSlotIn())) {
                event.canceled();
            }
        }
    }

    @SubscribeEvent
    public static void updateHoodState(BaublesChangeEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            Item itemIn = event.getStackIn().getItem();
            Item itemOut = event.getStackOut().getItem();
            if (itemIn == ModItems.STAR_CLOAK && itemOut != ModItems.STAR_CLOAK) {
                Resources.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD) && (BaublesApi.getIndexInBaubles(entity, ModItems.DRINKING_HAT, 0) == -1));
            }
            else if (itemIn == ModItems.DRINKING_HAT && itemOut != ModItems.DRINKING_HAT) {
                if (Resources.getHoodState()) {
                    Resources.setHoodState(false);
                }
            }
            else if (itemIn != ModItems.DRINKING_HAT && itemOut == ModItems.DRINKING_HAT) {
                if (!Resources.getHoodState()) {
                    Resources.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD));
                }
            }
        }
    }

    @SubscribeEvent
    public static void updateHoodState(LivingEquipmentChangeEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            if (event.getSlot() == EntityEquipmentSlot.HEAD) {
                ItemStack stackIn = event.getTo();
                ItemStack stackOut = event.getFrom();
                if (!stackIn.isEmpty()) {
                    if (Resources.getHoodState()) {
                        Resources.setHoodState(false);
                    }
                }
                else if (!stackOut.isEmpty()) {
                    if (!Resources.getHoodState()) {
                        Resources.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD) && (BaublesApi.getIndexInBaubles(entity, ModItems.DRINKING_HAT, 0) == -1));
                    }
                }
            }
        }
    }
}
