package baubles.common.util.command;

import baubles.common.util.command.sub.*;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandBaubles extends CommandTreeBase {
	public CommandBaubles() {
		this.addSubcommand(new CommandHelp());
		this.addSubcommand(new CommandView());
		this.addSubcommand(new CommandClear());
		this.addSubcommand(new CommandHand());
		this.addSubcommand(new CommandAdd());
		this.addSubcommand(new CommandDebug());
	}

	@Override
	public String getName() {
		return "baubles";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/baubles <action> [<player> [<params>]]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return i == 1;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			ICommand help = this.getSubCommand("help");
			if (help != null) help.execute(server, sender, new String[]{"help"});
		}
		else {
			ICommand cmd = this.getSubCommand(args[0]);

			if (cmd != null) {
				if (!cmd.checkPermission(server, sender)) {
					throw new CommandException("commands.generic.permission");
				}

				cmd.execute(server, sender, shiftArgs(args));
			}
			else {
				sender.sendMessage(new TextComponentTranslation("commands.baubles.error"));
				sender.sendMessage(new TextComponentTranslation("commands.baubles.help.get"));
			}
		}
	}

	@Override
	public ICommand getSubCommand(String command) {
		return super.getSubCommand(command.toLowerCase());
	}

	private static String[] shiftArgs(String[] s) {
		if(s == null || s.length == 0) {
			return new String[0];
		}

		String[] s1 = new String[s.length - 1];
		System.arraycopy(s, 1, s1, 0, s1.length);
		return s1;
	}

	public static EntityPlayerMP checkPlayer(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) throw new CommandException("commands.baubles.player.none");

        return getPlayer(server, sender, args[0]);
	}
}
