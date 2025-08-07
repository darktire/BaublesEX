package baubles.common.util.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.util.TypesData;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import baubles.common.util.command.CommandBaubles;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandAdd extends CommandBase {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = CommandBaubles.checkPlayer(server, sender, args);
        if (TypesData.hasType(args[1]) && args[2].matches("-?\\d+")) {
            int modifier = Integer.parseInt(args[2]);
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            baubles.modifySlot(args[1], modifier);
            PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, true), player);
            baubles.updateSlots();
        }
        else {
            sender.sendMessage(new TextComponentTranslation("commands.baubles.error"));
        }
    }
}
