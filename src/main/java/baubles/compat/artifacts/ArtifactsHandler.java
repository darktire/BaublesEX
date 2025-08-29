package baubles.compat.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.util.RenderHelper;
import baubles.api.BaublesApi;
import baubles.api.event.BaublesChangeEvent;
import baubles.api.event.BaublesRenderEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArtifactsHandler {
    @SubscribeEvent
    public void applyArtifactsRender(BaublesRenderEvent.InBaubles event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack)) {
                event.setCanceled();
            }
        }
    }

    @SubscribeEvent
    public void applyArtifactsRender(BaublesRenderEvent.InEquipments event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack stack = event.getStack();
            if (!RenderHelper.shouldItemStackRender(player, stack) || !RenderHelper.shouldRenderInSlot(player, event.getSlotIn())) {
                event.setCanceled();
            }
        }
    }

    @SubscribeEvent
    public void updateHoodState(BaublesChangeEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            Item itemIn = event.getStackIn().getItem();
            Item itemOut = event.getStackOut().getItem();
            if (itemIn == ModItems.STAR_CLOAK && itemOut != ModItems.STAR_CLOAK) {
                Resource.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD) && (BaublesApi.isBaubleEquipped(entity, ModItems.DRINKING_HAT) == -1));
            }
            else if (itemIn == ModItems.DRINKING_HAT && itemOut != ModItems.DRINKING_HAT) {
                if (Resource.getHoodState()) {
                    Resource.setHoodState(false);
                }
            }
            else if (itemIn != ModItems.DRINKING_HAT && itemOut == ModItems.DRINKING_HAT) {
                if (!Resource.getHoodState()) {
                    Resource.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD));
                }
            }
        }
    }

    @SubscribeEvent
    public void updateHoodState(LivingEquipmentChangeEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            if (event.getSlot() == EntityEquipmentSlot.HEAD) {
                ItemStack stackIn = event.getTo();
                ItemStack stackOut = event.getFrom();
                if (!stackIn.isEmpty()) {
                    if (Resource.getHoodState()) {
                        Resource.setHoodState(false);
                    }
                }
                else if (!stackOut.isEmpty()) {
                    if (!Resource.getHoodState()) {
                        Resource.setHoodState(RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD) && (BaublesApi.isBaubleEquipped(entity, ModItems.DRINKING_HAT) == -1));
                    }
                }
            }
        }
    }
}
