package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.command.CommandBaubles;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
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
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        if (args.length < 2) {
            for (int a = 0;;a++) {
                if (a >= baubles.getSlots()) break;
                ItemStack stack = baubles.getStackInSlot(a);
                if (stack.isEmpty()) continue;
                BaublesApi.toBauble(stack).onUnequipped(stack, player);
                if (Config.Commands.dropBaubles) player.dropItem(stack, false);
                baubles.setStackInSlot(a, ItemStack.EMPTY);
            }
            if (Config.Commands.commandLogs) {
                sender.sendMessage(new TextComponentTranslation("commands.baubles.clear.all", player.getName()));
                player.sendMessage(new TextComponentTranslation("commands.baubles.clear.all.claim", sender.getName()));
            }
        }
        else if (args[1].equals("modifier")) {
            baubles.clearModifier();
            PacketHandler.INSTANCE.sendTo(new PacketModifySlots(player, 2), player);
            baubles.updateContainer();
            player.sendMessage(new TextComponentTranslation("commands.baubles.success"));
        }
        else if (args[1].matches("\\d+")) {
            int slot = Integer.parseInt(args[1]);
            if (slot >= baubles.getSlots()) {
                throw new CommandException("commands.baubles.index.out", slot);
            }
            else {
                ItemStack stack = baubles.getStackInSlot(slot);
                BaublesApi.toBauble(stack).onEquipped(stack, player);
                if (Config.Commands.dropBaubles) player.dropItem(stack, false);
                baubles.setStackInSlot(slot, ItemStack.EMPTY);
                if (Config.Commands.commandLogs) {
                    sender.sendMessage(new TextComponentTranslation("commands.baubles.clear", slot, player.getName()));
                    player.sendMessage(new TextComponentTranslation("commands.baubles.clear.claim", slot, sender.getName()));
                }
            }
        }
    }
}
