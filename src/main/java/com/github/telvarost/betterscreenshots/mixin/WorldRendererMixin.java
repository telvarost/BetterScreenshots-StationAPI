package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.ModHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow private Minecraft client;

	@Redirect(
			method = "renderEntities",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"
			)
	)
	public boolean method_1544(LivingEntity instance) {
		return (this.client.camera.isSleeping() || ModHelper.ModHelperFields.isTakingIsometricScreenshot);
	}
}
