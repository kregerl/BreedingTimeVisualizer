package com.loucaskreger.breedingtimevisualizer.networking;

import com.loucaskreger.breedingtimevisualizer.BreedingTimeVisualizer;
import com.loucaskreger.breedingtimevisualizer.networking.packet.EntityPositionRequestPacket;
import com.loucaskreger.breedingtimevisualizer.networking.packet.EntityPositionResponsePacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

	public static SimpleChannel INSTANCE;
	private static int id = 0;

	public static int nextId() {
		return id++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(BreedingTimeVisualizer.MOD_ID, "breeding"),
				() -> "1.0", s -> true, s -> true);
		INSTANCE.registerMessage(nextId(), EntityPositionRequestPacket.class, EntityPositionRequestPacket::toBytes,
				EntityPositionRequestPacket::new, EntityPositionRequestPacket::handle);

		INSTANCE.registerMessage(nextId(), EntityPositionResponsePacket.class, EntityPositionResponsePacket::toBytes,
				EntityPositionResponsePacket::new, EntityPositionResponsePacket::handle);
	}

}
