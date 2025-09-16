package baubles.common.command.sub;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.stream.Collectors;

public class CommandHand extends CommandBase {
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
