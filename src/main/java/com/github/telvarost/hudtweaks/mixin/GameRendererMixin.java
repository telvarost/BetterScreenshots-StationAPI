package com.github.telvarost.hudtweaks.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.sortme.GameRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    private float field_2350;

    @Shadow private Minecraft minecraft;

    @Shadow protected abstract void method_1842(int i, float f);

    @Shadow protected abstract void method_1852(float f);

    @Shadow protected abstract void method_1847(float f);

//    @Unique
//    public void setupCameraTransform() {
//        this.field_2350 = (float)(512 >> (this.minecraft.options.viewDistance << 1));
//    }
//
//    @Unique
//    public void renderRainSnow() {
//        this.method_1847(0.0F);
//    }
//
//    @Unique
//    public void updateFogColor() {
//        this.method_1852(0.0F);
//    }
//
//    @Unique
//    public void setupFog() {
//        this.method_1842(0, 0.0F);
//    }
}
