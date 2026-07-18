package baubles.common.handler;

import baubles.Reference;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.PacketFullSync;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifier;
import baubles.common.network.PacketSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class BaublesSync {

    @SubscribeEvent
    public static void syncBaubles(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        BaublesApi.getBaublesHandler((EntityLivingBase) event.player).updateContainer();
        if (!event.player.world.isRemote) {
            syncModified((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity player = event.getEntityPlayer();
        Entity target = event.getTarget();
        if (player instanceof EntityPlayerMP playerMP && target instanceof EntityPlayerMP targetMP) {
            PacketFullSync full = createFullPkt(targetMP);
            PacketHandler.INSTANCE.sendTo(full, playerMP);
        }
    }

    public static void syncAll(EntityPlayerMP player) {
        PacketFullSync full = createFullPkt(player);
        PacketHandler.INSTANCE.sendTo(full, player);
        PacketHandler.INSTANCE.sendToAllTracking(full, player);
    }

    private static PacketFullSync createFullPkt(EntityPlayerMP player) {
        PacketFullSync full = new PacketFullSync();

        AttributeManager.getBaubles(player).forEach((type, instance) -> {
            full.addModifier(createModifierPkt(player, type, instance));
            instance.callback();
        });

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        PacketSync pkt = PacketSync.S2CPack(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            pkt.append(i, baubles.getStackInSlot(i), baubles.getVisible(i) ? 1 : 0);
        }

        full.setBaubles(pkt);
        return full;
    }

    public static void syncModified(EntityPlayerMP player) {
        PacketFullSync full = new PacketFullSync();

        AttributeManager.getModified(player).forEach((type, instance) -> {
            full.addModifier(createModifierPkt(player, type, instance));
            instance.callback();
        });

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        if (!baubles.canSync()) return;
        if (!baubles.getStx().isDirty() && !baubles.getVis().isDirty()) return;

        PacketSync pkt = PacketSync.S2CPack(player);
        if (baubles.getStx().isDirty()) {
            baubles.getStx().forEach(i -> pkt.append(i, baubles.getStackInSlot(i), -1));
            baubles.getStx().clear();
        }
        if (baubles.getVis().isDirty()) {
            baubles.getVis().forEach(i -> pkt.append(i, null, baubles.getVisible(i) ? 1 : 0));
            baubles.getVis().clear();
        }

        full.setBaubles(pkt);

        PacketHandler.INSTANCE.sendTo(full, player);
        PacketHandler.INSTANCE.sendToAllTracking(full, player);
    }

    private static PacketModifier createModifierPkt(EntityPlayerMP player, BaubleTypeEx type, AdvancedInstance instance) {
        PacketModifier message = new PacketModifier(player, type, instance.getBaseValue(), instance.getModifiers());
        for (int i = 0; i < 3; i++) {
            int modifier = (int) instance.getAnonymousModifier(i);
            if (modifier != 0) {
                message.append(null, modifier, i);
            }
        }
        return message;
    }
}
