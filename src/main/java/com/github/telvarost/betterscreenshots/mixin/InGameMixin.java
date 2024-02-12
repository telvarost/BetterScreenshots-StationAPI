//package com.github.telvarost.betterscreenshots.mixin;
//
//import com.github.telvarost.betterscreenshots.Config;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.DrawableHelper;
//import net.minecraft.client.gui.InGame;
//import net.minecraft.client.gui.screen.ingame.Chat;
//import org.lwjgl.input.Mouse;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.*;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.List;
//
//@Mixin(InGame.class)
//@Environment(value= EnvType.CLIENT)
//public abstract class InGameMixin extends DrawableHelper {
//
//    @Shadow private Minecraft minecraft;
//
//    @Shadow private List chatMessages;
//
//    @Unique private Integer numberOfTurns = 0;
//
//    @Unique private Integer chatOffset = 0;
//
//    @Redirect(
//            method = "renderHud",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/InGame;blit(IIIIII)V",
//                    ordinal = 0
//            )
//    )
//    private void betterScreenshots_renderHudHotbarPosition(InGame instance, int i, int j, int k, int l, int m, int n) {
//        instance.blit(i, j - Config.ConfigFields.hotbarYPositionOffset, k, l, m, n);
//    }
//
//    @Redirect(
//            method = "renderHud",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/InGame;blit(IIIIII)V",
//                    ordinal = 1
//            )
//    )
//    private void betterScreenshots_renderHudSelectedItemBorderPosition(InGame instance, int i, int j, int k, int l, int m, int n) {
//        instance.blit(i, j - Config.ConfigFields.hotbarYPositionOffset, k, l, m, n);
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(intValue = 32, ordinal = 0)
//    )
//    private int betterScreenshots_renderHudStatusBarPositions0(int value) {
//        return value + Config.ConfigFields.hotbarYPositionOffset;
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(intValue = 32, ordinal = 1)
//    )
//    private int betterScreenshots_renderHudStatusBarPositions1(int value) {
//        return value + Config.ConfigFields.hotbarYPositionOffset;
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(intValue = 32, ordinal = 2)
//    )
//    private int betterScreenshots_renderHudStatusBarPositions2(int value) {
//        return value + Config.ConfigFields.hotbarYPositionOffset;
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(intValue = 16, ordinal = 5)
//    )
//    private int betterScreenshots_renderHudItemPositions(int value) {
//        return value + Config.ConfigFields.hotbarYPositionOffset;
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(intValue = 200)
//    )
//    private int betterScreenshots_renderHudChatFadeTime(int value) {
//        return (Config.ConfigFields.chatFadeTime * 2);
//    }
//
//    @ModifyConstant(
//            method = "renderHud",
//            constant = @Constant(doubleValue = 200.0)
//    )
//    private double betterScreenshots_renderHudChatFadeTimeDivisor(double value) {
//        return (Config.ConfigFields.chatFadeTime * 2);
//    }
//
//    @Inject(method = "renderHud", at = @At("HEAD"), cancellable = true)
//    public void betterScreenshots_renderHudChatScroll(float f, boolean bl, int i, int j, CallbackInfo ci) {
//        if (!Config.ConfigFields.enableChatScroll) {
//            return;
//        }
//
//        int chatRangeTop = 20;
//
//        if (this.minecraft.currentScreen instanceof Chat) {
//            int currentWheelDegrees = Mouse.getDWheel();
//            numberOfTurns = Math.round((float)currentWheelDegrees / 120.0f);
//            chatOffset = chatOffset + numberOfTurns;
//
//            if (chatOffset < 0) {
//                chatOffset = 0;
//            }
//
//            if (chatRangeTop < this.chatMessages.size()) {
//                if (this.chatMessages.size() <= (chatRangeTop + chatOffset)) {
//                    chatOffset = this.chatMessages.size() - chatRangeTop;
//                }
//            } else {
//                chatOffset = 0;
//            }
//        } else {
//            chatOffset = 0;
//        }
//    }
//
//    @Redirect(
//            method = "renderHud",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
//            )
//    )
//    public Object betterScreenshots_renderHudChatOffset(List instance, int i) {
//        if (Config.ConfigFields.enableChatScroll) {
//            return instance.get(i + chatOffset);
//        } else {
//            return instance.get(i);
//        }
//    }
//
//    @ModifyConstant(
//            method = "addChatMessage",
//            constant = @Constant(intValue = 50)
//    )
//    public int chatLog_addChatMessageLimit(int value) {
//        return Config.ConfigFields.chatHistorySize;
//    }
//}
