package com.github.telvarost.hudtweaks.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.ProgressListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProgressListenerImpl.class)
@Environment(EnvType.CLIENT)
public abstract class ProgressListenerImplMixin implements ProgressListener {
}
