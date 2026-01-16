package baubles.common.command;

import baubles.api.cap.IBaublesItemHandler;
import baubles.common.command.sub.*;
import baubles.common.config.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;

public class BaublesCommand extends CommandTreeBase {
	private final CommandHelp help = new CommandHelp();

	public BaublesCommand() {
		this.addSubcommand(help);
		this.addSubcommand(new CommandView());
		this.addSubcommand(new CommandClear());
		this.addSubcommand(new CommandSlots());
		this.addSubcommand(new CommandSet());
		this.addSubcommand(new CommandDump());
		if (Config.Commands.debug) this.addSubcommand(new CommandDebug());
	}

	public BaublesCommand(Object unused) {} // todo move

	@Override
	public String getName() {
		return "baubles";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/baubles <action> ...";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			this.help.execute(server, sender, new String[]{});
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
				sendError(sender);
			}
		}
	}

	@Override
	public ICommand getSubCommand(String command) {
		return super.getSubCommand(command.toLowerCase());
	}

	public static String[] shiftArgs(String[] s) {
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

	public static void checkSlot(IBaublesItemHandler baubles, int slot) throws CommandException {
		if (slot >= baubles.getSlots()) {
			throw new CommandException("commands.baubles.index.out", slot);
		}
	}

	public static void sendError(ICommandSender sender) {
		sender.sendMessage(new TextComponentTranslation("commands.baubles.error"));
		sender.sendMessage(new TextComponentTranslation("commands.baubles.help.get"));
	}
}
