package baubles.common.command.sub;

import baubles.common.config.ConfigRecord;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandReload extends CommandBase {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString("start reloading"));
        ConfigRecord.reload();
        sender.sendMessage(new TextComponentString("reload successfully"));
    }
}
