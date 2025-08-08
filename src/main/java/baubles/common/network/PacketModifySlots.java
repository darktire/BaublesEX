package baubles.common.network;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.util.TypesData;
import baubles.common.Baubles;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;

public class PacketModifySlots implements IMessage {

    private int playerId;
    private int typeId;
    private int modifier;
    private int addition;

    public PacketModifySlots() {}

    /**
     * @param entity sever player
     * @param typeName  bauble type
     * @param modifier value of modifier, unused when addition > 1
     * @param addition 0 means setting; 1 means addition; 2 means reset; 3 means sync
     */
    public PacketModifySlots(EntityPlayer entity, String typeName, int modifier, int addition) {
        this.playerId = entity.getEntityId();
        BaubleTypeEx type = TypesData.getTypeByName(typeName);
        if (type == null) this.typeId = -1;
        else this.typeId = TypesData.getId(type);
        this.modifier = modifier;
        this.addition = addition;
    }

    /**
     * @param entity sever player
     * @param addition 2 means reset; 3 means sync
     */
    public PacketModifySlots(EntityPlayer entity, int addition) {
        this.playerId = entity.getEntityId();
        this.typeId = 0;
        this.modifier = 0;
        this.addition = addition;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeInt(typeId);
        buffer.writeInt(modifier);
        buffer.writeInt(addition);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        playerId = buffer.readInt();
        typeId = buffer.readInt();
        modifier = buffer.readInt();
        addition = buffer.readInt();
    }

    public static class Handler implements IMessageHandler<PacketModifySlots, IMessage> {
        @Override
        public IMessage onMessage(PacketModifySlots message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> execute(message));
            return null;
        }

        private void execute(PacketModifySlots message) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(message.playerId);
            if (entity instanceof EntityLivingBase) {
                IBaublesModifiable baublesCL = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                if (message.typeId > -1) {
                    if (message.addition == 0) baublesCL.modifySlot(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else if (message.addition == 1) baublesCL.modifySlotOA(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else if (message.addition == 2) baublesCL.clearModifier();
                    else if (message.addition == 3) syncModifier(message, baublesCL);
                }
            }
        }

        private void syncModifier(PacketModifySlots message, IBaublesModifiable baublesCL) {
            WorldServer[] worlds = Baubles.proxy.getSeverWorld();
            Entity entity = null;
            for (WorldServer world1 : worlds) {
                entity = world1.getEntityByID(message.playerId);
                if (entity != null) break;
            }
            IBaublesModifiable baublesSE = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
            Iterator<BaubleTypeEx> iterator = TypesData.iterator();
            iterator.forEachRemaining(type -> {
                String typeName = type.getTypeName();
                baublesCL.modifySlot(typeName, baublesSE.getModifier(typeName));
            });
        }
    }
}