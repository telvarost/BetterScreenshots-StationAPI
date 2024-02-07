package com.github.telvarost.hudtweaks.mixin;

import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererInvoker {
    @Accessor("field_2350")
    public void setupCameraTransform(float field_2350);
//
//    @Shadow private Minecraft minecraft;

    @Invoker("method_1842")
    public abstract void setupFog(int i, float f);

    @Invoker("method_1852")
    public abstract void updateFogColor(float f);

    @Invoker("method_1847")
    public abstract void renderRainSnow(float f);

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
