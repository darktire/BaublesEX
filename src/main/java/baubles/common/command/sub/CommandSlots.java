package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.common.command.BaublesCommand;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import baubles.util.HookHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSlots extends BaublesCommand {
    public CommandSlots() {
        super(null);
        this.addSubcommand(new SubModify());
        this.addSubcommand(new SubSet());
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/baubles slots <option> <player/config> <type> <number>, option: modify, set";
    }

    private static boolean checkArgs(String[] args) {
        return TypesData.hasType(args[1]) && args[2].matches("-?\\d+");
    }

    public static class SubModify extends CmdBase {
        @Override
        public String getName() {
            return "modify";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots modify <player> <type> <modifier> or /baubles modify config <type> <number>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            boolean flag = checkArgs(args);
            if (args[0].equals("config") && flag) {
                HookHelper.configSlot(args[1], Integer.parseInt(args[2]), true);
            }
            else {
                EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
                if (flag) {
                    int modifier = Integer.parseInt(args[2]);
                    IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                    baubles.modifySlot(args[1], modifier);
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, 1), player);
                    baubles.updateContainer();
                }
            }
            if (!flag) {
                sendError(sender);
            }
        }
    }

    public static class SubSet extends CmdBase {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots set <player> <type> <number> or /baubles set config <type> <number>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {            boolean flag = checkArgs(args);
            if (args[0].equals("config") && flag) {
                HookHelper.configSlot(args[1], Integer.parseInt(args[2]), false);
            }
            else {
                EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
                if (checkArgs(args)) {
                    int modifier = Integer.parseInt(args[2]);
                    IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                    baubles.setSlot(args[1], modifier);
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, 2), player);
                    baubles.updateContainer();
                }
            }
            if (!flag) {
                sendError(sender);
            }
        }
    }
}
