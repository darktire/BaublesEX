package baubles.common.command.sub;

import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.config.json.ConversionHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.io.IOException;

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
            ConversionHelper.typeToJson(TypesData.getOrder(), true);
            ConversionHelper.itemToJson(ItemsData.getList(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Config.Commands.commandLogs) {
            String path = Config.getModDir().getAbsoluteFile().toString();
            Style style = new Style()
                    .setUnderlined(true)
                    .setColor(TextFormatting.BLUE)
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("commands.baubles.dump.tip")))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path));
            sender.sendMessage(new TextComponentTranslation("commands.baubles.dump"));
            sender.sendMessage(new TextComponentString(path).setStyle(style));
        }
    }
}
