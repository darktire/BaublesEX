package baubles.common.util.command.sub;

import baubles.api.BaublesApi;
import baubles.api.util.BaubleItemsContent;
import baubles.api.util.BaublesContent;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandDebug extends CommandBase {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Entity entity = sender.getCommandSenderEntity();
        if (args[0].equals("change")) {
            Item item = Item.getByNameOrId(args[1]);
            BaubleItemsContent.itemToBauble(item).setType(BaublesContent.getTypeByName(args[2]));
        }
        else if (args[0].equals("mod")) {
            if (entity instanceof EntityLivingBase && BaublesContent.hasType(args[1]) && args[2].matches("-?\\d+")) {
                int modifier = Integer.parseInt(args[2]);
                BaublesApi.getBaublesHandler((EntityLivingBase) entity).modifySlots(args[1], modifier);
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots((EntityPlayer) entity, args[1], modifier), (EntityPlayerMP) entity);
            }
        }
        else if (args[0].equals("reset") && entity instanceof EntityLivingBase) {
            BaublesApi.getBaublesHandler((EntityLivingBase) entity).clearModifier();
            PacketHandler.INSTANCE.sendTo(new PacketModifySlots((EntityPlayer) entity, "reset", 0), (EntityPlayerMP) entity);
        }
        else {
            sender.sendMessage(new TextComponentTranslation("commands.baubles.error"));
        }
    }
}
