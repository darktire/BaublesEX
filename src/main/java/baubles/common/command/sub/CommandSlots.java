package baubles.common.command.sub;

import baubles.api.BaubleTypeEx;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.registries.TypeData;
import baubles.common.command.BaublesCommand;
import baubles.common.command.CommandTree;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifier;
import baubles.util.CommonHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSlots extends CommandTree {
    public CommandSlots() {
        this.addSubcommand(new SubModify());
        this.addSubcommand(new SubSet());
        this.addSubcommand(new SubClear());
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/baubles slots <option> <player/config> <type> <number>, option: modify, set, clear";
    }

    private static boolean checkArgs(String[] args) {
        return TypeData.hasType(args[1]) && args[2].matches("-?\\d+");
    }

    public static class SubModify extends Tabbable {
        @Override
        public String getName() {
            return "modify";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots modify <player/config> <type> <modifier>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            boolean flag = checkArgs(args);
            if (args[0].equals("config") && flag) {
                CommonHelper.configSlot(args[1], Integer.parseInt(args[2]), true);
            }
            else {
                EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
                if (flag) {
                    int modifier = Integer.parseInt(args[2]);
                    BaubleTypeEx type = TypeData.getTypeByName(args[1]);
                    if (type != null) {
                        AbstractAttributeMap map = player.getAttributeMap();
                        AdvancedInstance instance = AttributeManager.getInstance(map, type);
                        double present = instance.getAnonymousModifier(0);
                        instance.applyAnonymousModifier(0, present + modifier);
                        PacketHandler.INSTANCE.sendTo(new PacketModifier(player, args[1], (int) (present + modifier), 0), player);
                    }
                }
            }
            if (!flag) {
                sendError(sender);
            }
        }
    }

    public static class SubSet extends Tabbable {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots set <player/config> <type> <number>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            boolean flag = checkArgs(args);
            if (args[0].equals("config") && flag) {
                CommonHelper.configSlot(args[1], Integer.parseInt(args[2]), false);
            }
            else {
                EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
                if (checkArgs(args)) {
                    int modifier = Integer.parseInt(args[2]);
                    BaubleTypeEx type = TypeData.getTypeByName(args[1]);
                    if (type != null) {
                        AbstractAttributeMap map = player.getAttributeMap();
                        AdvancedInstance instance = AttributeManager.getInstance(map, type);
                        double present = instance.getAttributeValue() - instance.getAnonymousModifier(0);
                        instance.applyAnonymousModifier(0, modifier - present);
                        PacketHandler.INSTANCE.sendTo(new PacketModifier(player, args[1], (int) (modifier - present), 0), player);
                    }
                }
            }
            if (!flag) {
                sendError(sender);
            }
        }
    }

    public static class SubClear extends Tabbable {
        @Override
        public String getName() {
            return "clear";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/baubles slots clear <player> or /baubles slots clear <player> <type>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
            if (args.length == 1) {
                TypeData.sortedList().forEach(t -> CommonHelper.setAnonymousModifier(player, t, 0));
            }
            else {
                BaubleTypeEx type = TypeData.getTypeByName(args[1]);
                if (type != null) {
                    CommonHelper.setAnonymousModifier(player, type, 0);
                }
            }
        }
    }
}
