package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.ModHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Living;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow private Minecraft client;

	@Redirect(
			method = "method_1544",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Living;isSleeping()Z"
			)
	)
	public boolean method_1544(Living instance) {
		return (this.client.viewEntity.isSleeping() || ModHelper.ModHelperFields.isTakingIsometricScreenshot);
	}
}
