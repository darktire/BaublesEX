package baubles.common.command.sub;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import baubles.common.command.BaublesCommand;
import baubles.common.network.PacketPool;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.stream.Collectors;

public class CommandView extends BaublesCommand {
    public CommandView() {
        super(false);
        this.addSubcommand(new Hand());
        this.addSubcommand(new Network());
    }

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
        try {
            EntityPlayerMP player = BaublesCommand.checkPlayer(server, sender, args);

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
            TypesData.applyToTypes(type -> {
                String typeName = type.getTypeName();
                int i = baubles.getModifier(typeName);
                sender.sendMessage(new TextComponentTranslation(typeName + " " + i));
            });
        } catch (CommandException e) {
            super.execute(server, sender, args);
        }

    }

    private static class Hand extends CommandBase {
        @Override
        public String getName() {
            return "hand";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return null;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
            if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                ItemStack heldItem = player.getHeldItemMainhand();

                if(!heldItem.isEmpty()) {
                    IBauble bauble = BaublesApi.toBauble(heldItem);
                    String s = "undefined";
                    if (bauble != null) {
                        s = bauble.getTypes(heldItem)
                                .stream()
                                .map(BaubleTypeEx::getTranslateKey)
                                .map(I18n::format)
                                .collect(Collectors.joining(", "));
                    }
                    int meta = heldItem.getMetadata();
                    String metaInfo = meta == 0 ? "" : ":" + meta;
                    String item = String.valueOf(heldItem.getItem().getRegistryName());
                    sender.sendMessage(new TextComponentTranslation("commands.baubles.hand", item, metaInfo, s));
                }
            }
        }
    }


    private static class Network extends CommandBase {
        @Override
        public String getName() {
            return "network";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return null;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
            sender.sendMessage(new TextComponentTranslation(PacketPool.getStats()));
        }
    }
}
