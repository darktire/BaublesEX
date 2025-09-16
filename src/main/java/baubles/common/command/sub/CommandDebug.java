package baubles.common.command.sub;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

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
//        Entity entity = sender.getCommandSenderEntity();
//        if (args[0].equals("change")) {
//            Item item = Item.getByNameOrId(args[1]);
//            ItemsData.toBauble(item).setType(TypesData.getTypeByName(args[2]));
//        }
//        else {
//            BaublesCommand.sendError(sender);
//        }
    }
}
