package com.github.telvarost.hudtweaks.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ScreenshotManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScreenshotManager.class)
@Environment(EnvType.CLIENT)
public class ScreenshotManagerMixin {
}
