package baubles.compat.tcon;

import baubles.api.BaublesApi;
import baubles.compat.ModOnly;
import baubles.util.HookHelper;
import com.existingeevee.moretcon.tools.tooltypes.Gauntlet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.lang.reflect.Field;

@ModOnly("moretcon")
public class EventHandler extends Gauntlet {
    private static final Field LAST_DMG_FIELD = HookHelper.getField("com.existingeevee.moretcon.tools.tooltypes.Gauntlet", "lastDMG");
    private static float lastDMG = (float) HookHelper.getValue(LAST_DMG_FIELD, null);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingKnockBackEvent e) {
        if (checkState("onLivingAttack")) return;

        if (e.getAttacker() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getAttacker();

            BaublesApi.applyToBaubles(player, stack -> {
                if (stack.getItem() instanceof Gauntlet && !ToolHelper.isBroken(stack)) {
                    float knockback = e.getOriginalStrength();
                    for (ITrait t : ToolHelper.getTraits(stack)) {
                        knockback = t.knockBack(stack, player, e.getEntityLiving(), lastDMG, e.getOriginalStrength(), knockback, itemRand.nextBoolean());
                    }
                }
            });

        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent e) {
        if (checkState("onLivingHurt")) return;

        DamageSource source = e.getSource();
        if (source.getImmediateSource() instanceof EntityPlayer && source.getDamageType().equals("player")) {
            EntityPlayer player = (EntityPlayer) source.getImmediateSource();
            lastDMG = e.getAmount();

            BaublesApi.applyToBaubles(player, stack -> {
                if (stack.getItem() instanceof Gauntlet && !ToolHelper.isBroken(stack)) {
                    boolean crit = Math.random() < 0.125;
                    for (ITrait t : ToolHelper.getTraits(stack)) {
                        lastDMG = t.damage(stack, player, e.getEntityLiving(), e.getAmount(), lastDMG, crit);
                    }
                    for (ITrait t : ToolHelper.getTraits(stack)) {
                        t.onHit(stack, player, e.getEntityLiving(), lastDMG, crit);
                        t.afterHit(stack, player, e.getEntityLiving(), lastDMG, crit, true);
                    }
                    if (Math.random() < 0.75 && !player.capabilities.isCreativeMode) {
                        ToolHelper.damageTool(stack, 1, player);
                    }
                }
            });

            e.setAmount(lastDMG);
        }
    }

    private static boolean checkState(String methodName) {
        int count = 0;
        String className = EventHandler.class.getName();

        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            if (ste.getClassName().equals(className) && ste.getMethodName().equals(methodName)) {
                count++;
            }
        }

        return count > 1;
    }
}
