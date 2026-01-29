package baubles.common.command;

import baubles.common.command.sub.*;
import baubles.common.config.Config;
import net.minecraft.command.ICommandSender;

public class BaublesCommand extends CommandTree {

	public BaublesCommand() {
		this.addSubcommand(help);
		this.addSubcommand(new CommandView());
		this.addSubcommand(new CommandClear());
		this.addSubcommand(new CommandSlots());
		this.addSubcommand(new CommandSet());
		this.addSubcommand(new CommandDump());
		this.addSubcommand(new CommandReload());
		if (Config.Commands.debug) this.addSubcommand(new CommandDebug());
	}

	@Override
	public String getName() {
		return "baubles";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/baubles <action> ...";
	}
}
