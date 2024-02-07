package com.github.telvarost.hudtweaks.mixin;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.BufferedReader;
import java.io.PrintWriter;

/** - All credit for the code in this class goes to Dany and his mod UniTweaks
 *  See: https://github.com/DanyGames2014/UniTweaks
 */
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Shadow
    protected abstract float parseFloat(String string);

    @Inject(method = "method_1228", at = @At(value = "HEAD"))
    public void finalBeta_setFloat(Option option, float value, CallbackInfo ci) {
    }

    @Inject(method = "changeOption", at = @At(value = "HEAD"))
    public void finalBeta_setBoolean(Option option, int value, CallbackInfo ci) {
    }


    @Inject(method = "getFloatValue", at = @At(value = "HEAD"), cancellable = true)
    public void finalBeta_getFloat(Option option, CallbackInfoReturnable<Float> cir) {
    }

    @Inject(method = "getBooleanValue", at = @At(value = "HEAD"), cancellable = true)
    public void finalBeta_getBoolean(Option option, CallbackInfoReturnable<Boolean> cir) {
    }


    @Inject(method = "getTranslatedValue", at = @At(value = "HEAD"), cancellable = true)
    public void finalBeta_getTranslatedValue(Option option, CallbackInfoReturnable<String> cir) {
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void finalBeta_load(CallbackInfo ci, BufferedReader bufferedReader, String string) {
    }

    @Inject(method = "saveOptions", at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;close()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void finalBeta_saveOptions(CallbackInfo ci, PrintWriter printWriter) {
    }
}
