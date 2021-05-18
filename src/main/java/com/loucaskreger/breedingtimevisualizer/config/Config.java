package com.loucaskreger.breedingtimevisualizer.config;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	public static boolean holdOrToggle;
	public static double viewRange;
	public static int ableToBreedColor;
	public static int unableToBreedColor;

	static {
		final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static void bakeConfig() {
		holdOrToggle = ClientConfig.holdOrToggle.get();
		viewRange = ClientConfig.viewRange.get();
		ableToBreedColor = Integer.parseInt(ClientConfig.ableToBreedColor.get());
		ableToBreedColor = Integer.parseInt(ClientConfig.ableToBreedColor.get());
	}
}
