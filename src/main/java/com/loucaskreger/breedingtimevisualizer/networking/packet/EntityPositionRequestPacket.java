package com.loucaskreger.breedingtimevisualizer.networking.packet;

import java.util.function.Supplier;

import com.loucaskreger.breedingtimevisualizer.networking.Networking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class EntityPositionRequestPacket {

	private int id;

	public EntityPositionRequestPacket(PacketBuffer buffer) {
		this.id = buffer.readInt();
	}

	public EntityPositionRequestPacket(int id) {
		this.id = id;

	}

	public void toBytes(PacketBuffer buffer) {
		buffer.writeInt(this.id);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> this.processRequest(context));
		context.get().setPacketHandled(true);
	}

	public void processRequest(Supplier<NetworkEvent.Context> context) {
		Entity entity = context.get().getSender().getCommandSenderWorld().getEntity(this.id);
		if (entity instanceof AnimalEntity) {
			Networking.INSTANCE
					.sendToServer(new EntityPositionResponsePacket(entity.getId(), ((AnimalEntity) entity).getAge()));
		}
	}
}
