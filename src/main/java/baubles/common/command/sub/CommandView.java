package baubles.common.command.sub;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.common.command.CommandBaubles;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandView extends CommandBase {

    @Override
    public String getName() {
        return "view";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = CommandBaubles.checkPlayer(server, sender, args);

        IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);

        sender.sendMessage(new TextComponentTranslation("commands.baubles.view.info", player.getName()));
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (!stack.isEmpty()) {
                sender.sendMessage(new TextComponentTranslation("commands.baubles.view", i, stack.getDisplayName(), baubles.getTypeInSlot(i)));
            }
            else {
                sender.sendMessage(new TextComponentTranslation("commands.baubles.view", i, "nothing", baubles.getTypeInSlot(i)));
            }
        }
    }
}
