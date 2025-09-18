package baubles.compat.crt;

import baubles.api.event.BaublesEvent;
import baubles.compat.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.api.event.IEventManager;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenRegister
@ZenExpansion("crafttweaker.events.IEventManager")
public class EventHandler {
    private static final EventList<CrTBSEvent.PreEquip> EQUIP_PRE = new EventList<>();
    private static final EventList<CrTBSEvent.PostEquip> EQUIP_POST = new EventList<>();
    private static final EventList<CrTBSEvent.PreUnequip> UNEQUIP_PRE = new EventList<>();
    private static final EventList<CrTBSEvent.PostUnequip> UNEQUIP_POST = new EventList<>();
    private static final EventList<CrTBSEvent.WearingTick> WEARING_TICK = new EventList<>();



    @ZenMethod
    public static IEventHandle onEquipPre(IEventManager manager, IEventHandler<CrTBSEvent.PreEquip> h){
        return EQUIP_PRE.add(h);
    }
    @ZenMethod
    public static IEventHandle onEquipPost(IEventManager manager, IEventHandler<CrTBSEvent.PostEquip> h){
        return EQUIP_POST.add(h);
    }
    @ZenMethod
    public static IEventHandle onUnequipPre(IEventManager manager, IEventHandler<CrTBSEvent.PreUnequip> h){
        return UNEQUIP_PRE.add(h);
    }
    @ZenMethod
    public static IEventHandle onUnequipPost(IEventManager manager, IEventHandler<CrTBSEvent.PostUnequip> h){
        return UNEQUIP_POST.add(h);
    }
    @ZenMethod
    public static IEventHandle onWearingTick(IEventManager manager, IEventHandler<CrTBSEvent.WearingTick> h){
        return WEARING_TICK.add(h);
    }



    @SubscribeEvent
    public static void handleEquipPre(BaublesEvent.Equip.Pre e){
        if (EQUIP_PRE.hasHandlers())   EQUIP_PRE.publish(new CrTBSEvent.PreEquip(e));
    }
    @SubscribeEvent
    public static void handleEquipPost(BaublesEvent.Equip.Post e){
        if (EQUIP_POST.hasHandlers())  EQUIP_POST.publish(new CrTBSEvent.PostEquip(e));
    }
    @SubscribeEvent
    public static void handleUnequipPre(BaublesEvent.Unequip.Pre e){
        if (UNEQUIP_PRE.hasHandlers()) UNEQUIP_PRE.publish(new CrTBSEvent.PreUnequip(e));
    }
    @SubscribeEvent
    public static void handleUnequipPost(BaublesEvent.Unequip.Post e){
        if (UNEQUIP_POST.hasHandlers())UNEQUIP_POST.publish(new CrTBSEvent.PostUnequip(e));
    }
    @SubscribeEvent
    public static void handleWearingTick(BaublesEvent.WearingTick e){
        if (WEARING_TICK.hasHandlers())WEARING_TICK.publish(new CrTBSEvent.WearingTick(e));
    }
}
