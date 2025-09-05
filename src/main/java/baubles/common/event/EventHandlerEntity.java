package baubles.common.event;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.BaublesContainerProvider;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.event.BaublesRenderEvent;
import baubles.common.config.Config;
import cofh.core.enchantment.EnchantmentSoulbound;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static cofh.core.init.CoreEnchantments.soulbound;

@Mod.EventBusSubscriber(modid = Baubles.MOD_ID)
public class EventHandlerEntity {
    private static final ResourceLocation BAUBLES_CAP = new ResourceLocation(Baubles.MOD_ID, "container");
    private static final boolean CoFHLoaded = Loader.isModLoaded("cofhcore");

    @SubscribeEvent
    public static void equipmentRenderEvent(BaublesRenderEvent.InEquipments event) {
        if (!(event.getStack().getItem() instanceof ItemElytra)) {
            event.canceled();
        }
    }

/*    @SubscribeEvent
    public void openEntityGui(PlayerInteractEvent.EntityInteractSpecific event) {
        if (Config.ModItems.testItem) {
            Entity target = event.getTarget();
            if (target instanceof EntityLivingBase && BaublesApi.canEquipBaubles((EntityLivingBase) target)) {
                EntityPlayer player = event.getEntityPlayer();
                if (player.getHeldItem(event.getHand()).isEmpty()) {
                    IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                    boolean flag = false;
                    for (int i = 0; i < baubles.getSlots(); i++) {
                        if (baubles.getStackInSlot(i).getItem() == BaublesRegister.ModItems.Tire) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        ((IHelper) player).setTarget((EntityLivingBase) target);
                        if (!event.getWorld().isRemote) {
                            player.openGui(Baubles.instance, Baubles.GUI_A, event.getWorld(), 0, 0, 0);
                        }
                        event.setCancellationResult(EnumActionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }*/

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            BaublesContainer bco = (BaublesContainer) BaublesApi.getBaublesHandler(event.getOriginal());
            BaublesContainer bcn = (BaublesContainer) BaublesApi.getBaublesHandler(event.getEntityPlayer());
            NBTTagCompound nbt = bco.serializeNBT();
            bcn.deserializeNBT(nbt);
        } catch (Exception e) {
            Baubles.log.error("Could not clone player [" + event.getOriginal().getName() + "] baubles when changing dimensions");
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(BAUBLES_CAP, new BaublesContainerProvider(new BaublesContainer((EntityLivingBase) event.getObject())));
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        // todo onWornTick
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            for (int i = 0; i < baubles.getSlots(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                IBauble bauble = BaublesApi.toBauble(stack);
                if (bauble != null) {
                    bauble.onWornTick(stack, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerDeath(PlayerDropsEvent event) {
        if (event.getEntity() instanceof EntityPlayer
                && !event.getEntity().world.isRemote
                && !event.getEntity().world.getGameRules().getBoolean("keepInventory")
                && !Config.keepBaubles) {
            dropItemsAt(event.getEntityPlayer(), event.getDrops(), event.getEntityPlayer());
        }
    }

    public static void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity e) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        for (int i = 0; i < baubles.getSlots(); ++i) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (!stack.isEmpty() && ((IBauble) stack.getItem()).canDrop(stack, player)) {
                if (EnchantmentHelper.hasVanishingCurse(stack)) {
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
                else if (CoFHLoaded && EnchantmentHelper.getEnchantmentLevel(soulbound, stack) > 0) {
                    handleSoulbound(stack);
                }
                else {
                    EntityItem ei = new EntityItem(e.world,
                                e.posX, e.posY + e.getEyeHeight(), e.posZ,
                                stack.copy());
                    ei.setPickupDelay(40);
                    float f1 = e.world.rand.nextFloat() * 0.5F;
                    float f2 = e.world.rand.nextFloat() * (float) Math.PI * 2.0F;
                    ei.motionX = -MathHelper.sin(f2) * f1;
                    ei.motionZ = MathHelper.cos(f2) * f1;
                    ei.motionY = 0.20000000298023224D;
                    drops.add(ei);
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
    }

    private static void handleSoulbound(ItemStack stack) {
        int level = EnchantmentHelper.getEnchantmentLevel(soulbound, stack);
        if(!EnchantmentSoulbound.permanent) {
            if(cofh.core.util.helpers.MathHelper.RANDOM.nextInt(level + 1) == 0) {
                ItemHelper.removeEnchantment(stack, soulbound);
                if(level > 1) ItemHelper.addEnchantment(stack, soulbound, level - 1);
            }
        }
    }
}