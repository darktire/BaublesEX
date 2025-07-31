package baubles.common.util.command.sub;

import baubles.api.util.BaubleItemsContent;
import baubles.api.util.BaublesContent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

public class CommandModify extends CommandBase {
    @Override
    public String getName() {
        return "mod";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Item item = Item.getByNameOrId(args[0]);
        BaubleItemsContent.itemToBauble(item).setType(BaublesContent.getTypeByName(args[1]));
    }
}
