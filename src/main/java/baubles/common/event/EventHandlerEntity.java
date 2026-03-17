package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.BaublesContainerProvider;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import baubles.common.config.Config;
import baubles.common.network.NetworkHandler;
import baubles.common.network.PacketModifier;
import baubles.util.CommonHelper;
import baubles.util.HookHelper;
import cofh.core.enchantment.EnchantmentSoulbound;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import static cofh.core.init.CoreEnchantments.soulbound;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class EventHandlerEntity {
    private static final ResourceLocation BAUBLES_CAP = new ResourceLocation(BaublesApi.MOD_ID, "container");
    private static final boolean CoFHLoaded = Loader.isModLoaded("cofhcore");
    private static final int PICKUP_DELAY = 40;
    private static final Method RIGHT_CLICK = ObfuscationReflectionHelper.findMethod(Item.class, "func_77659_a", ActionResult.class, World.class, EntityPlayer.class, EnumHand.class);

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            BaublesContainer bco = (BaublesContainer) BaublesApi.getBaublesHandler((EntityLivingBase) event.getOriginal());
            BaublesContainer bcn = (BaublesContainer) BaublesApi.getBaublesHandler((EntityLivingBase) event.getEntityPlayer());
            bcn.setRespawnTask(() -> bcn.copyFrom(bco));
        } catch (Exception e) {
            BaublesApi.log.error("Could not clone player [" + event.getOriginal().getName() + "] baubles when changing dimensions");
        }
    }

//    @SubscribeEvent(priority = EventPriority.LOW)
//    public static void onRespawnEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {}

    @SubscribeEvent
    public static void onChangedDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player.world.isRemote) return;
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        AttributeManager.getBaubles(player).forEach((type, instance) -> NetworkHandler.CHANNEL.sendTo(new PacketModifier(player, type, instance.getBaseValue(), instance.getModifiers()), player));
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        baubles.stx.markDirty(0, baubles.getSlots());
        baubles.vis.markDirty(0, baubles.getSlots());
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (shouldAttach(entity)) {
            event.addCapability(BAUBLES_CAP, new BaublesContainerProvider((EntityLivingBase) entity));
        }
    }

    private static boolean shouldAttach(Entity entity) {//todo
        return entity instanceof EntityPlayer;
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {// todo entity event pre/post
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            BaublesApi.applyToBaubles(player, stack -> {
                IBauble bauble = BaublesApi.toBauble(stack);
                if (bauble != null) {
                    bauble.onWornTick(stack, player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            if (entity instanceof EntityPlayerMP) {
                // todo incorrect sequence: sever -> attribute -> client
                BaublesContainer baubles = (BaublesContainer) BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.onRespawn();
                baubles.stx.markDirty(0, baubles.getSlots());
                baubles.vis.markDirty(0, baubles.getSlots());
                syncAnonymousModifier((EntityPlayerMP) entity);
            }
        }
    }

    @SubscribeEvent
    public static void playerDeath(PlayerDropsEvent event) {
        if (event.getEntity().world.isRemote || event.getEntity().world.getGameRules().getBoolean("keepInventory") || Config.keepBaubles) return;

        EntityPlayer player = event.getEntityPlayer();
        List<EntityItem> drops = event.getDrops();

        double posX = player.posX;
        double posY = player.posY - 0.3 + player.getEyeHeight();
        double posZ = player.posZ;
        Random rand = player.world.rand;

        BaublesApi.applyByIndex(player, (baubles, i) -> dropItemsAt(player, drops, baubles, i, posX, posY, posZ, rand));
    }

    private static void dropItemsAt(EntityPlayer player, List<EntityItem> drops, IBaublesItemHandler baubles, int i, double x, double y, double z, Random rand) {
        ItemStack stack = baubles.getStackInSlot(i);
        if (stack.isEmpty() || !BaublesApi.toBauble(stack).canDrop(stack, player)) return;

        if (EnchantmentHelper.hasVanishingCurse(stack)) {
            baubles.setStackInSlot(i, ItemStack.EMPTY);
            return;
        }

        if (CoFHLoaded && EnchantmentHelper.getEnchantmentLevel(soulbound, stack) > 0) {
            handleSoulbound(stack);
            return;
        }

        float speed = rand.nextFloat() * 0.1F;
        float angle = rand.nextFloat() * (float) Math.PI * 2.0F;
        EntityItem ei = new EntityItem(player.world, x, y, z, stack.copy());
        ei.setPickupDelay(PICKUP_DELAY);
        ei.motionX = -MathHelper.sin(angle) * speed;
        ei.motionZ = MathHelper.cos(angle) * speed;
        ei.motionY = 0.2D;
        drops.add(ei);
        baubles.setStackInSlot(i, ItemStack.EMPTY);
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!Config.rightClick) return;
        if (event.getCancellationResult() != EnumActionResult.SUCCESS) {
            ItemStack heldItem = event.getItemStack();
            if (HookHelper.isOverridden(heldItem.getItem(), RIGHT_CLICK)) return;
            if (Config.getBlacklist().contains(heldItem.getItem()) || !BaublesApi.isBauble(heldItem)) return;
            if (CommonHelper.tryEquipping(event.getEntityPlayer(), event.getHand(), heldItem)) {
                event.setCancellationResult(EnumActionResult.SUCCESS);
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

    private static void syncAnonymousModifier(EntityPlayerMP player) {
        AbstractAttributeMap map = player.getAttributeMap();
        TypeData.applyToTypes(type -> {
            for (int i = 0; i < 3; i++) {
                int modifier = (int) AttributeManager.getInstance(map, type).getAnonymousModifier(i);
                if (modifier != 0) {
                    NetworkHandler.CHANNEL.sendTo(new PacketModifier(player, type, modifier, i), player);
                }
            }
        });
    }
}