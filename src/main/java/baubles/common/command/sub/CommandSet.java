package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.command.BaublesCommand;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSet extends CmdBase {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);
        Item item = Item.getByNameOrId(args[2]);
        if (item != null && args[1].matches("\\d+")) {
            int slot = Integer.parseInt(args[1]);
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            BaublesCommand.checkSlot(baubles, slot);
            ItemStack stack = baubles.getStackInSlot(slot);
            ItemStack stack1 = new ItemStack(item, 1, args.length > 3 ? parseInt(args[3]) : 0);

            if (args.length > 4)
            {
                String s = buildString(args, 4);

                try
                {
                    stack1.setTagCompound(JsonToNBT.getTagFromJson(s));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.give.tagError", nbtexception.getMessage());
                }
            }

            if (!ItemStack.areItemStacksEqual(stack, stack1)) {
                baubles.setStackInSlot(slot, stack1);
                PacketSync pkt = PacketSync.S2CPack(player, slot, stack1, -1);
                PacketHandler.INSTANCE.sendTo(pkt, player);
                if (BaublesApi.isBauble(stack)) {
                    BaublesApi.toBauble(stack).onEquipped(stack, player);
                }
                if (BaublesApi.isBauble(stack1)) {
                    BaublesApi.toBauble(stack1).onUnequipped(stack1, player);
                }
                if (Config.Commands.commandLogs) {
                    sender.sendMessage(new TextComponentTranslation("commands.baubles.set", item.getRegistryName(), slot, player.getName()));
                }
            }
        }
        else {
            BaublesCommand.sendError(sender);
        }
    }
}
