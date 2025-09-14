package baubles.common.command.sub;

import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.config.json.JsonHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CommandDump extends CommandBase {

    @Override
    public String getName() {
        return "dump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            JsonHelper.typeToJson(this.getList(TypesData.iterator()), true);
            JsonHelper.itemToJson(this.getList(ItemsData.iterator()), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Config.Commands.commandLogs) {
            sender.sendMessage(new TextComponentTranslation("commands.baubles.dump", Config.MOD_DIR.getAbsoluteFile()));
        }
    }

    private <T> List<T> getList(Iterator<T> iterator) {
        List<T> list = new LinkedList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
