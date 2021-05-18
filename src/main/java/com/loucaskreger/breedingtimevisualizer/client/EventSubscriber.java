package com.loucaskreger.breedingtimevisualizer.client;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.loucaskreger.breedingtimevisualizer.BreedingTimeVisualizer;
import com.loucaskreger.breedingtimevisualizer.config.ClientConfig;
import com.loucaskreger.breedingtimevisualizer.config.Config;
import com.loucaskreger.breedingtimevisualizer.networking.Networking;
import com.loucaskreger.breedingtimevisualizer.networking.packet.EntityPositionRequestPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(modid = BreedingTimeVisualizer.MOD_ID, value = Dist.CLIENT)
public class EventSubscriber {

	public static final String TEAM_NAME = "breedingcolor";
	public static List<AnimalEntity> entities = new ArrayList<AnimalEntity>();
	public static Map<Integer, Integer> entityBreedingTimers = new HashMap<Integer, Integer>();
	public static final KeyBinding key = new KeyBinding(BreedingTimeVisualizer.MOD_ID + ".key.breedingtimer",
			GLFW_KEY_B, BreedingTimeVisualizer.MOD_ID + ".key.categories");
	private static final Minecraft mc = Minecraft.getInstance();
	private static boolean toggled = false;

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) {
			Config.bakeConfig();
		}
	}

	@SubscribeEvent
	public static void onRenderLiving(final RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof AnimalEntity) {
			if (entities.contains((AnimalEntity) entity)) {
				Scoreboard scoreboard = mc.level.getScoreboard();
				ScorePlayerTeam team;
				if (scoreboard.getTeamNames().contains(TEAM_NAME)) {
					team = scoreboard.getPlayerTeam(TEAM_NAME);
				} else {
					team = scoreboard.addPlayerTeam(TEAM_NAME);
				}
				team.setColor(TextFormatting.RED);
				scoreboard.addPlayerToTeam(entity.getUUID().toString(), team);
				entity.setGlowing(true);
			}
		}
	}


	@SubscribeEvent
	public static void onClientTick(final ClientTickEvent event) {
		if (mc.level != null) {

			if (key.isDown() && ClientConfig.holdOrToggle.get()) {
				key.setDown(false);
				toggled = !toggled;
			}

			if (ClientConfig.holdOrToggle.get() ? toggled : key.isDown()) {
				removeFromGlowTeam();

				entities = mc.level.getLoadedEntitiesOfClass(AnimalEntity.class,
						mc.player.getBoundingBox().inflate(Math.min(25.0D, ClientConfig.viewRange.get())));
				if (!entities.isEmpty()) {
					entities.forEach(entity -> Networking.INSTANCE
							.sendToServer(new EntityPositionRequestPacket(entity.getId())));
				}
				addToGlowTeam();
			} else {
				Iterator<AnimalEntity> it = entities.iterator();
				while (it.hasNext()) {
					AnimalEntity animal = it.next();
					animal.setGlowing(false);
					it.remove();
				}
				entityBreedingTimers.clear();
			}
		}
	}

	private static void addToGlowTeam() {
		for (AnimalEntity animal : entities) {
			Scoreboard scoreboard = mc.level.getScoreboard();
			ScorePlayerTeam team;
			if (scoreboard.getTeamNames().contains(TEAM_NAME)) {
				team = scoreboard.getPlayerTeam(TEAM_NAME);
			} else {
				team = scoreboard.addPlayerTeam(TEAM_NAME);
			}
			scoreboard.addPlayerToTeam(animal.getUUID().toString(), team);
			animal.setGlowing(true);
		}
	}

	private static void removeFromGlowTeam() {
		Iterator<AnimalEntity> it = entities.iterator();
		while (it.hasNext()) {
			AnimalEntity animal = it.next();
			Scoreboard scoreboard = mc.level.getScoreboard();
			ScorePlayerTeam team;
			if (scoreboard.getTeamNames().contains(TEAM_NAME)) {
				team = scoreboard.getPlayerTeam(TEAM_NAME);
				scoreboard.removePlayerFromTeam(animal.getUUID().toString(), team);
			}
			animal.setGlowing(false);
			if (!(mc.level
					.getLoadedEntitiesOfClass(AnimalEntity.class,
							mc.player.getBoundingBox().inflate(Math.max(25.0D, ClientConfig.viewRange.get())))
					.contains(animal))) {
				it.remove();
				entityBreedingTimers.remove(animal.getId());
			}

		}
	}
}
