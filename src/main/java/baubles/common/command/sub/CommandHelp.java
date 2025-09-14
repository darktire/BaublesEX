package baubles.common.command.sub;

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
            sender.sendMessage(new TextComponentTranslation("/baubles clear <player> <slot>"));
            sender.sendMessage(new TextComponentTranslation("/baubles slots <option> <player> <type> <number>"));
            sender.sendMessage(new TextComponentTranslation("/baubles set <player> <slot> <item> <meta> {nbt}"));
            sender.sendMessage(new TextComponentTranslation("/baubles dump"));
        }
    }
}
