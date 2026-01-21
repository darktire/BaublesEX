package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.command.BaublesCommand;
import baubles.common.config.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClear extends CmdBase {
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
        EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
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
//        else if (args[1].equals("modifier")) {
//            baubles.clearModifier();
//            PacketHandler.INSTANCE.sendTo(new PacketModifier(player, null, 0, 0), player);
//            baubles.updateContainer();
//            if (Config.Commands.commandLogs) {
//                player.sendMessage(new TextComponentTranslation("commands.baubles.success"));
//            }
//        }
        else if (args[1].matches("\\d+")) {
            clearOnce(sender, args[1], baubles, player);
            if (args.length > 2) {
                for (int i = 2; i < args.length; i++){
                    clearOnce(sender, args[i], baubles, player);
                }
            }
        }
    }

    private void clearOnce(ICommandSender sender, String s, IBaublesItemHandler baubles, EntityPlayerMP player) throws CommandException {
        int slot = Integer.parseInt(s);
        BaublesCommand.checkSlot(baubles, slot);
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
