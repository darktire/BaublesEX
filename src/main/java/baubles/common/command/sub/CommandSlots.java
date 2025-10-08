package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.common.command.BaublesCommand;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Property;

public class CommandSlots extends BaublesCommand {
    public CommandSlots() {
        super(null);
        this.addSubcommand(new SubModify());
        this.addSubcommand(new SubSet());
        this.addSubcommand(new SubSetModify());
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/baubles slots <option> <player> <type> <number>, option: modify, set, set_modify";
    }

    public static class SubModify extends CommandBase {
        @Override
        public String getName() {
            return "modify";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots modify <player> <type> <modifier>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
            if (TypesData.hasType(args[1]) && args[2].matches("-?\\d+")) {
                int modifier = Integer.parseInt(args[2]);
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                baubles.modifySlotOA(args[1], modifier);
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, 1), player);
                baubles.updateContainer();
            }
            else {
                sendError(sender);
            }
        }
    }

    public static class SubSet extends CommandBase {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots set <player> <type> <number> or /baubles set config <type> <number>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args[0].equals("config") && args[2].matches("-?\\d+")) {
                int modifier = Integer.parseInt(args[2]);
                Property property = Config.Slots.getCategory().get(args[1]);
                if (property == null) {
                    //todo
                }
                else {
                    property.set(modifier);
                    Config.saveConfig();
                    Config.syncToBaubles();
                }
            }
            else {
                EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
                if (TypesData.hasType(args[1]) && args[2].matches("-?\\d+")) {
                    int modifier = Integer.parseInt(args[2]);
                    IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                    baubles.setSlot(args[1], modifier);
                    PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, 0), player);
                    baubles.updateContainer();
                }
                else {
                    sendError(sender);
                }
            }
        }
    }

    public static class SubSetModify extends CommandBase {
        @Override
        public String getName() {
            return "set_modify";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots set_modify <player> <type> <modifier>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
            if (TypesData.hasType(args[1]) && args[2].matches("-?\\d+")) {
                int modifier = Integer.parseInt(args[2]);
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                baubles.modifySlot(args[1], modifier);
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, args[1], modifier, 0), player);
                baubles.updateContainer();
            }
            else {
                sendError(sender);
            }
        }
    }
}
