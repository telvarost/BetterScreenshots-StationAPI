package com.github.telvarost.betterscreenshots.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.client.render.ProgressRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ProgressRenderer.class)
public abstract class ProgressListenerImplMixin implements LoadingDisplay {

    @Shadow private String title;

    @Redirect(
            method = "progressStartNoAbort",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/ProgressRenderer;start(Ljava/lang/String;)V"
            )
    )
    public void notifyIgnoreGameRunning(ProgressRenderer instance, String s) {
        instance.start(s);
    }

    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    public void notify(String string, CallbackInfo ci) {
        this.title = string;
    }
}
