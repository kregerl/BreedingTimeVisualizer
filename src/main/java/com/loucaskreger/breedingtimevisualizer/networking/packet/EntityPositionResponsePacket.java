package com.loucaskreger.breedingtimevisualizer.networking.packet;

import java.util.function.Supplier;

import com.loucaskreger.breedingtimevisualizer.client.EventSubscriber;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class EntityPositionResponsePacket {

	private int inLove;
	private int id;

	public EntityPositionResponsePacket(PacketBuffer buffer) {
		this.inLove = buffer.readInt();
		this.id = buffer.readInt();
	}

	public EntityPositionResponsePacket(int id, int inLove) {
		this.inLove = inLove;
		this.id = id;
	}

	public void toBytes(PacketBuffer buffer) {
		buffer.writeInt(this.inLove);
		buffer.writeInt(this.id);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(this::processResponse);
		context.get().setPacketHandled(true);
	}

	public void processResponse() {
		EventSubscriber.entityBreedingTimers.put(this.id, this.inLove);
//		EventSubscriber.inLoveTime = this.inLove;
	}
}
