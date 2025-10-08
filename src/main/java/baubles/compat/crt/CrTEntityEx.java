package baubles.compat.crt;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
@ZenRegister
public class CrTEntityEx {

    @ZenGetter("baubles")
    public static IContainer getBaublesInventory(IEntityLivingBase crtEntity) {
        EntityLivingBase entity = CraftTweakerMC.getEntityLivingBase(crtEntity);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        return CrTContainer.of(baubles);
    }
}
