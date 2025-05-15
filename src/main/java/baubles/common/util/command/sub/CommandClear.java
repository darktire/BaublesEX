package baubles.common.util.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.util.command.CommandBaubles;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClear extends CommandBase {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = CommandBaubles.checkPlayer(server, sender, args);

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

        if (args.length < 2) {
            for (int a = 0; a<baubles.getSlots();a++) {
                baubles.setStackInSlot(a, ItemStack.EMPTY);
            }
            sender.sendMessage(new TextComponentTranslation("commands.baubles.clear.all", player.getName()));
            player.sendMessage(new TextComponentTranslation("commands.baubles.clear.all.claim", sender.getName()));
        }
        else {
            int slot = -1;
            try {
                slot = Integer.parseInt(args[1]);
            } catch (Exception e) {
                throw new CommandException("commands.baubles.args.error");
            }
            if (slot < 0 || slot >= baubles.getSlots()) {
                throw new CommandException("commands.baubles.index.out", slot);
            } else {
                baubles.setStackInSlot(slot, ItemStack.EMPTY);
                sender.sendMessage(new TextComponentTranslation("commands.baubles.clear", slot, player.getName()));
                player.sendMessage(new TextComponentTranslation("commands.baubles.clear.claim", slot, sender.getName()));
            }
        }

    }
}
