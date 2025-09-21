package baubles.compat.crt;

import baubles.api.BaublesApi;
import baubles.api.event.BaublesEvent;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.IEventCancelable;
import crafttweaker.api.event.IEventHasResult;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fml.common.eventhandler.Event;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

public class CrTBSEvent<T extends BaublesEvent> implements IEventCancelable {
    private static final String BASE_CLASS = "mods." + BaublesApi.MOD_ID + ".BaublesEvent";
    protected final T event;

    private CrTBSEvent(T event) {
        this.event = event;
    }

    @ZenGetter("entity")
    public IEntityLivingBase getPlayer() {
        return CraftTweakerMC.getIEntityLivingBase(this.event.getEntityLiving());
    }

    @ZenGetter("stack")
    public IItemStack getStack() {
        return CraftTweakerMC.getIItemStack(this.event.getStack());
    }

    @Override
    public boolean isCanceled() {
        return true;
    }

    @Override
    public void setCanceled(boolean unused) {
        this.event.canceled();
    }

    private static class Pre<U extends BaublesEvent & BaublesEvent.ResultAccessor> extends CrTBSEvent<U> implements IEventHasResult {

        private Pre(U event) {
            super(event);
        }

        @ZenGetter("ret")
        public boolean getRet() {
            return this.event.getRet();
        }

        @Override
        public String getResult() {
            return this.event.getResult().toString();
        }

        @Override
        public void setDenied() {
            this.event.setResult(Event.Result.DENY);
        }

        @Override
        public void setDefault() {
            this.event.setResult(Event.Result.DEFAULT);
        }

        @Override
        public void setAllowed() {
            this.event.setResult(Event.Result.ALLOW);
        }
    }

    @ZenRegister
    @ZenClass(BASE_CLASS + ".WearingTick")
    public static class WearingTick extends CrTBSEvent<BaublesEvent.WearingTick> {
        WearingTick(BaublesEvent.WearingTick event) {
            super(event);
        }
    }

    @ZenRegister
    @ZenClass(BASE_CLASS + ".Equip.Pre")
    public static class PreEquip extends Pre<BaublesEvent.Equip.Pre> {

        public PreEquip(BaublesEvent.Equip.Pre event) {
            super(event);
        }
    }

    @ZenRegister
    @ZenClass(BASE_CLASS + ".Equip.Post")
    public static class PostEquip extends CrTBSEvent<BaublesEvent.Equip.Post> {

        public PostEquip(BaublesEvent.Equip.Post event) {
            super(event);
        }
    }

    @ZenRegister
    @ZenClass(BASE_CLASS + ".Unequip.Pre")
    public static class PreUnequip extends Pre<BaublesEvent.Unequip.Pre>{

        public PreUnequip(BaublesEvent.Unequip.Pre event) {
            super(event);
        }
    }

    @ZenRegister
    @ZenClass(BASE_CLASS + ".Unequip.Post")
    public static class PostUnequip extends CrTBSEvent<BaublesEvent.Unequip.Post> {

        public PostUnequip(BaublesEvent.Unequip.Post event) {
            super(event);
        }
    }
}
