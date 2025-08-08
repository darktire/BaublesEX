package baubles.common.util.command.sub;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandHelp extends CommandBase {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(new TextComponentTranslation("/baubles view <player>"));
            sender.sendMessage(new TextComponentTranslation("/baubles hand"));
            sender.sendMessage(new TextComponentTranslation("/baubles clear <player> [<slot>]"));
            sender.sendMessage(new TextComponentTranslation("/baubles clear <player> modifier"));
            sender.sendMessage(new TextComponentTranslation("/baubles add <player> <type> <number>"));
        }
    }
}
