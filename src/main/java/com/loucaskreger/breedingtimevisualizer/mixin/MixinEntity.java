package com.loucaskreger.breedingtimevisualizer.mixin;

import java.awt.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.loucaskreger.breedingtimevisualizer.client.EventSubscriber;
import com.loucaskreger.breedingtimevisualizer.config.ClientConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;

@Mixin(Entity.class)
public class MixinEntity {

	private static final float MAX_IN_LOVE = 6000f;
	private Color start = new Color(Integer.parseInt(ClientConfig.unableToBreedColor.get(), 16)); // 16733525
	private Color end = new Color(Integer.parseInt(ClientConfig.ableToBreedColor.get(), 16)); // 5635925

	@Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
	private void getTeamColor(CallbackInfoReturnable<Integer> ci) {
		Entity entity = (Entity) (Object) this;
		Team team = entity.getTeam();

		if (entity instanceof AnimalEntity) {
			AnimalEntity animal = (AnimalEntity) entity;
			if (EventSubscriber.entities.contains(animal)
					&& EventSubscriber.entityBreedingTimers.containsKey(animal.getId())) {
				if (team.getName().equals(EventSubscriber.TEAM_NAME)) {
					float percent = (float) EventSubscriber.entityBreedingTimers.get(animal.getId()) / MAX_IN_LOVE;
					if (percent < 0) {
						ci.setReturnValue(start.getRGB());
					} else {
						ci.setReturnValue(this.lerpColor(end, start, percent).getRGB());
					}
				}
			}
		}
	}

	private Color lerpColor(Color start, Color end, float f) {
		int r, g, b;
		// MathHelper.lerp(f, a, b)
		r = (int) MathHelper.lerp(f, start.getRed(), end.getRed());
		g = (int) MathHelper.lerp(f, start.getGreen(), end.getGreen());
		b = (int) MathHelper.lerp(f, start.getBlue(), end.getBlue());
		Color c = new Color(r, g, b);
		return c;
	}

}
