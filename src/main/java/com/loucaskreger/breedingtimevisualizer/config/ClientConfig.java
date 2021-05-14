package com.loucaskreger.breedingtimevisualizer.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfig {

	public static BooleanValue holdOrToggle;

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Hold or Toggle");
		holdOrToggle = builder.comment("Set to true to change view mode from hold to toggle.").define("holdOrToggle",
				false);
	}

}
