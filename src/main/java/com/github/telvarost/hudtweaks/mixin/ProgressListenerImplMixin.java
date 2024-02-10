package com.github.telvarost.hudtweaks.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.ProgressListenerError;
import net.minecraft.util.ProgressListenerImpl;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProgressListenerImpl.class)
@Environment(EnvType.CLIENT)
public abstract class ProgressListenerImplMixin implements ProgressListener {

    @Shadow private String message;

    @Redirect(
            method = "notifyIgnoreGameRunning",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ProgressListenerImpl;notify(Ljava/lang/String;)V"
            )
    )
    public void notifyIgnoreGameRunning(ProgressListenerImpl instance, String s) {
        instance.notify(s);
    }

    @Inject(method = "notify", at = @At("HEAD"), cancellable = true)
    public void notify(String string, CallbackInfo ci) {
        this.message = string;
    }
}
