package com.loucaskreger.breedingtimevisualizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.loucaskreger.breedingtimevisualizer.client.EventSubscriber;
import com.loucaskreger.breedingtimevisualizer.config.Config;
import com.loucaskreger.breedingtimevisualizer.networking.Networking;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BreedingTimeVisualizer.MOD_ID)
public class BreedingTimeVisualizer {
	public static final String MOD_ID = "breedingtimevisualizer";
	public static final Logger LOGGER = LogManager.getLogger();

	public BreedingTimeVisualizer() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
		Networking.registerMessages();
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
	}

	private void setupClient(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(EventSubscriber.key);
	}

}
