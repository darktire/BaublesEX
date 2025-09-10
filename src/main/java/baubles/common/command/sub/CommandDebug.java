package baubles.common.command.sub;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifySlots;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CommandDebug extends CommandBase {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Entity entity = sender.getCommandSenderEntity();
        if (args[0].equals("change")) {
            Item item = Item.getByNameOrId(args[1]);
            ItemsData.toBauble(item).setType(TypesData.getTypeByName(args[2]));
        }
        else if (args[0].equals("mod")) {
            if (entity instanceof EntityLivingBase && TypesData.hasType(args[1]) && args[2].matches("-?\\d+")) {
                int modifier = Integer.parseInt(args[2]);
                IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.modifySlot(args[1], modifier);
                PacketHandler.INSTANCE.sendTo(new PacketModifySlots((EntityPlayer) entity, args[1], modifier, 0), (EntityPlayerMP) entity);
                baubles.updateContainer();
            }
        }
        else if (args[0].equals("view") && entity instanceof EntityLivingBase) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
            Iterator<BaubleTypeEx> iterator = TypesData.iterator();
            iterator.forEachRemaining(type -> {
                int i = baubles.getModifier(type.getTypeName());
                sender.sendMessage(new TextComponentTranslation("" + i));
            });
        }
        else if (args[0].equals("dump")) {// todo dump out, info
            try {
                Config.json.typeToJson(this.getList(TypesData.iterator()));
                Config.json.itemToJson(this.getList(ItemsData.iterator()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            sender.sendMessage(new TextComponentTranslation("commands.baubles.error"));
            sender.sendMessage(new TextComponentTranslation("commands.baubles.help.get"));
        }
    }

    private <T> List<T> getList(Iterator<T> iterator) {
        List<T> list = new LinkedList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
