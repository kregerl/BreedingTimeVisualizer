package com.loucaskreger.breedingtimevisualizer.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfig {

	public static BooleanValue holdOrToggle;
	public static ConfigValue<Double> viewRange;
	public static ConfigValue<String> ableToBreedColor;
	public static ConfigValue<String> unableToBreedColor;

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Hold or Toggle");
		holdOrToggle = builder.comment("Set to true to change view mode from hold to toggle.").define("holdOrToggle",
				false);
		builder.pop();
		builder.push("Rendering");
		viewRange = builder.comment("The range in which you'll be able to see the breeding time of animals (Max: 25.0)")
				.define("viewRange", 5.0D);
		ableToBreedColor = builder.comment(
				"Animals that are able to breed will be shown in this color. Note: You do not need to put 0's before the hex colors. Default: Green (00FF00) ")
				.define("ableToBreedColor", Integer.toHexString(0x00FF00));
		unableToBreedColor = builder.comment(
				"Animals that are able to breed will be shown in this color. Note: You do not need to put 0's before the hex colors. Default: Red (FF0000)")
				.define("unableToBreedColor", Integer.toHexString(0xFF0000));
		builder.pop();
	}

}
