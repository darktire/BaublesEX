package baubles.compat.tcon;

import baubles.api.BaublesApi;
import baubles.compat.ModOnly;
import com.existingeevee.moretcon.tools.tooltypes.Gauntlet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

@ModOnly("moretcon")
public class EventHandler extends Gauntlet {
    private static float lastDMG = -1.0F;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingAttackEvent(LivingKnockBackEvent e) {
        int count = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            if (ste.getClassName().equals(this.getClass().getName())) {
                count++;
            }
        }

        if (count > 1) {
            return;
        }

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
    public void onLivingAttackEvent(LivingHurtEvent e) {
        int count = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            if (ste.getClassName().equals(this.getClass().getName())) {
                count++;
            }
        }

        if (count > 1) {
            return;
        }

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
}
